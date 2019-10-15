package com.example.tapp.common.dto;

import java.io.Serializable;
import java.util.UUID;

import com.example.tapp.common.discriminator.ConnectionRequestStatus;

public class RequestOperation implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String ID = "id";
    public static final String OPERATION = "operation";
    public static final String BY = "by";
    
    private UUID id;
    public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	
    private String by;
    
    ConnectionRequestStatus operation;
	
	
	public ConnectionRequestStatus getOperation() {
		return operation;
	}
	public void setOperation(ConnectionRequestStatus operation) {
		this.operation = operation;
	}
	public String getBy() {
		return by;
	}
	public void setBy(String by) {
		this.by = by;
	}

 

}