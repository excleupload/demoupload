package com.example.tapp.business.service.implementation;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.web.multipart.MultipartFile;

import com.example.tapp.business.service.UserService;
import com.example.tapp.common.discriminator.Gender;
import com.example.tapp.common.discriminator.SocialMedia;
import com.example.tapp.common.discriminator.UserRole;
import com.example.tapp.common.discriminator.UserStatus;
import com.example.tapp.common.dto.SocialMediaDto;
import com.example.tapp.common.dto.UserDeviceDto;
import com.example.tapp.common.dto.UserDto;
import com.example.tapp.common.list.helper.Order;
import com.example.tapp.common.list.helper.PageResponse;
import com.example.tapp.common.notification.MailNotification;
import com.example.tapp.common.utils.AppUtils;
import com.example.tapp.common.utils.OtpManager;
import com.example.tapp.common.utils.TokenGenerator;
import com.example.tapp.common.utils.YamlPropertySourceFactory;
import com.example.tapp.data.dao.ConnectionRequestDao;
import com.example.tapp.data.dao.MessageDao;
import com.example.tapp.data.dao.MessageDialogDao;
import com.example.tapp.data.dao.ReportDao;
import com.example.tapp.data.dao.UserConnectionDao;
import com.example.tapp.data.dao.UserDao;
import com.example.tapp.data.dao.UserNotificationDao;
import com.example.tapp.data.entities.Report;
import com.example.tapp.data.entities.User;
import com.example.tapp.data.entities.UserConnection;
import com.example.tapp.data.entities.UserNotification;
import com.example.tapp.data.entities.UserProfile;
import com.example.tapp.data.entities.UserSocialMedia;
import com.example.tapp.data.exception.GeneralException;
import com.example.tapp.data.exception.RecordNotFoundException;
import com.example.tapp.data.exception.UserNotFoundException;

@Service
@PropertySource(factory = YamlPropertySourceFactory.class, value = "classpath:utils.yml")
public class UserServiceImpl implements UserService {

	private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

	@Value("${tapp.application.user.default-image}")
	private String DEFAULT_IMAGE;

	@Value("${tapp.application.user.token.validity}")
	private Integer USER_TOKEN_VALIDITY;

	@Value("${tapp.application.user.profileImage}")
	private String IMAGE_PATH;

	@Autowired
	private UserConnectionDao connDao;

	@Autowired
	private UserDao userDao;

	@Autowired
	private OtpManager otpManeger;

	@Autowired
	private SmsNotification smsNotify;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private ConnectionRequestDao connReqDao;

	@Autowired
	private MessageDialogDao dialogDao;

	@Autowired
	private MessageDao messageDao;

	@Autowired
	private UserNotificationDao userNotifyDao;

	@Autowired
	private ReportDao reportDao;

	@Autowired
	private com.example.tapp.common.utils.FileHandler fileHandler;

	@Autowired
	private MailNotification mailNotify;

	@PostConstruct
	public void init() {
		this.systemUser();
	}

	private void systemUser() {
		User persistSys = userDao.getSystemUser();
		persistSys = persistSys == null ? new User() : persistSys;
		persistSys.setFirstName("Tapp");
		persistSys.setLastName("Team");
		persistSys.setEmail("tapp.team@mail.com");
		persistSys.setRole(UserRole.ROLE_SYSTEM);
		persistSys.setCreatedOn(new Date());
		persistSys.setAuthToken("");
		persistSys.setStatus(UserStatus.ACTIVE);
		persistSys.setIsRead(true);
		if (persistSys.getProfile() == null) {
			UserProfile profile = new UserProfile();
			profile.setProfileImageName(DEFAULT_IMAGE);
			persistSys.setProfile(profile);
		}
		userDao.update(persistSys);
	}

