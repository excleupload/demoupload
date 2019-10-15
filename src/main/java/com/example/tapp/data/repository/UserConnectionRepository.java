package com.example.tapp.data.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tapp.common.discriminator.UserRole;
import com.example.tapp.data.entities.UserConnection;

public interface UserConnectionRepository extends JpaRepository<UserConnection, UUID>
{

	Optional<UserConnection> findByOwnerUserIdAndConnectedUserId(UUID ownerId, UUID connectedUserId);

	Long countByOwnerUserRoleAndConnectedUserRole(UserRole roleUser, UserRole roleUser2);

}
