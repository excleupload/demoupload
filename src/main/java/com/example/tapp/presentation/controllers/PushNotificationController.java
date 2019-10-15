package com.example.tapp.presentation.controllers;

import org.springframework.http.RequestEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.services.sns.AmazonSNS;

@RestController
@RequestMapping("/pushnotification")
public class PushNotificationController {


    AmazonSNS amazonSNS;
    RequestEntity<?> sendPushNotifiaction() {
        return null;

    }


}