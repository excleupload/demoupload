package com.example.tapp.data.dao;

import java.util.List;
import java.util.UUID;

import com.example.tapp.data.entities.RateAppAndFeedback;

public interface RateAndFeedbackDao {

	RateAppAndFeedback save(RateAppAndFeedback entity);

	RateAppAndFeedback update(RateAppAndFeedback entity);

	RateAppAndFeedback getRateByUserId(UUID id);

	List<RateAppAndFeedback> getRateList();

}
