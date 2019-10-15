package com.example.tapp.common.dto;


import java.io.Serializable;
import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_NULL)
public class Push implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final Integer CONNECTION_REQUEST_ACCEPT = 1;
    public static final Integer CONNECTION_REQUEST_REJECT = 2;
    public static final Integer MESSAGE = 3;
    public static final Integer ADMIN_NOTIFICATION = 4;

    private Integer type;
    private String title;
    private Long time;
    private String message;
    private HashMap<String, Object> appData = new HashMap<>();

    public Push() {

    }

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public HashMap<String, Object> getAppData() {
		return appData;
	}

	public void setAppData(HashMap<String, Object> appData) {
		this.appData = appData;
	}

  

}