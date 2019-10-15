package com.example.tapp.common.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

import com.example.tapp.common.discriminator.ConnectionRequestStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_NULL)
public class ConnectionRequestDto implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String FIRST_USER_ID = "firstUserId";
    public static final String SECOND_USER_ID = "secondUserId";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String PLACE = "place";

    private UUID id;
    private UUID firstUserId;
    private UUID secondUserId;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String place;
    private UserDto firstUser;
    private UserDto secondUser;
    private ConnectionRequestStatus status;
    private Long time;
    private Boolean firstUserAccepted;
    private Boolean seconUserAccepted;
    private UserDto user;
    private Boolean userAccepted;
    

    public UUID getId() {
		return id;
	}


	public void setId(UUID id) {
		this.id = id;
	}


	public UUID getFirstUserId() {
		return firstUserId;
	}


	public void setFirstUserId(UUID firstUserId) {
		this.firstUserId = firstUserId;
	}


	public UUID getSecondUserId() {
		return secondUserId;
	}


	public void setSecondUserId(UUID secondUserId) {
		this.secondUserId = secondUserId;
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


	public String getPlace() {
		return place;
	}


	public void setPlace(String place) {
		this.place = place;
	}


	public UserDto getFirstUser() {
		return firstUser;
	}


	public void setFirstUser(UserDto firstUser) {
		this.firstUser = firstUser;
	}


	public UserDto getSecondUser() {
		return secondUser;
	}


	public void setSecondUser(UserDto secondUser) {
		this.secondUser = secondUser;
	}


	public ConnectionRequestStatus getStatus() {
		return status;
	}


	public void setStatus(ConnectionRequestStatus status) {
		this.status = status;
	}


	public Long getTime() {
		return time;
	}


	public void setTime(Long time) {
		this.time = time;
	}


	public Boolean getFirstUserAccepted() {
		return firstUserAccepted;
	}


	public void setFirstUserAccepted(Boolean firstUserAccepted) {
		this.firstUserAccepted = firstUserAccepted;
	}


	public Boolean getSeconUserAccepted() {
		return seconUserAccepted;
	}


	public void setSeconUserAccepted(Boolean seconUserAccepted) {
		this.seconUserAccepted = seconUserAccepted;
	}


	public UserDto getUser() {
		return user;
	}


	public void setUser(UserDto user) {
		this.user = user;
	}


	public Boolean getUserAccepted() {
		return userAccepted;
	}


	public void setUserAccepted(Boolean userAccepted) {
		this.userAccepted = userAccepted;
	}


	public ConnectionRequestDto() {

    }


}
