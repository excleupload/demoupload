package com.example.tapp.presentation.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.tapp.business.service.UserNotifyService;
import com.example.tapp.common.response.ResponseUtils;
import static com.example.tapp.common.utils.Route.notificationList;

import static com.example.tapp.common.utils.Route.notificationClear;
@RestController
public class NotificationController {

    @Autowired
    private UserNotifyService notifyService;

    @GetMapping(notificationList)
    public ResponseEntity<?> list(@RequestParam("userId") UUID userId) {
        return ResponseUtils.success.apply(notifyService.list(userId));
    }

    @PostMapping(notificationClear)
    public ResponseEntity<?> clear(@RequestParam("userId") UUID userId, @RequestParam("lastNotify") UUID notifyId) {
        ///
        notifyService.clear(userId, notifyId);
        return ResponseUtils.success.apply("Notification clear.");
    }

}