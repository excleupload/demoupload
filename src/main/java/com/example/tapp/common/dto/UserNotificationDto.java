package com.example.tapp.common.dto;


import java.io.Serializable;
import java.util.UUID;

public class UserNotificationDto implements Serializable {

    private static final long serialVersionUID = 1L;

    public UUID id;
    private UserDto sender;
    private String message;
    private Long time;
    
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	public UserDto getSender() {
		return sender;
	}
	public void setSender(UserDto sender) {
		this.sender = sender;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Long getTime() {
		return time;
	}
	public void setTime(Long time) {
		this.time = time;
	}


}