package com.example.tapp.data.dao.implementation;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Repository;

import com.example.tapp.data.dao.AdminUserDao;
import com.example.tapp.data.entities.AdminUser;
import com.example.tapp.data.entities.StaticPages;
import com.example.tapp.data.exception.GeneralException;
import com.example.tapp.data.exception.RecordNotFoundException;
import com.example.tapp.data.exception.UserNotFoundException;
import com.example.tapp.data.repository.AdminUserRepository;

@Repository
@Transactional
public class AdminUserDaoImpl implements AdminUserDao {

    @Autowired
    AdminUserRepository adminUserRepo;

    @Override
    public AdminUser save(AdminUser adminUser) throws GeneralException {
        if (adminUserRepo.findByEmail(adminUser.getEmail()).isPresent()) {
            throw new GeneralException("Admin user already exist.");
        }
        adminUser.setCreatedOn(new Date());
        adminUser.setModifiedOn(new Date());
        adminUser.setDeleted(false);
        return adminUserRepo.save(adminUser);
    }

    @Override
    public AdminUser update(AdminUser adminUser) {
        return adminUserRepo.save(adminUser);
    }

    @Override
    public AdminUser getUserByEmail(String email) throws UserNotFoundException {
        return adminUserRepo.findByEmail(email).orElseThrow(UserNotFoundException::new);
    }

    @Override
    public AdminUser getDetailsById(Integer id) throws RecordNotFoundException {
        return adminUserRepo.findById(id).orElseThrow(RecordNotFoundException::new);
    }

    @Override
    public AdminUser getByForgotPasswordToken(String fpToken) throws RecordNotFoundException {
        return adminUserRepo.findByFpToken(fpToken).orElseThrow(RecordNotFoundException::new);
    }

    @Override
    public List<AdminUser> getList() {
        return adminUserRepo.findAll(Sort.by(Direction.DESC, StaticPages.CREATED_ON));
    }
}
