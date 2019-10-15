package com.example.tapp.business.service.implementation;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.tapp.business.service.RateAndFeedBackService;
import com.example.tapp.common.dto.RateAndFeedbackDto;
import com.example.tapp.data.dao.RateAndFeedbackDao;
import com.example.tapp.data.entities.RateAppAndFeedback;
import com.example.tapp.data.exception.RecordNotFoundException;

@Service("rateAndFeedBackservice")
public class RateAndFeedBackServiceImpl implements RateAndFeedBackService {

    @Autowired
    private RateAndFeedbackDao rateDao;

    @Override
    public RateAndFeedbackDto save(RateAndFeedbackDto dto) throws RecordNotFoundException {
        RateAppAndFeedback entity = rateDao.getRateByUserId(dto.getUserId());
        boolean isNew = entity == null ? true : false;
        entity = entity == null ? new RateAppAndFeedback() : entity;
        entity.setUserId(dto.getUserId());
        entity.setComment(dto.getComment());
        entity.setRating(dto.getRating());
        entity = isNew ? rateDao.save(entity) : rateDao.update(entity);
        return entity.toDto();
    }

    @Override
    public List<RateAndFeedbackDto> getRateAndFeedBack() {
        try {
            return rateDao.getRateList().stream().map(RateAndFeedbackDto::new).collect(Collectors.toList());
        } catch (Exception e) {
            throw new IllegalArgumentException("List Not Found");
        }

    }

}