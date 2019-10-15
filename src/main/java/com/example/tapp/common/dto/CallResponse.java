package com.example.tapp.common.dto;

import com.example.tapp.data.entities.User;

public class CallResponse {

    private String chanelId;
    private Long time;
    private Long receiverId;
    private Long senderId;
    private Integer type;
    private Integer status;
    private User userInfo;
    
	public String getChanelId() {
		return chanelId;
	}
	public void setChanelId(String chanelId) {
		this.chanelId = chanelId;
	}
	public Long getTime() {
		return time;
	}
	public void setTime(Long time) {
		this.time = time;
	}
	public Long getReceiverId() {
		return receiverId;
	}
	public void setReceiverId(Long receiverId) {
		this.receiverId = receiverId;
	}
	public Long getSenderId() {
		return senderId;
	}
	public void setSenderId(Long senderId) {
		this.senderId = senderId;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public User getUserInfo() {
		return userInfo;
	}
	public void setUserInfo(User userInfo) {
		this.userInfo = userInfo;
	}
   


}