package com.example.tapp.presentation.controllers.admin;

import static com.example.tapp.common.response.ResponseUtils.success;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.tapp.business.service.ReportService;
import com.example.tapp.business.service.UserService;
import com.example.tapp.data.exception.GeneralException;

@RestController
@RequestMapping("/admin")
public class AdminNotificationController {
    @Autowired
    AdminNotificationService adminNotificationService;

    @Autowired
    UserService userService;

    @Autowired
    ReportService reportService;

    @GetMapping("/notification/notificationlisttest")
    public ResponseEntity<?> getUserList() {
        return success.apply(adminNotificationService.getUserCountList());
    }

    @GetMapping("/notification/notificationlist")
    public ResponseEntity<?> notification() {
        return success.apply(adminNotificationService.getNotification());
    }

    @GetMapping("/notification/allnotificationlist")
    public ResponseEntity<?> notificationUserAllList() {
        return success.apply(userService.getUserReadFalse());
    }

    @GetMapping("/notification/adminUserWisList/{userId}")
    public ResponseEntity<?> notificationByUserId(@PathVariable("userId") UUID userId) throws GeneralException {
        return success.apply(userService.getUserByProfile(userId));
    }

    @GetMapping("/notification/adminReplyUserSupport")
    public ResponseEntity<?> notificationforUserSupport(HttpServletRequest request) {
        return success.apply(reportService.getUserReply());
    }

    @GetMapping("/notification/adminUserSupportCount")
    public ResponseEntity<?> userSupportCount() {
        return success.apply(reportService.getUserSupportCount());
    }

    @GetMapping("/notification/replyByUserid/{userId}")
    public ResponseEntity<?> replyadminByUserID(@PathVariable("userId") UUID userId) throws GeneralException {
        return success.apply(reportService.getReportListByNotification(userId));
    }
}