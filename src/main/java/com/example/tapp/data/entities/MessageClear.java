package com.example.tapp.data.entities;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

@Entity
@Table(name = "message_clear")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class MessageClear extends BaseEntityUUID {

    @Column(length = 16)
    public UUID dialogId;

    @Column(length = 16)
    private UUID occupantId;
    public UUID getDialogId() {
		return dialogId;
	}
	public void setDialogId(UUID dialogId) {
		this.dialogId = dialogId;
	}
	public UUID getOccupantId() {
		return occupantId;
	}
	public void setOccupantId(UUID occupantId) {
		this.occupantId = occupantId;
	}
	public Date getClearOn() {
		return clearOn;
	}
	public void setClearOn(Date clearOn) {
		this.clearOn = clearOn;
	}
	private Date clearOn;

   

}
