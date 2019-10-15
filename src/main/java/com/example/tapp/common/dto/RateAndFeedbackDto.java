package com.example.tapp.common.dto;

import java.io.Serializable;
import java.util.UUID;

import com.example.tapp.data.entities.BaseEntityUUID;
import com.example.tapp.data.entities.RateAppAndFeedback;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_NULL)
public class RateAndFeedbackDto extends BaseEntityUUID implements Serializable  {

    private static final long serialVersionUID = 6989912648118479042L;

    public static final String COMMENT = "comment";
    public static final String RATING = "rating";
    public static final String USER_ID = "userId";

    private String comment;
    private float rating;
    private UUID userId;

    public RateAndFeedbackDto() {

    }

    public RateAndFeedbackDto(RateAppAndFeedback rate) {

        this.comment = rate.getComment();
        this.rating = rate.getRating();
        this.userId=getId();
    }

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public float getRating() {
		return rating;
	}

	public void setRating(float rating) {
		this.rating = rating;
	}

	public UUID getUserId() {
		return userId;
	}

	public void setUserId(UUID userId) {
		this.userId = userId;
	}



}