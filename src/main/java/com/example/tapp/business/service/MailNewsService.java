package com.example.tapp.business.service;

import com.example.tapp.common.dto.NotificationDto;

public interface MailNewsService {

	boolean sendmail(NotificationDto newsMail);

	boolean sendPush(NotificationDto pushNotification);

}
