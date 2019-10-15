package com.example.tapp.data.entities;

import java.awt.TrayIcon.MessageType;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.example.tapp.common.discriminator.MessageStatus;

@Entity
@Table(name = "message")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Message extends BaseEntityUUID {

    public static final String DIALOG = "dialog";
    public static final String SENDER_ID = "senderId";
    public static final String RECEIVER_ID = "receiverId";
    public static final String STATUS = "status";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dialog_id")
    private MessageDialog dialog;

    @Column(columnDefinition = "TEXT")
    private String message;

    @Column(length = 16)
    private UUID senderId;

    @Column(length = 16)
    private UUID receiverId;

    @Enumerated(EnumType.STRING)
    private MessageStatus status;

    public MessageDialog getDialog() {
		return dialog;
	}

	public void setDialog(MessageDialog dialog) {
		this.dialog = dialog;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public UUID getSenderId() {
		return senderId;
	}

	public void setSenderId(UUID senderId) {
		this.senderId = senderId;
	}

	public UUID getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(UUID receiverId) {
		this.receiverId = receiverId;
	}

	public MessageStatus getStatus() {
		return status;
	}

	public void setStatus(MessageStatus status) {
		this.status = status;
	}

	public MessageType getType() {
		return type;
	}

	public void setType(MessageType type) {
		this.type = type;
	}

	@Enumerated(EnumType.STRING)
    private MessageType type;

}
