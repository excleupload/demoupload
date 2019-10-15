package com.example.tapp.data.dao;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import com.example.tapp.common.discriminator.ConnectionRequestStatus;
import com.example.tapp.common.list.helper.Page;
import com.example.tapp.common.list.helper.Sort;
import com.example.tapp.data.entities.UserConnection;
import com.example.tapp.data.exception.RecordNotFoundException;

public interface UserConnectionDao {

	UserConnection save(UserConnection entity);

	UserConnection update(UserConnection entity);

	Object[] getListByOwnerId(UUID ownerId, ConnectionRequestStatus status, HashMap<String, Object> filter, Page page,
			Sort... sorts);

	List<Object[]> getListofUserName(UUID ownerId, ConnectionRequestStatus status, Sort... sorts);

	List<Object> getConnectedUserIdByOwner(UUID ownerId, ConnectionRequestStatus status);

	UserConnection getByOwnerAndConnectedUser(UUID ownerId, UUID connectedUserId) throws RecordNotFoundException;

	List<Object> getConnectedUsersDialogId(UUID ownerId, ConnectionRequestStatus status);

	List<Object> getConnectedUsersDialogId(UUID ownerId, ConnectionRequestStatus status, HashMap<String, Object> filter,
			Sort... sorts);

	UserConnection getById(UUID id) throws RecordNotFoundException;

	void delete(UserConnection entity);

	Long getTotalConnection();

}
