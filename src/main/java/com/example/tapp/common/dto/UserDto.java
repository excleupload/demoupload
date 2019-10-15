package com.example.tapp.common.dto;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import com.example.tapp.common.discriminator.Gender;
import com.example.tapp.common.discriminator.UserStatus;
import com.example.tapp.data.entities.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_NULL)
public class UserDto implements Serializable {

    private static final long serialVersionUID = 6989912648118479042L;

    public static final String ID = "id";
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";
    public static final String EMAIL = "email";
    public static final String COUNTRY_CODE = "countryCode";
    public static final String MOBILE = "mobile";
    public static final String FACEBOOK_ID = "facebookId";
    public static final String OTP = "otp";
    public static final String AGE = "age";
    public static final String JOB_TITLE = "jobTitle";
    public static final String EDUCATION_PLACE = "educationPlace";
    public static final String ABOUT = "about";
    public static final String ADRESS = "address";
    public static final String WEBSITE = "webSite";
    public static final String SOCIAL_MEDIA = "socialMedia";
    public static final String GENDER = "gender";
    public static final String STATUS = "status";
    public static final String DOB = "dob";
    public static final String ISREAD = "isRead";

    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String countryCode;
    private String mobile;
    private String facebookId;
    private String profileImage;
    private Integer age;
    private Boolean mobileVerified;
    private String authToken;
    private String otp;
    private String jobTitle;
    private String educationPlace;
    private String about;
    private String webSite;
    private String address;
    private List<SocialMediaDto> socialMedia;
    private Gender gender;
    private UserStatus status;
    private Long dob;

    private Boolean isRead;
    public UserDto() {

    }

    public UserDto(User user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.countryCode = user.getCountryCode();
        this.mobile = user.getMobile();
        this.facebookId = user.getFacebookId();
        this.mobileVerified = user.isMobileVerified();
        this.profileImage = user.getProfile().getProfileImageName();
        this.authToken = user.getAuthToken();
        this.gender = user.getProfile().getGender();
        this.status = user.getStatus();
        this.isRead = user.getIsRead();

    }

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getFacebookId() {
		return facebookId;
	}

	public void setFacebookId(String facebookId) {
		this.facebookId = facebookId;
	}

	public String getProfileImage() {
		return profileImage;
	}

	public void setProfileImage(String profileImage) {
		this.profileImage = profileImage;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Boolean getMobileVerified() {
		return mobileVerified;
	}

	public void setMobileVerified(Boolean mobileVerified) {
		this.mobileVerified = mobileVerified;
	}

	public String getAuthToken() {
		return authToken;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public String getEducationPlace() {
		return educationPlace;
	}

	public void setEducationPlace(String educationPlace) {
		this.educationPlace = educationPlace;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public String getWebSite() {
		return webSite;
	}

	public void setWebSite(String webSite) {
		this.webSite = webSite;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public List<SocialMediaDto> getSocialMedia() {
		return socialMedia;
	}

	public void setSocialMedia(List<SocialMediaDto> socialMedia) {
		this.socialMedia = socialMedia;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public UserStatus getStatus() {
		return status;
	}

	public void setStatus(UserStatus status) {
		this.status = status;
	}

	public Long getDob() {
		return dob;
	}

	public void setDob(Long dob) {
		this.dob = dob;
	}

	public Boolean getIsRead() {
		return isRead;
	}

	public void setIsRead(Boolean isRead) {
		this.isRead = isRead;
	}

    
}

