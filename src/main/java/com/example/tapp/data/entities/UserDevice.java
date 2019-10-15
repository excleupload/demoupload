package com.example.tapp.data.entities;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.example.tapp.common.discriminator.DeviceType;

@Entity
@Table(name = "user_device")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class UserDevice extends BaseEntityUUID {

    private String deviceToken;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private DeviceType type;

    public String getDeviceToken() {
		return deviceToken;
	}

	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}

	public DeviceType getType() {
		return type;
	}

	public void setType(DeviceType type) {
		this.type = type;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@OneToOne(fetch = FetchType.LAZY)
    private User user;

   

}