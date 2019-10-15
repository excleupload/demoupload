package com.example.tapp.data.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tapp.data.entities.ConnectionRequest;

public interface ConnectionRequestRepository extends JpaRepository<ConnectionRequest, UUID> {

	

Optional<ConnectionRequest> findsByFirstUserIdAndSecondUserId(UUID firstUserId, UUID secondUserId);

Optional<ConnectionRequest> findByFirstUserIdAndSecondUserId(UUID firstUserId, UUID secondUserId);

}
