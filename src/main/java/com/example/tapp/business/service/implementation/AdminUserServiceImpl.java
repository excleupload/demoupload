package com.example.tapp.business.service.implementation;

import java.util.Date;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import com.example.tapp.business.service.AdminUserService;
import com.example.tapp.common.dto.AdminChangePassword;
import com.example.tapp.common.dto.AdminUserDto;
import com.example.tapp.common.notification.MailNotification;
import com.example.tapp.common.utils.PasswordEncoder;
import com.example.tapp.common.utils.TokenGenerator;
import com.example.tapp.common.utils.YamlPropertySourceFactory;
import com.example.tapp.data.dao.AdminUserDao;
import com.example.tapp.data.entities.AdminUser;
import com.example.tapp.data.exception.GeneralException;
import com.example.tapp.data.exception.RecordNotFoundException;
import com.example.tapp.data.exception.UserNotFoundException;

@Service("adminUserService")
@PropertySource(factory = YamlPropertySourceFactory.class, value = "classpath:utils.yml")
public class AdminUserServiceImpl implements AdminUserService{


	    @Value("${tapp.application.token.validity}")
	    private Integer TOKEN_VALIDITY;

	    @Value("${tapp.application.forgot-password.validity}")
	    private Integer FP_TOKEN_VALIDITY;

	    @Value("${tapp.application.forgot-password.link}")
	    private String FP_LINK;

	    @Autowired
	    private AdminUserDao adminUserDao;

	    @Autowired
	    private PasswordEncoder passwordEncoder;

	    @Autowired
	    private MailNotification mailNotify;

	    @PostConstruct
	    public void init() {
	        AdminUserDto dto = new AdminUserDto();
	        dto.setName("Admin User");
	        dto.setEmail("admin@admin.com");
	        dto.setPassword("admin@123");
	        try {
	            this.save(dto);
	        } catch (Exception ex) {
	            // nothing
	        }
	       
	    }

	    @Override
	    public AdminUserDto save(AdminUserDto adminUserDto) throws GeneralException {
	        AdminUser adminUser = new AdminUser();
	        adminUser.setName(adminUserDto.getName());
	        adminUser.setEmail(adminUserDto.getEmail());
	        adminUser.setPassword(passwordEncoder.encode(adminUserDto.getPassword()));
	        return adminUserDao.save(adminUser).dto();
	    }

	    @Override
	    public AdminUserDto login(String email, String password) throws GeneralException {
	        AdminUser user = null;
	        try {
	            user = adminUserDao.getUserByEmail(email);
	        } catch (UserNotFoundException ex) {
	            throw new GeneralException("You entered wrong email.");
	        }

	        if (!passwordEncoder.isMatched(password, user.getPassword())) {
	            throw new GeneralException("You entered wrong password.");
	        }

	        // Set new token
	        user.setAuthToken(TokenGenerator.createToken(user.getId(), user.getEmail()));
	        user.setLastLogin(System.currentTimeMillis());
	        return adminUserDao.update(user).dto();
	    }

	    @Override
	    public AdminUserDto getDetailById(Integer id) throws RecordNotFoundException {
	        return adminUserDao.getDetailsById(id).dto();
	    }

	    @Override
	    public boolean changePassword(AdminChangePassword changePassword) throws RecordNotFoundException {
	        AdminUser adminUser = adminUserDao.getDetailsById(changePassword.getId());
	        if (!passwordEncoder.isMatched(changePassword.getCurrentPassword(), adminUser.getPassword())) {
	            return false;
	        }
	        adminUser.setPassword(passwordEncoder.encode(changePassword.getNewPassword()));
	        adminUser.setModifiedOn(new Date());
	        adminUserDao.update(adminUser);
	        return true;
	    }

	    @Override
	    public boolean changeName(AdminUserDto adminUserDto) throws RecordNotFoundException {
	        AdminUser adminUser = adminUserDao.getDetailsById(adminUserDto.getId());
	        adminUser.setName(adminUserDto.getName());
	        adminUser.setModifiedOn(new Date());
	        adminUserDao.update(adminUser);
	        return true;
	    }

	    @Override
	    public boolean checkToken(String token) throws GeneralException, RecordNotFoundException {
	        String[] tokenData = TokenGenerator.extract(token);
	        AdminUser adminUser = adminUserDao.getDetailsById(new Integer(tokenData[0]));
	        if (!adminUser.getEmail().equals(tokenData[1])) {
	            throw new GeneralException("Authentication token is not valid.");
	        }
	        long tokenTime = Long.parseLong(tokenData[2]) + TokenGenerator.getValidityTime(TOKEN_VALIDITY);
	        if (tokenTime < System.currentTimeMillis()) {
	            throw new GeneralException("Authentication token is not valid.");
	        }
	        return true;
	    }

	    @Override
	    public boolean forgotPassword(String email) throws GeneralException {
	        AdminUser adminUser;
	        try {
	            adminUser = adminUserDao.getUserByEmail(email);
	        } catch (UserNotFoundException e) {
	            return false;
	        }
	        adminUser.setFpToken(UUID.randomUUID().toString());
	        adminUser.setFpTokenCreatedOn(System.currentTimeMillis());
	        adminUser.setFpTokenUsed(false);
	        adminUserDao.update(adminUser);

	        Object[] args = { "TAPP - FORGOT PASSWORD", adminUser.getName(),
	                FP_LINK.replace("{0}", adminUser.getFpToken()) };
	        mailNotify.sendForgotPassword(adminUser.getEmail(), args);
	        return true;
	    }

	    @Override
	    public boolean verifyForgotPasswordLink(String fpToken) throws GeneralException {
	        AdminUser adminUser;
	        try {
	            adminUser = adminUserDao.getByForgotPasswordToken(fpToken);
	            if (adminUser.isFpTokenUsed())
	                throw new GeneralException("Sorry! This link already used.");

	            long time = adminUser.getFpTokenCreatedOn() + TokenGenerator.getValidityTime(FP_TOKEN_VALIDITY);
	            if (time < System.currentTimeMillis())
	                throw new GeneralException("Sorry! This link has been expired.");

	        } catch (RecordNotFoundException e) {
	            throw new GeneralException("Sorry! This link is invalid.");
	        }
	        return true;
	    }

	    @Override
	    public boolean resetPassword(String fpToken, String password) throws GeneralException {
	        AdminUser adminUser;
	        try {
	            adminUser = adminUserDao.getByForgotPasswordToken(fpToken);
	            if (adminUser.isFpTokenUsed())
	                throw new GeneralException("You have already reset your password.");
	        } catch (RecordNotFoundException e) {
	            throw new GeneralException("Oops! Something went wrong. Please try again.");
	        }
	        adminUser.setPassword(passwordEncoder.encode(password));
	        adminUser.setModifiedOn(new Date());
	        adminUser.setFpTokenUsed(true);
	        adminUserDao.update(adminUser);
	        return false;
	    }
	   
	   
	}