	@Override
	public UserDto save(UserDto userDto) {
		boolean isNew = false;
		User user = userDao.getUserByFacebookId(userDto.getFacebookId());
		if (user == null) {
			isNew = true;
			user = new User(userDto);
			user.setProfile(new UserProfile());
			user.setEmail(userDto.getEmail());
			user.setMobileVerified(false);
			user.setIsRead(false);
		} else {
			user.setMobileVerified(user.getMobile().equals(userDto.getMobile()) && user.isMobileVerified());
		}
		user.setFirstName(userDto.getFirstName());
		user.setLastName(userDto.getLastName());
		user.setCountryCode(userDto.getCountryCode());
		user.setMobile(userDto.getMobile());
		user.setFacebookId(userDto.getFacebookId());
		user.setAuthToken("");

		UserProfile profile = user.getProfile();
		if ((userDto.getProfileImage() == null || userDto.getProfileImage().isEmpty())) {
			if (profile.getProfileImageName() == null || profile.getProfileImageName().isEmpty()) {
				profile.setProfileImageName(DEFAULT_IMAGE);
			}
		} else {
			profile.setProfileImageName(userDto.getProfileImage());
		}
		profile.setGender(userDto.getGender());
		profile.setDob(userDto.getDob() == null ? Long.MIN_VALUE : userDto.getDob());
		profile.setAge(userDto.getDob() == null ? 0 : AppUtils.getAge(userDto.getDob()));

		user = isNew ? userDao.save(user) : userDao.update(user);
		if (!user.isMobileVerified()) {
			// Create OTP
			String otp = otpCreateAndStore(user);
			log.info("OTP : {}", otp);
			// send message
			smsNotify.sendOTP(user.getCountryCode(), user.getMobile(), otp);
		}

		// Set Auth Token
		user.setAuthToken(TokenGenerator.createToken(user.getId(), user.getEmail()));
		userDao.update(user);
		return user.toDto();
	}

	@Override
	public boolean verifyOtp(UserDto dto) {
		try {
			User persist = userDao.getUserById(dto.getId());

			// Verify OTP
			if (otpManeger.verifyOtp(persist.getId().toString(), dto.getOtp())) {
				persist.setMobileVerified(true);
				userDao.update(persist);

				// Remove OTP from cache
				otpManeger.removeOtp(persist.getId().toString());
				return true;
			}
		} catch (UserNotFoundException e) {
			log.info(e.getMessage());
			return false;
		}
		return false;
	}

	@Override
	public void resendOtp(UUID id) throws GeneralException {
		try {
			User user = userDao.getUserById(id);
			String otp = otpCreateAndStore(user);

			log.info("OTP : {}", otp);

			smsNotify.sendOTP(user.getCountryCode(), user.getMobile(), otp);
		} catch (UserNotFoundException e) {
			log.info(e.getMessage());
			throw new GeneralException(e.getMessage());
		}
	}

	@Override
	public boolean checkToken(String token) throws GeneralException, RecordNotFoundException {
		String[] tokenData = TokenGenerator.extract(token);
		try {
			User user = userDao.getUserById(UUID.fromString(tokenData[0]));
			if (user.getAuthToken() == null || !user.getAuthToken().equals(token)
					|| user.getStatus().equals(UserStatus.BLOCKED)) {
				throw new GeneralException("Authentication token is not valid.");
			}

			if (!user.getEmail().equals(tokenData[1])) {
				throw new GeneralException("Authentication token is not valid.");
			}

			long tokenTime = Long.parseLong(tokenData[2]) + TokenGenerator.getValidityTime(USER_TOKEN_VALIDITY);
			if (tokenTime < System.currentTimeMillis()) {
				throw new GeneralException("Authentication token is not valid.");
			}

		} catch (UserNotFoundException ex) {
			throw new GeneralException("Authentication token is not valid.");
		}
		return true;
	}

	@Override
	public UserDto details(UUID id) throws GeneralException {
		try {
			UserDto dto = userDao.getUserById(id).toDto();
			dto.setAuthToken(null);
			return dto;
		} catch (UserNotFoundException e) {
			log.info(e.getMessage());
			throw new GeneralException(e.getMessage());
		}
	}

	@Override
	public void addDevice(UserDeviceDto dto) throws GeneralException {
		try {
			userDao.addUserDevice(dto.getUserId(), dto.getType(), dto.getDeviceToken());
		} catch (UserNotFoundException e) {
			log.info(e.getMessage());
			throw new GeneralException(e.getMessage());
		}
	}

