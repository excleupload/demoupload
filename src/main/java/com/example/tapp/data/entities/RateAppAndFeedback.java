package com.example.tapp.data.entities;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import com.example.tapp.common.dto.RateAndFeedbackDto;

@Entity
@Table(name = "rate_app_and_feedback")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class RateAppAndFeedback extends BaseEntity<Long> {

    @Column(name = "comment", nullable = false, length = 1000)
    private String comment;

    @Column(name = "rating", nullable = true)
    private float rating;

    @Column(name = "user_id", length = 16)
    private UUID userId;

   
   
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



	public RateAndFeedbackDto toDto() {
            return new RateAndFeedbackDto(this);
        }

}