package com.example.tapp.common.notification;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
public class SmsNotification {

    private static final Logger log = LoggerFactory.getLogger(SmsNotification.class);

    private static final String OPT_MESSAGE = "mobile.varification.message";

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private SmsSender smsSender;

    public void sendOTP(String countryCode, String number, String otp) {
        String phoneNumber = countryCode + number;

        Object[] args = new Object[1];
        args[0] = otp;

        String message = messageSource.getMessage(OPT_MESSAGE, args, "", Locale.US);
        try {
            String sid = smsSender.sendSms(phoneNumber, message);
            if (sid != null) {
                log.info("SMS send success {}", phoneNumber);
            } else {
                log.info("SMS send error {}", phoneNumber);
            }
        } catch (Exception ex) {
            log.info("SMS send error {} : {}", phoneNumber, ex.getMessage());
        }
    }
}
