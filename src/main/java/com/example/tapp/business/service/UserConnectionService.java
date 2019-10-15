package com.example.tapp.business.service;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import com.example.tapp.common.discriminator.ConnectionRequestStatus;
import com.example.tapp.common.dto.UserDto;
import com.example.tapp.common.list.helper.PageResponse;
import com.example.tapp.data.exception.RecordNotFoundException;

public interface UserConnectionService {

	PageResponse<?> getListByUserId(UUID userId, HashMap<String, Object> options);

	List<UserDto> getListUserNameByUserId(UUID userId);

	List<UUID> getConnectedUserIds(UUID userId);

	void removeConnection(UUID connectionId) throws RecordNotFoundException;

	void changeStatus(UUID connId, ConnectionRequestStatus status) throws RecordNotFoundException;

	Object[] getConnByOwnerUserAndConnectedUser(UUID oId, UUID cId) throws RecordNotFoundException;

}
