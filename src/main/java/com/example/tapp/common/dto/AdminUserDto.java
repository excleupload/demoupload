package com.example.tapp.common.dto;

import java.io.Serializable;

import com.example.tapp.data.entities.AdminUser;

public class AdminUserDto implements Serializable {

    public static final String ID = "id";
    public static final String NAME = "name";

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String name;
    private String email;
    private String password;
    private String authToken;
    private Long lastLogin;
    private Long createdOn;
    private Long modifiedOn;

    public AdminUserDto() {

    }

    public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAuthToken() {
		return authToken;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

	public Long getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Long lastLogin) {
		this.lastLogin = lastLogin;
	}

	public Long getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Long createdOn) {
		this.createdOn = createdOn;
	}

	public Long getModifiedOn() {
		return modifiedOn;
	}

	public void setModifiedOn(Long modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

	public AdminUserDto(AdminUser adminUser) {
        this.id = adminUser.getId();
        this.name = adminUser.getName();
        this.email = adminUser.getEmail();
        this.authToken = adminUser.getAuthToken();
        this.lastLogin = adminUser.getLastLogin();
        this.createdOn = adminUser.getCreatedOn().getTime();
        this.modifiedOn = adminUser.getModifiedOn().getTime();
    }

   

}

