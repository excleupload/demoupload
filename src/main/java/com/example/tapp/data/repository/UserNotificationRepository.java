package com.example.tapp.data.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tapp.data.entities.UserNotification;

public interface UserNotificationRepository extends JpaRepository<UserNotification,UUID> {

	Optional<UserNotification> findByUserIdAndId(UUID userId, UUID lastNotify);

	List<UserNotification> findByUserId(UUID userId, Sort by);

}
