package com.example.tapp.common.dto;


import java.io.Serializable;

public class AdminChangePassword implements Serializable {

    public static final String ID = "id";
    public static final String CURRENT_PASSWORD = "currentPassword";
    public static final String NEW_PASSWORD = "newPassword";
   
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String currentPassword;
    public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCurrentPassword() {
		return currentPassword;
	}
	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
	}
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	private String newPassword;

}
