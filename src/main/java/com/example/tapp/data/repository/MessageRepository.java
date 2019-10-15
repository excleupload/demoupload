package com.example.tapp.data.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tapp.common.discriminator.MessageStatus;
import com.example.tapp.data.entities.Message;

public interface MessageRepository extends JpaRepository<Message,UUID> {

	List<Message> findByReceiverIdAndStatus(UUID receiverId, MessageStatus pending, org.springframework.data.domain.Sort sort);

}
