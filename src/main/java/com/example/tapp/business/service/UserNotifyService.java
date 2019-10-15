package com.example.tapp.business.service;

import java.util.List;
import java.util.UUID;

import com.example.tapp.common.dto.UserNotificationDto;

public interface UserNotifyService {

	void requestAccepted(UUID senderId, UUID receiverId);

	List<UserNotificationDto> list(UUID userId);

	void clear(UUID userId, UUID lastNotify);

}
