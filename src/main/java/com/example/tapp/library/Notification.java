package com.example.tapp.library;

import com.example.tapp.common.dto.NotificationDto;
import com.example.tapp.common.dto.Push;

public interface Notification {

    public String android(String token, Push push);

    public String apple(String token, Push push);

    String adminIOS(String token, NotificationDto push);

}