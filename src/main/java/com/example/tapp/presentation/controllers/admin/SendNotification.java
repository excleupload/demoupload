package com.example.tapp.presentation.controllers.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.tapp.business.service.MailNewsService;
import com.example.tapp.business.service.UserService;
import com.example.tapp.common.dto.NotificationDto;
import com.example.tapp.data.exception.RecordNotFoundException;
import com.example.tapp.data.exception.UserNotFoundException;
import static com.example.tapp.common.response.ResponseUtils.success;

@RestController
@RequestMapping("/admin")
public class SendNotification {

    @Autowired
    private UserService userService;

    @Autowired
    private MailNewsService mailNewsService;

    @GetMapping("/sendNotificationService/listOfEmail")
    public ResponseEntity<?> getEmailList() throws RecordNotFoundException, UserNotFoundException {
        return success.apply(userService.getListforEmail());
    }

    @PostMapping(value = "/sendNotificationService/sendmail")
    public ResponseEntity<?> sendmail(@RequestBody NotificationDto newsMail) {
        try {
            return success.apply(mailNewsService.sendmail(newsMail));
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Internal server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping(value = "/sendNotificationService/pushnotification")
    public ResponseEntity<?> sendPushNotification(@RequestBody NotificationDto pushNotification) {
        try {
            return success.apply(mailNewsService.sendPush(pushNotification));
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Internal server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}