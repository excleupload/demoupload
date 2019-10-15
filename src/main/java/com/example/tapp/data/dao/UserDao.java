package com.example.tapp.data.dao;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import com.example.tapp.common.discriminator.DeviceType;
import com.example.tapp.common.list.helper.Sort;
import com.example.tapp.data.entities.User;
import com.example.tapp.data.exception.UserNotFoundException;

public interface UserDao {

	Object[] getListByIsRead();

	User save(User user);

	User update(User user);

	boolean delete(UUID id) throws UserNotFoundException;

	User getUserByEmail(String email);

	List<User> getListforEmail();

	User getUserById(UUID id) throws UserNotFoundException;

	void addUserDevice(UUID id, DeviceType type, String deviceToken) throws UserNotFoundException;

	void removeUserDevice(UUID id) throws UserNotFoundException;

	void removeUserToken(UUID id) throws UserNotFoundException;

	User getUserByFacebookId(String facebookId);

	List<User> getManageUser();

	Object[] getListByUser(Sort... sorts);

	Object[] getListByUserProfile(HashMap<String, Object> filter, Sort... sorts);

	void deleteManageUser(UUID id);

	Long getUsersCount();

	Long getMaleUserCount();

	Long getFemaleUserCount();

	List<Object[]> getUserCountByYear(Integer year);

	List<User> getUserNotification();

	User getSystemUser();

	List<Object[]> findByList();

}
