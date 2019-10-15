package com.example.tapp.common.dto;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

public class NotificationDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<String> emails;
    private String subject;
    private String body;
    private List<UUID> userIds;

    public NotificationDto() {
        super();
    }

    public NotificationDto(List<String> emails, String subject, String body, List<UUID> userIds) {
        super();
        this.emails = emails;
        this.subject = subject;
        this.body = body;
        this.userIds = userIds;

    }

	public List<String> getEmails() {
		return emails;
	}

	public void setEmails(List<String> emails) {
		this.emails = emails;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public List<UUID> getUserIds() {
		return userIds;
	}

	public void setUserIds(List<UUID> userIds) {
		this.userIds = userIds;
	}
    

}