package com.example.tapp.business.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com.example.tapp.common.dto.UserDeviceDto;
import com.example.tapp.common.dto.UserDto;
import com.example.tapp.common.list.helper.PageResponse;
import com.example.tapp.data.exception.GeneralException;
import com.example.tapp.data.exception.RecordNotFoundException;
import com.example.tapp.data.exception.UserNotFoundException;

public interface UserService {

	UserDto save(UserDto userDto);

	boolean verifyOtp(UserDto dto);

	void resendOtp(UUID id) throws GeneralException;

	boolean checkToken(String token) throws GeneralException, RecordNotFoundException;

	UserDto details(UUID id) throws GeneralException;

	void addDevice(UserDeviceDto dto) throws GeneralException;

	void logout(UUID userId) throws GeneralException;

	void profile(HashMap<String, Object> params) throws GeneralException;

	UserDto getProfile(UUID userId, UUID connectionId, Boolean visited) throws GeneralException;

	UserDto getUserDetailsByToken(String token) throws UserNotFoundException;

	List<UserDto> getListforEmail();

	PageResponse<?> getUserProfileList(HashMap<String, Object> options);

	void deleteManageUser(UUID id);

	UserDto getUserByFacebookId(String facebookId);

	UserDto getUserByProfile(UUID userId) throws GeneralException;

	PageResponse<?> getUserReadFalse();

	UserDto updateUserProfileImage(MultipartFile file, UUID userId)
			throws UserNotFoundException, GeneralException, IOException;

	Object[] getUserProfileImage(String fileName);

}
