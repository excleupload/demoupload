package com.example.tapp.presentation.controllers;

import static com.example.tapp.common.response.ResponseUtils.error;
import static com.example.tapp.common.response.ResponseUtils.errorList;
import static com.example.tapp.common.response.ResponseUtils.sendImage;
import static com.example.tapp.common.response.ResponseUtils.success;
import static com.example.tapp.common.utils.Route.addDevice;
import static com.example.tapp.common.utils.Route.facebook;
import static com.example.tapp.common.utils.Route.logout;
import static com.example.tapp.common.utils.Route.profile;
import static com.example.tapp.common.utils.Route.register;
import static com.example.tapp.common.utils.Route.resendOtp;
import static com.example.tapp.common.utils.Route.uploadProfile;
import static com.example.tapp.common.utils.Route.userDetails;
import static com.example.tapp.common.utils.Route.userProfileImage;
import static com.example.tapp.common.utils.Route.verifyMobile;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.tapp.business.service.ConnectionRequestService;
import com.example.tapp.business.service.UserService;
import com.example.tapp.common.discriminator.UserStatus;
import com.example.tapp.common.dto.UserDeviceDto;
import com.example.tapp.common.dto.UserDto;
import com.example.tapp.data.exception.GeneralException;
import com.example.tapp.data.exception.UserNotFoundException;
import com.twilio.rest.preview.deployedDevices.fleet.Device;




@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ConnectionRequestService connReqService;

    @PostMapping(register)
    public ResponseEntity<?> register(@RequestBody UserDto userDto, BindingResult result) {
        //
        new Register().validate(userDto, result);
        if (result.hasErrors()) {
            return errorList.apply(result.getFieldErrors());
        }
        UserDto dto = userService.save(userDto);

        // Create Connection
        new Thread(() -> {
            connReqService.createRequestForSystemUser(dto.getId());
        }).start();
        return success.apply(dto);
    }

    @PostMapping(facebook)
    public ResponseEntity<?> facebook(@RequestParam("facebookId") String facebookId) {
        //
        UserDto dto = userService.getUserByFacebookId(facebookId);
        if (dto != null) {
            if (dto.getStatus().equals(UserStatus.BLOCKED)) {
                return errorWithStatus.apply("Your account has been deactivated.", HttpStatus.UNPROCESSABLE_ENTITY);
            }
            return success.apply(dto);
        }

        return error.apply("User not found.");
    }
    
    @PostMapping(verifyMobile)
    public ResponseEntity<?> verifyMobile(@RequestBody UserDto userDto, BindingResult result) {
        //
        new VerifyMobile().validate(userDto, result);
        if (result.hasErrors())
            return errorList.apply(result.getFieldErrors());

        if (userService.verifyOtp(userDto)) {
            return success.apply("Valid OTP.");
        }
        return error.apply("Invalid OTP.");
    }
    @PostMapping(resendOtp)
    public ResponseEntity<?> resendOtp(@RequestBody UserDto dto) throws GeneralException {
        //
        userService.resendOtp(dto.getId());
        return success.apply("OTP has been successfully sent.");
    }

    @RequestMapping(userDetails)
    public ResponseEntity<?> details(@RequestParam("userId") UUID userId) throws GeneralException {
        //
        return success.apply(userService.details(userId));
    }
    @PostMapping(addDevice)
    public ResponseEntity<?> addDevice(@RequestBody UserDeviceDto dto, BindingResult result) throws GeneralException {
        //
        new Device().validate(dto, result);
        if (result.hasErrors())
            return errorList.apply(result.getFieldErrors());

        userService.addDevice(dto);
        return success.apply("Device successfully added.");
    }

    @GetMapping(logout)
    public ResponseEntity<?> logout(@RequestParam("userId") UUID userId) throws GeneralException {
        //
        userService.logout(userId);
        return success.apply("User successfully logout.");
    }

    @PostMapping(profile)
    public ResponseEntity<?> profile(@RequestBody HashMap<String, Object> params) throws GeneralException {
        //
        userService.profile(params);
        return success.apply("User profile successfully updated.");
    }

    @GetMapping(profile)
    public ResponseEntity<?> profile(@RequestParam("userId") UUID userId,
            @RequestParam(name = "connId", required = false) UUID connectionId,
            @RequestParam(name = "visited", required = false) Boolean visited) throws GeneralException {

        //
        return success.apply(userService.getProfile(userId, connectionId, visited));
    }

    @PostMapping(uploadProfile)
    public ResponseEntity<?> uploadProfile(@RequestParam(name = "file", required = true) MultipartFile file,
            @RequestParam(name = "userId", required = true) UUID userId)
            throws GeneralException, UserNotFoundException, IOException {
        //
        return success.apply(userService.updateUserProfileImage(file, userId));
    }

    @GetMapping(userProfileImage)
    public void getProfileImage(@PathVariable("fileName") String fileName, HttpServletResponse response)
            throws IOException {
        //
        sendImage(userService.getUserProfileImage(fileName), response);
    }

}