package com.example.tapp.data.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tapp.common.discriminator.Gender;
import com.example.tapp.common.discriminator.UserRole;
import com.example.tapp.common.discriminator.UserStatus;
import com.example.tapp.data.entities.User;

public interface UserRepository extends JpaRepository<User,UUID >{

	Optional<User> findByEmail(String email);

	List<User> findByRoleAndStatusIn(UserRole roleUser, List<UserStatus> asList, Sort by);

	Optional<User> findByFacebookId(String facebookId);

	Long countByRoleAndStatus(UserRole roleUser, UserStatus active);

	Long countByProfileGenderAndStatus(Gender male, UserStatus active);

	Optional<User> findByRole(UserRole roleSystem);

}
