package com.example.tapp.data.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class BaseEntity<T extends Serializable> {


    public static final String ID = "id";
    public static final String CREATED_ON = "createdOn";
    public static final String MODIFIED_ON = "modifiedOn";
    public static final String DELETED = "deleted";

 
    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private T Id;
    private Date createdOn;
    private Date modifiedOn;
    public T getId() {
		return Id;
	}
	public void setId(T id) {
		Id = id;
	}
	public Date getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	public Date getModifiedOn() {
		return modifiedOn;
	}
	public void setModifiedOn(Date modifiedOn) {
		this.modifiedOn = modifiedOn;
	}
	public boolean isDeleted() {
		return deleted;
	}
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	private boolean deleted;



}