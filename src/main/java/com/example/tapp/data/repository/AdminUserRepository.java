package com.example.tapp.data.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tapp.data.entities.AdminUser;

public interface AdminUserRepository extends JpaRepository<AdminUser, UUID> {

	Optional<AdminUser> findByEmail(String email);

	Optional<AdminUser> findById(Integer id);

	Optional<AdminUser> findByFpToken(String fpToken);

}
