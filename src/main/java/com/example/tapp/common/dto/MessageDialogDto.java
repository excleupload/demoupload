package com.example.tapp.common.dto;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import com.example.tapp.common.discriminator.ConnectionRequestStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_NULL)
public class MessageDialogDto implements Serializable {

    private static final long serialVersionUID = 2407332454626702785L;

    public static final String DIALOG_ID = "dialogId";

    private UUID dialogId;
    private List<UUID> occupantIds;
    private List<UserDto> occupants;
    private Long createdOn;
    private String lastMessage;
    private UserDto occupant;
    private Long lastMessageTime;
    
    public UUID getDialogId() {
		return dialogId;
	}
	public void setDialogId(UUID dialogId) {
		this.dialogId = dialogId;
	}
	public List<UUID> getOccupantIds() {
		return occupantIds;
	}
	public void setOccupantIds(List<UUID> occupantIds) {
		this.occupantIds = occupantIds;
	}
	public List<UserDto> getOccupants() {
		return occupants;
	}
	public void setOccupants(List<UserDto> occupants) {
		this.occupants = occupants;
	}
	public Long getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(Long createdOn) {
		this.createdOn = createdOn;
	}
	public String getLastMessage() {
		return lastMessage;
	}
	public void setLastMessage(String lastMessage) {
		this.lastMessage = lastMessage;
	}
	public UserDto getOccupant() {
		return occupant;
	}
	public void setOccupant(UserDto occupant) {
		this.occupant = occupant;
	}
	public Long getLastMessageTime() {
		return lastMessageTime;
	}
	public void setLastMessageTime(Long lastMessageTime) {
		this.lastMessageTime = lastMessageTime;
	}
	public boolean isOnline() {
		return online;
	}
	public void setOnline(boolean online) {
		this.online = online;
	}
	public ConnectionRequestStatus getStatus() {
		return status;
	}
	public void setStatus(ConnectionRequestStatus status) {
		this.status = status;
	}
	private boolean online;
    private ConnectionRequestStatus status;

   
}