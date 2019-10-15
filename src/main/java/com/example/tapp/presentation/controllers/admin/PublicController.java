package com.example.tapp.presentation.controllers.admin;

import static com.example.tapp.common.response.ResponseUtils.error;
import static com.example.tapp.common.response.ResponseUtils.success;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.tapp.business.service.AdminUserService;
import com.example.tapp.data.exception.GeneralException;


@RestController
public class PublicController {

    @Autowired
    private AdminUserService adminUserService;

    @RequestMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) throws GeneralException {
        //
        if (adminUserService.forgotPassword(email)) {
            return success.apply("Reset password link has been sent on your E-Mail.");
        }
        return error.apply("Sorry! we couldn't find an account with that E-Mail.");
    }

    @RequestMapping("/forgot-password/verify")
    public ResponseEntity<?> verifyFpToken(@RequestParam String fpToken) throws GeneralException {
        //
        adminUserService.verifyForgotPasswordLink(fpToken);
        return success.apply("Link is valid");
    }

    @RequestMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam("fpToken") String fpToken,
            @RequestParam("password") String password) throws GeneralException {
        //
        adminUserService.resetPassword(fpToken, password);
        return success.apply("Password reset succeeded.");
    }
}