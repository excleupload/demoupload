package com.example.tapp.presentation.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.tapp.business.service.RateAndFeedBackService;
import com.example.tapp.common.dto.RateAndFeedbackDto;
import com.example.tapp.data.exception.GeneralException;
import com.example.tapp.data.exception.RecordNotFoundException;
import com.example.tapp.data.exception.UserNotFoundException;
import static com.example.tapp.common.response.ResponseUtils.errorList;
import static com.example.tapp.common.response.ResponseUtils.success;
import static com.example.tapp.common.utils.Route.rating;
import static com.example.tapp.common.utils.Route.ratingSave;
import static com.example.tapp.common.utils.Route.ratingdisplay;


@RestController
public class RateAndFeedbackController {

    @Autowired
    private RateAndFeedBackService rateAndFeedBackService;

    @PostMapping(ratingSave)
    public ResponseEntity<?> save(@RequestBody RateAndFeedbackDto dto, BindingResult result)
            throws GeneralException, UserNotFoundException, RecordNotFoundException {

        new Rating().validate(dto, result);
        if (result.hasErrors()) {
            return errorList.apply(result.getFieldErrors());
        }
        return success.apply(rateAndFeedBackService.save(dto));
    }
    @GetMapping(ratingdisplay)
    public ResponseEntity<?> getRateAndFeedbackData() {
        return success.apply(rateAndFeedBackService.getRateAndFeedBack());
    }
}