	@Override
	public void logout(UUID userId) throws GeneralException {
		try {
			userDao.removeUserDevice(userId);
			userDao.removeUserToken(userId);
		} catch (UserNotFoundException e) {
			log.info(e.getMessage());
			throw new GeneralException(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void profile(HashMap<String, Object> params) throws GeneralException {
		try {
			UUID userId = UUID.fromString((String) params.get(UserDto.ID));
			User user = userDao.getUserById(userId);
			UserProfile profile = user.getProfile();
			user.setIsRead(true);
			params.forEach((key, value) -> {
				switch (key) {
				case UserDto.FIRST_NAME:
					user.setFirstName((String) params.get(UserDto.FIRST_NAME));
					break;
				case UserDto.LAST_NAME:
					user.setLastName((String) params.get(UserDto.LAST_NAME));
					break;
				case UserDto.EMAIL:
					user.setEmail((String) params.get(UserDto.EMAIL));
					break;
				case UserDto.AGE:
					profile.setAge((int) params.get(UserDto.AGE));
					break;
				case UserDto.DOB:
					if (params.get(UserDto.DOB) instanceof Integer) {
						profile.setAge(AppUtils.getAge((int) params.get(UserDto.DOB)));
						profile.setDob(new Long((int) params.get(UserDto.DOB)));
					} else if (params.get(UserDto.DOB) instanceof Long) {
						profile.setAge(AppUtils.getAge((long) params.get(UserDto.DOB)));
						profile.setDob((long) params.get(UserDto.DOB));
					}
					break;
				case UserDto.JOB_TITLE:
					profile.setJobTitle((String) params.get(UserDto.JOB_TITLE));
					break;
				case UserDto.EDUCATION_PLACE:
					profile.setEducationPlace((String) params.get(UserDto.EDUCATION_PLACE));
					break;
				case UserDto.ABOUT:
					profile.setAbout((String) params.get(UserDto.ABOUT));
					break;
				case UserDto.ADRESS:
					profile.setAddress((String) params.get(UserDto.ADRESS));
					break;
				case UserDto.WEBSITE:
					profile.setWebSite((String) params.get(UserDto.WEBSITE));
					break;
				case UserDto.COUNTRY_CODE:
					user.setCountryCode((String) params.get(UserDto.COUNTRY_CODE));
					break;

				case UserDto.STATUS:
					user.setStatus(UserStatus.valueOf((String) params.get(UserDto.STATUS)));
					String s = "{1}";
					if (user.getStatus() == UserStatus.BLOCKED) {
						Object[] args = { "TAPP - Deactive Mail", s.replace("{1}", user.getFirstName()) };
						mailNotify.sendDeactiveMail(user.getEmail(), args);
					}

					break;
				case UserDto.GENDER:
					user.getProfile().setGender(Gender.valueOf((String) params.get(UserDto.GENDER)));
					break;
				case UserDto.MOBILE:
					String mobile = (String) params.get(UserDto.MOBILE);
					if (!mobile.equals(user.getMobile())) {
						user.setMobileVerified(false);
						// SEND OTP HERE
						String otp = otpCreateAndStore(user);
						System.out.println(otp);
						smsNotify.sendOTP(user.getCountryCode(), user.getMobile(), otp);
					}
					user.setMobile(mobile);
					break;
				case UserDto.SOCIAL_MEDIA:
					((List<HashMap<String, Object>>) params.get(UserDto.SOCIAL_MEDIA)).forEach((media) -> {
						UserSocialMedia socialMedia = profile.getSocialMedia().stream()
								.filter(sm -> sm.getMedia()
										.equals(SocialMedia.valueOf((String) media.get(SocialMediaDto.SOCIAL_MEDIA))))
								.findFirst().orElse(new UserSocialMedia());
						if (socialMedia.getId() == null) {
							profile.getSocialMedia().add(socialMedia);
							socialMedia.setProfile(profile);
							socialMedia.setCreatedOn(new Date());
						}
						socialMedia.setContent((String) media.get(SocialMediaDto.CONTENT));
						socialMedia.setMedia(SocialMedia.valueOf((String) media.get(SocialMediaDto.SOCIAL_MEDIA)));
						socialMedia.setShared(Boolean.valueOf((Boolean) media.get(SocialMediaDto.SHARED)));
						socialMedia.setModifiedOn(new Date());
					});
					break;
				}
			});
			userDao.update(user);
		} catch (Exception ex) {
			log.info(ex.getMessage());
			ex.printStackTrace();
			throw new GeneralException(ex.getMessage());
		}
	}

	@Override
	public UserDto getProfile(UUID userId, UUID connectionId, Boolean visited) throws GeneralException {
		try {
			User user = userDao.getUserById(userId);
			UserDto userDto = user.toDto();
			if (userDto.getIsRead() == false) {
				userDto.setIsRead(true);
			}
			if (connectionId != null) {
				try {
					UserConnection connection = connDao.getById(connectionId);
					if (visited != null && visited) {
						connection.setProfileVisited(visited);
						connDao.update(connection);
					}
				} catch (Exception ex) {
					log.info(ex.getMessage());
				}
			}

			userDto.setAuthToken(null);

			UserProfile profile = user.getProfile();
			userDto.setJobTitle(profile.getJobTitle());
			userDto.setAbout(profile.getAbout());
			if (profile.getDob() != Long.MIN_VALUE) {
				userDto.setAge(AppUtils.getAge(profile.getDob()));
				userDto.setDob(profile.getDob());
			} else {
				userDto.setAge(0);
				userDto.setDob(0L);
			}

			userDto.setEducationPlace(profile.getEducationPlace());
			userDto.setWebSite(profile.getWebSite());
			userDto.setAddress(profile.getAddress());
			userDto.setSocialMedia(profile.getSocialMedia().stream().map(sm -> sm.dto()).collect(Collectors.toList()));
			userDto.setGender(profile.getGender());
			return userDto;
		} catch (UserNotFoundException e) {
			log.info(e.getMessage());
			throw new GeneralException(e.getMessage());
		}
	}

	private String otpCreateAndStore(User user) {
		String otp = otpManeger.generate();
		// String otp = "123456";
		// Store OTP in cache
		otpManeger.storeOtp(user.getId().toString(), otp);
		return otp;
	}

	@Override
	@org.springframework.transaction.annotation.Transactional(propagation = Propagation.REQUIRED)
	public UserDto getUserDetailsByToken(String token) throws UserNotFoundException {
		String[] tokenData = TokenGenerator.extract(token);
		return userDao.getUserById(UUID.fromString(tokenData[0])).toDto();
	}

	@Override
	public List<UserDto> getListforEmail() {
		try {
			return userDao.getListforEmail().stream().map(UserDto::new).collect(Collectors.toList());
		} catch (Exception e) {
			throw new IllegalArgumentException("User not Found");
		}
	}

	public List<UserDto> getManageUserList() {
		return userDao.getManageUser().stream().map(UserDto::new).collect(Collectors.toList());
	}

	@SuppressWarnings("unchecked")
	@Override
	public PageResponse<?> getUserProfileList(HashMap<String, Object> options) {
		// Page page = (Page) options.get(Page.PAGE);

		HashMap<String, Object> filter = (HashMap<String, Object>) options
				.get(com.example.tapp.common.list.helper.Filter.FILTER);
		PageResponse<UserDto> pageResponse = new PageResponse<>();

		if (filter != null && !filter.isEmpty() && filter.get(User.STATUS) != null
				&& filter.get(User.STATUS).equals("REPORTED")) {

			Object[] objects = reportDao.getReportedUserList(filter);
			pageResponse.setTotal((long) objects[1]);
			pageResponse.setList(((List<Report>) objects[0]).stream().map(report -> {
				UserDto dto = report.getReportedUser().toDto();
				dto.setStatus(UserStatus.REPORTED);
				return dto;
			}).collect(Collectors.toList()));

		} else {

			com.example.tapp.common.list.helper.Sort sort = com.example.tapp.common.list.helper.Sort.of(Order.DESC,
					User.FIRST_NAME);
			Object[] objects = userDao.getListByUserProfile(filter, null, sort);
			pageResponse.setTotal((long) objects[1]);
			pageResponse.setList(((List<User>) objects[0]).stream().map(User::toDto).collect(Collectors.toList()));
		}
		return pageResponse;
	}

	@Override
	@Transactional
	public void deleteManageUser(UUID id) {
		try {
			User persistUser = userDao.getUserById(id);
			/** START: Clear Connection Data **/
			persistUser.getConnections().stream().forEach(item -> {
				if (item.getMessageDialogId() != null) {
					messageDao.deleteMessage(item.getMessageDialogId(), new Date());
					try {
						dialogDao.deleteDialog(dialogDao.getDialoagById(item.getMessageDialogId()));
					} catch (Exception ex) {
						log.info("ERROR: {}", ex.getMessage());
					}
				}
				connDao.delete(item);
			});
			persistUser.getConnected().stream().forEach(item -> {
				connReqDao.delete(item.getRequestReference());
				connDao.delete(item);
			});

			persistUser.getConnected().clear();
			persistUser.getConnections().clear();
			// END:

			/** START: Clear Notification data **/
			List<UserNotification> notifications = userNotifyDao.list(persistUser.getId());
			if (notifications != null && notifications.size() > 0) {
				userNotifyDao.clear(notifications.get(notifications.size() - 1));
			}
			// END:

			/** START: Clear report data **/
			List<Report> reports = reportDao.getReportDataOfUser(persistUser.getId());
			reports.stream().forEach(item -> reportDao.delete(item));
			// END:

			/** Change User's Status **/
			persistUser.setStatus(UserStatus.DELETED);
			persistUser.setFacebookId(UUID.randomUUID().toString());
			userDao.update(persistUser);
		} catch (Exception ex) {
			log.info("ERROR : {}", ex.getMessage());
		}
	}

	@Override
	public UserDto getUserByFacebookId(String facebookId) {
		User user = userDao.getUserByFacebookId(facebookId);
		return user != null ? user.toDto() : null;
	}

	@Override
	@Transactional
	public UserDto getUserByProfile(UUID userId) throws GeneralException {
		try {
			User user = userDao.getUserById(userId);
			UserDto userDto = user.toDto();
			if (userDto.getIsRead() == false) {
				Query query = entityManager.createQuery("update  User u set u.isRead=:is_read  where u.id= :userId");
				query.setParameter("is_read", true);
				query.setParameter("userId", userId);
				query.executeUpdate();
			}
			UserProfile profile = user.getProfile();
			userDto.setJobTitle(profile.getJobTitle());
			userDto.setAbout(profile.getAbout());
			userDto.setAge(AppUtils.getAge(profile.getDob()));
			userDto.setEducationPlace(profile.getEducationPlace());
			userDto.setWebSite(profile.getWebSite());
			userDto.setAddress(profile.getAddress());
			userDto.setSocialMedia(profile.getSocialMedia().stream().map(sm -> sm.dto()).collect(Collectors.toList()));
			userDto.setGender(profile.getGender());
			userDto.setDob(profile.getDob());
			return userDto;
		} catch (UserNotFoundException e) {
			log.info(e.getMessage());
			throw new GeneralException(e.getMessage());
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public PageResponse<?> getUserReadFalse() {

		Object[] objects = userDao.getListByIsRead();
		PageResponse<UserDto> pageResponse = new PageResponse<>();
		pageResponse.setTotal((long) objects[1]);
		pageResponse.setList(((List<User>) objects[0]).stream().map(User::toDto).collect(Collectors.toList()));
		return pageResponse;
	}

	@Override
	public UserDto updateUserProfileImage(MultipartFile file, UUID userId)
			throws UserNotFoundException, GeneralException, IOException {
		User persist = userDao.getUserById(userId);
		String cProfileImage = persist.getProfile().getProfileImageName();
		if (cProfileImage != null && !cProfileImage.isEmpty() && cProfileImage.contains("/tapp/api/")) {
			String[] temp = cProfileImage.split("/");
			String fileName = temp[temp.length - 1];
			fileHandler.deleteUserProfile(fileName);
		}

		String newfileName = fileHandler.saveUserProfile(file.getBytes(), file.getOriginalFilename());
		persist.getProfile().setProfileImageName(IMAGE_PATH + newfileName);
		return userDao.update(persist).toDto();
	}

	@Override
	public Object[] getUserProfileImage(String fileName) {
		Object[] objects = new Object[2];
		objects[0] = fileHandler.getUserProfileImage(fileName);
		objects[1] = fileName;
		return objects;
	}

}
