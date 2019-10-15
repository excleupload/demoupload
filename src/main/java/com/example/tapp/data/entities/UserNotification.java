package com.example.tapp.data.entities;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

@Entity
@Table(name = "user_notification")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class UserNotification extends BaseEntityUUID {

    public static final String USER_ID = "userId";

    @Column(length = 16)
    private UUID userId; // receiver
   
    @Column(length = 16)
    private UUID senderId; // sender
    private String message;
    private boolean sent;
	public UUID getUserId() {
		return userId;
	}
	public void setUserId(UUID userId) {
		this.userId = userId;
	}
	public UUID getSenderId() {
		return senderId;
	}
	public void setSenderId(UUID senderId) {
		this.senderId = senderId;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public boolean isSent() {
		return sent;
	}
	public void setSent(boolean sent) {
		this.sent = sent;
	}
    



}