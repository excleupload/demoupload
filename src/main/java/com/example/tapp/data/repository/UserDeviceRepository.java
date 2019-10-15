package com.example.tapp.data.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tapp.data.entities.UserDevice;

public interface UserDeviceRepository extends JpaRepository<UserDevice, UUID> {

}
