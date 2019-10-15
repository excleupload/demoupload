package com.example.tapp.data.entities;



import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.example.tapp.common.discriminator.ConnectionRequestStatus;

@Entity
@Table(name = "user_connection")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class UserConnection extends BaseEntityUUID {

    public static final String OWNER_USER = "ownerUser";
    public static final String CONNECTED_USER = "connectedUser";
    public static final String STATUS = "status";
    public static final String DIALOG_ID = "messageDialogId";
    public static final String REQUEST_REFERENCE = "requestReference";

    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_user")
    private User ownerUser;

    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.EAGER)
    @JoinColumn(name = "connected_user")
    private User connectedUser;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ConnectionRequestStatus status;

    @OneToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name = "request_id")
    private ConnectionRequest requestReference;

    @Column(length = 16)
    private UUID messageDialogId;

    private boolean profileVisited;

	public User getOwnerUser() {
		return ownerUser;
	}

	public void setOwnerUser(User ownerUser) {
		this.ownerUser = ownerUser;
	}

	public User getConnectedUser() {
		return connectedUser;
	}

	public void setConnectedUser(User connectedUser) {
		this.connectedUser = connectedUser;
	}

	public ConnectionRequestStatus getStatus() {
		return status;
	}

	public void setStatus(ConnectionRequestStatus status) {
		this.status = status;
	}

	public ConnectionRequest getRequestReference() {
		return requestReference;
	}

	public void setRequestReference(ConnectionRequest requestReference) {
		this.requestReference = requestReference;
	}

	public UUID getMessageDialogId() {
		return messageDialogId;
	}

	public void setMessageDialogId(UUID messageDialogId) {
		this.messageDialogId = messageDialogId;
	}

	public boolean isProfileVisited() {
		return profileVisited;
	}

	public void setProfileVisited(boolean profileVisited) {
		this.profileVisited = profileVisited;
	}


}