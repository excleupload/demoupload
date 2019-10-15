package com.example.tapp.business.service;

import java.util.List;

import com.example.tapp.common.dto.RateAndFeedbackDto;
import com.example.tapp.data.exception.RecordNotFoundException;

public interface RateAndFeedBackService {

	RateAndFeedbackDto save(RateAndFeedbackDto dto) throws RecordNotFoundException;

	List<RateAndFeedbackDto> getRateAndFeedBack();

}
