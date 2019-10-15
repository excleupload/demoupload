package com.example.tapp.data.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tapp.data.entities.MessageClear;

public interface MessageClearRepository extends JpaRepository<MessageClear, UUID> {

	Optional<MessageClear> findByDialogIdAndOccupantId(UUID dialogId, UUID occupantId);

}
