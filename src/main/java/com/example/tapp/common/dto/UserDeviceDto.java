package com.example.tapp.common.dto;

import java.io.Serializable;
import java.util.UUID;

import com.example.tapp.common.discriminator.DeviceType;

public class UserDeviceDto implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String USER_ID = "userId";
    public static final String TYPE = "type";
    public static final String DEVICE_TOKEN = "deviceToken";

    private UUID userId;
    private DeviceType type;
    private String deviceToken;
	public UUID getUserId() {
		return userId;
	}
	public void setUserId(UUID userId) {
		this.userId = userId;
	}
	public DeviceType getType() {
		return type;
	}
	public void setType(DeviceType type) {
		this.type = type;
	}
	public String getDeviceToken() {
		return deviceToken;
	}
	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}

   

}
