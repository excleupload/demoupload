package com.example.tapp.data.dao;


import java.util.List;

import com.example.tapp.data.entities.AdminUser;
import com.example.tapp.data.exception.GeneralException;
import com.example.tapp.data.exception.RecordNotFoundException;
import com.example.tapp.data.exception.UserNotFoundException;

public interface AdminUserDao {

	AdminUser save(AdminUser adminUser) throws GeneralException;

	AdminUser update(AdminUser adminUser);

	AdminUser getUserByEmail(String email) throws UserNotFoundException;

	AdminUser getDetailsById(Integer id) throws  com.example.tapp.data.exception.RecordNotFoundException;

	AdminUser getByForgotPasswordToken(String fpToken) throws RecordNotFoundException;

	List<AdminUser> getList();

}
