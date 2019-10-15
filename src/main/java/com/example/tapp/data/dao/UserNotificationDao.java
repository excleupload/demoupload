package com.example.tapp.data.dao;

import java.util.List;
import java.util.UUID;

import com.example.tapp.data.entities.UserNotification;
import com.example.tapp.data.exception.RecordNotFoundException;

public interface UserNotificationDao {

	UserNotification save(UserNotification entity);

	UserNotification update(UserNotification entity);

	List<UserNotification> list(UUID userId);

	UserNotification getByuserIdAndId(UUID userId, UUID lastNotify) throws RecordNotFoundException;

	void clear(UserNotification lastNotification);

}
