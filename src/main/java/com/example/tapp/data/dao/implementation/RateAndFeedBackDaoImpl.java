package com.example.tapp.data.dao.implementation;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Repository;

import com.example.tapp.data.dao.RateAndFeedbackDao;
import com.example.tapp.data.entities.RateAppAndFeedback;
import com.example.tapp.data.entities.StaticPages;
import com.example.tapp.data.repository.RateAndFeedBackRepository;

@Repository
@Transactional
public class RateAndFeedBackDaoImpl implements RateAndFeedbackDao {

    @Autowired
    
    private RateAndFeedBackRepository rateRepo;

    @Override
    public RateAppAndFeedback save(RateAppAndFeedback entity) {
        entity.setCreatedOn(new Date());
        entity.setModifiedOn(new Date());
        entity.setDeleted(false);
        return rateRepo.save(entity);
    }

    @Override
    public RateAppAndFeedback update(RateAppAndFeedback entity) {
        entity.setModifiedOn(new Date());
        return rateRepo.save(entity);
    }

    @Override
    public RateAppAndFeedback getRateByUserId(UUID id){
        return rateRepo.findByUserId(id).orElse(null);
    }
    @Override
    public List<RateAppAndFeedback> getRateList() {
        return rateRepo.findAll(Sort.by(Direction.DESC, StaticPages.CREATED_ON));
    }
}
