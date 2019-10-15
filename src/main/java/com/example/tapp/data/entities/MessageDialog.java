package com.example.tapp.data.entities;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;



@Entity
@Table(name = "message_dialog")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class MessageDialog extends BaseEntityUUID {

    private String lastMessage;

    @Column(length = 16)
    private UUID lastMessageUserId;

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.EAGER)
    @JoinTable(name = "message_dialog_user", joinColumns = { @JoinColumn(name = "dialog_id") }, inverseJoinColumns = {
            @JoinColumn(name = "user_id") })
    private Set<User> occupants = new HashSet<>();

	public String getLastMessage() {
		return lastMessage;
	}

	public void setLastMessage(String lastMessage) {
		this.lastMessage = lastMessage;
	}

	public UUID getLastMessageUserId() {
		return lastMessageUserId;
	}

	public void setLastMessageUserId(UUID lastMessageUserId) {
		this.lastMessageUserId = lastMessageUserId;
	}

	public Set<User> getOccupants() {
		return occupants;
	}

	public void setOccupants(Set<User> occupants) {
		this.occupants = occupants;
	}

 

}
