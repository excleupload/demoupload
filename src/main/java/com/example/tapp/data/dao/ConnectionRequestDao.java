package com.example.tapp.data.dao;

import java.util.HashMap;
import java.util.UUID;

import com.example.tapp.common.discriminator.ConnectionRequestStatus;
import com.example.tapp.common.list.helper.Page;
import com.example.tapp.common.list.helper.Sort;
import com.example.tapp.data.entities.ConnectionRequest;

public interface ConnectionRequestDao {

	ConnectionRequest save(ConnectionRequest entity);

	ConnectionRequest update(ConnectionRequest entity);

	void delete(ConnectionRequest entity);

	ConnectionRequest get(UUID firstUserId, UUID secondUserId);

	ConnectionRequest swapGet(UUID firstUserId, UUID secondUserId);

	ConnectionRequest getById(UUID id) throws com.example.tapp.data.exception.RecordNotFoundException;

	Object[] getListByUserId(UUID userId, ConnectionRequestStatus status, HashMap<String, Object> filter, Page page,
			Sort[] sort);

}
