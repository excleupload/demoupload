package com.example.tapp.data.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.example.tapp.common.discriminator.UserRole;
import com.example.tapp.common.discriminator.UserStatus;
import com.example.tapp.common.dto.UserDto;

@Entity
@Table(name = "user_detail")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class User extends BaseEntityUUID {
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";
    public static final String STATUS = "status";
    public static final String ROLE = "role";
    public static final String IsRead = "isRead";
    public static final String FACEBOOK_ID = "facebookId";
    public static final String EMAIL = "email";

    private String firstName;

    private String lastName;

    private String email;

    private String mobile;

    @Column(length = 10)
    private String countryCode;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private UserRole role;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private UserStatus status;

    private String facebookId;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String authToken;

    private boolean mobileVerified = false;

    private boolean socialMediaShare = true;

    @OneToOne(cascade = CascadeType.ALL, targetEntity = UserProfile.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id")
    private UserProfile profile;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user", orphanRemoval = true)
    @JoinColumn(name = "user_id")
    private UserDevice device;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "ownerUser")
    private Set<UserConnection> connections = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "connectedUser")
    private Set<UserConnection> connected = new HashSet<>();

    @ManyToMany(mappedBy = "occupants")
    private Set<MessageDialog> dialogs = new HashSet<>();

    @Column(nullable = false)
    private Boolean isRead = false;

    public User() {

    }

    public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public UserRole getRole() {
		return role;
	}

	public void setRole(UserRole role) {
		this.role = role;
	}

	public UserStatus getStatus() {
		return status;
	}

	public void setStatus(UserStatus status) {
		this.status = status;
	}

	public String getFacebookId() {
		return facebookId;
	}

	public void setFacebookId(String facebookId) {
		this.facebookId = facebookId;
	}

	public String getAuthToken() {
		return authToken;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

	public boolean isMobileVerified() {
		return mobileVerified;
	}

	public void setMobileVerified(boolean mobileVerified) {
		this.mobileVerified = mobileVerified;
	}

	public boolean isSocialMediaShare() {
		return socialMediaShare;
	}

	public void setSocialMediaShare(boolean socialMediaShare) {
		this.socialMediaShare = socialMediaShare;
	}

	public UserProfile getProfile() {
		return profile;
	}

	public void setProfile(UserProfile profile) {
		this.profile = profile;
	}

	public UserDevice getDevice() {
		return device;
	}

	public void setDevice(UserDevice device) {
		this.device = device;
	}

	public Set<UserConnection> getConnections() {
		return connections;
	}

	public void setConnections(Set<UserConnection> connections) {
		this.connections = connections;
	}

	public Set<UserConnection> getConnected() {
		return connected;
	}

	public void setConnected(Set<UserConnection> connected) {
		this.connected = connected;
	}

	public Set<MessageDialog> getDialogs() {
		return dialogs;
	}

	public void setDialogs(Set<MessageDialog> dialogs) {
		this.dialogs = dialogs;
	}

	public Boolean getIsRead() {
		return isRead;
	}

	public void setIsRead(Boolean isRead) {
		this.isRead = isRead;
	}

	public User(UserDto userDto) {
        this.firstName = userDto.getFirstName();
        this.lastName = userDto.getLastName();
        this.email = userDto.getEmail();
        this.countryCode = userDto.getCountryCode();
        this.mobile = userDto.getMobile();

    }



    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof User))
            return false;
        User user = (User) obj;
        if (this.getId().equals(user.getId()))
            return true;
        return true;
    }

    public UserDto toDto() {
        return new UserDto(this);
    }

}