package com.example.tapp.business.service;

import com.example.tapp.common.dto.AdminChangePassword;
import com.example.tapp.common.dto.AdminUserDto;
import com.example.tapp.data.exception.GeneralException;
import com.example.tapp.data.exception.RecordNotFoundException;

public interface AdminUserService {

	AdminUserDto save(AdminUserDto adminUserDto) throws GeneralException;

	AdminUserDto login(String email, String password) throws GeneralException;

	AdminUserDto getDetailById(Integer id) throws RecordNotFoundException;

	boolean changePassword(AdminChangePassword changePassword) throws RecordNotFoundException;

	boolean changeName(AdminUserDto adminUserDto) throws RecordNotFoundException;

	boolean checkToken(String token) throws GeneralException, RecordNotFoundException;

	boolean forgotPassword(String email) throws GeneralException;

	boolean verifyForgotPasswordLink(String fpToken) throws GeneralException;

	boolean resetPassword(String fpToken, String password) throws GeneralException;

}
