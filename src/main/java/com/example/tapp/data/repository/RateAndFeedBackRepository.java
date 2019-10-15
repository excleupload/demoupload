package com.example.tapp.data.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tapp.data.entities.RateAppAndFeedback;

public interface RateAndFeedBackRepository  extends JpaRepository<RateAppAndFeedback,UUID>{

	Optional<RateAppAndFeedback> findByUserId(UUID id);

}
