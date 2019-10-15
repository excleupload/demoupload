package com.example.tapp.data.dao.implementation;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Repository;

import com.example.tapp.data.dao.UserNotificationDao;
import com.example.tapp.data.entities.UserNotification;
import com.example.tapp.data.exception.RecordNotFoundException;
import com.example.tapp.data.repository.UserNotificationRepository;

@Repository
@Transactional
public class UserNotificationDaoImpl implements UserNotificationDao {

    @Autowired
    private UserNotificationRepository notifyRepo;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public UserNotification save(UserNotification entity) {
        entity.setCreatedOn(new Date());
        entity.setModifiedOn(new Date());
        entity.setDeleted(false);
        return notifyRepo.save(entity);
    }

    @Override
    public UserNotification update(UserNotification entity) {
        entity.setModifiedOn(new Date());
        return notifyRepo.save(entity);
    }

    @Override
    public List<UserNotification> list(UUID userId) {
        return notifyRepo.findByUserId(userId, org.springframework.data.domain.Sort.by(Direction.DESC, UserNotification.CREATED_ON));
    }

    @Override
    public UserNotification getByuserIdAndId(UUID userId, UUID lastNotify) throws RecordNotFoundException {
        return notifyRepo.findByUserIdAndId(userId, lastNotify).orElseThrow(RecordNotFoundException::new);
    }

    @Override
    public void clear(UserNotification lastNotification) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaDelete<UserNotification> criteriaDelete = builder.createCriteriaDelete(UserNotification.class);
        Root<UserNotification> root = criteriaDelete.from(UserNotification.class);

        Predicate predicateUser = builder.equal(root.get(UserNotification.USER_ID), lastNotification.getUserId());
        Predicate predicateCreatedOn = builder.lessThanOrEqualTo(root.get(UserNotification.CREATED_ON),
                lastNotification.getCreatedOn());
        criteriaDelete.where(builder.and(predicateUser, predicateCreatedOn));
        entityManager.createQuery(criteriaDelete).executeUpdate();
    }

}
