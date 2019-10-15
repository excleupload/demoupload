package com.example.tapp.business.service;

import java.util.UUID;

import com.example.tapp.common.dto.MessageDto;
import com.example.tapp.data.entities.ConnectionRequest;
import com.example.tapp.data.entities.UserConnection;

public interface INotification {

	void onMessage(Object[] notifyData, MessageDto message);

	void onConnectionAccept(UserConnection connection);

	void onConnectionReject(ConnectionRequest request, UUID cUserId);

}
