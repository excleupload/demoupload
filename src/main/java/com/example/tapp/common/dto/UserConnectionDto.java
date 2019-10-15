package com.example.tapp.common.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

import com.example.tapp.common.discriminator.ConnectionRequestStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_NULL)
public class UserConnectionDto implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String USER_NAME = "name";

    private UUID id;
    private UserDto user;
    private Long time;
    private String place;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String dialogId;
    private ConnectionRequestStatus status;
    private ConnectionRequestStatus secondUserStatus;
    private Boolean visited;
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	public UserDto getUser() {
		return user;
	}
	public void setUser(UserDto user) {
		this.user = user;
	}
	public Long getTime() {
		return time;
	}
	public void setTime(Long time) {
		this.time = time;
	}
	public String getPlace() {
		return place;
	}
	public void setPlace(String place) {
		this.place = place;
	}
	public BigDecimal getLatitude() {
		return latitude;
	}
	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
	}
	public BigDecimal getLongitude() {
		return longitude;
	}
	public void setLongitude(BigDecimal longitude) {
		this.longitude = longitude;
	}
	public String getDialogId() {
		return dialogId;
	}
	public void setDialogId(String dialogId) {
		this.dialogId = dialogId;
	}
	public ConnectionRequestStatus getStatus() {
		return status;
	}
	public void setStatus(ConnectionRequestStatus status) {
		this.status = status;
	}
	public ConnectionRequestStatus getSecondUserStatus() {
		return secondUserStatus;
	}
	public void setSecondUserStatus(ConnectionRequestStatus secondUserStatus) {
		this.secondUserStatus = secondUserStatus;
	}
	public Boolean getVisited() {
		return visited;
	}
	public void setVisited(Boolean visited) {
		this.visited = visited;
	}



}
