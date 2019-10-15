package com.example.tapp.common.dto;


import java.io.Serializable;
import java.util.UUID;

import com.example.tapp.common.discriminator.MessageStatus;
import com.example.tapp.common.discriminator.MessageType;
import com.example.tapp.data.entities.Message;

public class MessageDto implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String DIALOG_ID = "dialogId";
    public static final String TYPE = "type";
    public static final String MESSAGE = "message";

    private UUID id;
    private UUID dialogId;
    private MessageType type;
    private String message;
    private UUID sender;
    private UUID receiver;
    private Long time;
    private MessageStatus status;
    
    
    public MessageDto() {
		super();
		// TODO Auto-generated constructor stub
	}
    public MessageDto(Message message)
    {
 	   
    }
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	public UUID getDialogId() {
		return dialogId;
	}
	public void setDialogId(UUID dialogId) {
		this.dialogId = dialogId;
	}
	public MessageType getType() {
		return type;
	}
	public void setType(MessageType type) {
		this.type = type;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public UUID getSender() {
		return sender;
	}
	public void setSender(UUID sender) {
		this.sender = sender;
	}
	public UUID getReceiver() {
		return receiver;
	}
	public void setReceiver(UUID receiver) {
		this.receiver = receiver;
	}
	public Long getTime() {
		return time;
	}
	public void setTime(Long time) {
		this.time = time;
	}
	public MessageStatus getStatus() {
		return status;
	}
	public void setStatus(MessageStatus status) {
		this.status = status;
	}
	public String getSenderProfileImage() {
		return senderProfileImage;
	}
	public void setSenderProfileImage(String senderProfileImage) {
		this.senderProfileImage = senderProfileImage;
	}
	public String getReceiverProfileImage() {
		return receiverProfileImage;
	}
	public void setReceiverProfileImage(String receiverProfileImage) {
		this.receiverProfileImage = receiverProfileImage;
	}
	private String senderProfileImage;
    private String receiverProfileImage;


}
