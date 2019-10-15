package com.example.tapp.data.dao.implementation;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.tapp.common.discriminator.ConnectionRequestStatus;
import com.example.tapp.common.list.helper.Filter;
import com.example.tapp.common.list.helper.Page;
import com.example.tapp.common.list.helper.Sort;
import com.example.tapp.data.dao.ConnectionRequestDao;
import com.example.tapp.data.entities.ConnectionRequest;
import com.example.tapp.data.entities.User;
import com.example.tapp.data.exception.RecordNotFoundException;
import com.example.tapp.data.repository.ConnectionRequestRepository;

@Repository
public class ConnectionRequestDaoImpl extends UtilDao implements ConnectionRequestDao {

    @Autowired
    private ConnectionRequestRepository connectionRequestRepo;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public ConnectionRequest save(ConnectionRequest entity) {
        entity.setDeleted(false);
        entity.setCreatedOn(new Date());
        entity.setModifiedOn(new Date());
        return connectionRequestRepo.save(entity);
    }

    @Override
    public ConnectionRequest update(ConnectionRequest entity) {
        entity.setModifiedOn(new Date());
        return connectionRequestRepo.save(entity);
    }

    @Override
    public void delete(ConnectionRequest entity) {
        connectionRequestRepo.delete(entity);
    }

    @Override
    public ConnectionRequest get(UUID firstUserId, UUID secondUserId) {
        return connectionRequestRepo.findsByFirstUserIdAndSecondUserId(firstUserId, secondUserId).orElse(null);
    }

    @Override
    public ConnectionRequest swapGet(UUID firstUserId, UUID secondUserId) {
        Optional<ConnectionRequest> optional = connectionRequestRepo.findByFirstUserIdAndSecondUserId(firstUserId,
                secondUserId);
        return optional.isPresent() ? optional.get()
                : connectionRequestRepo.findByFirstUserIdAndSecondUserId(secondUserId, firstUserId).orElse(null);
    }

    @Override
    public ConnectionRequest getById(UUID id) throws RecordNotFoundException {
        return connectionRequestRepo.findById(id).orElseThrow(RecordNotFoundException::new);
    }

    @Override
    public Object[] getListByUserId(UUID userId, ConnectionRequestStatus status, HashMap<String, Object> filter,
            Page page, Sort... sort) {
        if (filter == null || filter.isEmpty()) {
            return fetchListWithoutfilter(userId, status, page, sort);
        } else {
            return fetchListWithFilter(userId, status, filter, page, sort);
        }

    }

    Object[] fetchListWithoutfilter(UUID userId, ConnectionRequestStatus status, Page page, Sort... sort) {
        Object[] data = new Object[2];
        // List Query
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ConnectionRequest> criteriaQuery = builder.createQuery(ConnectionRequest.class);
        Root<ConnectionRequest> root = criteriaQuery.from(ConnectionRequest.class);
        criteriaQuery.select(root);
        List<Predicate> predicates = new ArrayList<>();

        Predicate firstUser = builder.and(builder.equal(root.get(ConnectionRequest.FIRST_USER).get(User.ID), userId),
                builder.equal(root.get(ConnectionRequest.FIRST_USER_STATUS), ConnectionRequestStatus.NOT_NOW));

        Predicate secondUser = builder.and(builder.equal(root.get(ConnectionRequest.SECOND_USER).get(User.ID), userId),
                builder.equal(root.get(ConnectionRequest.SECOND_USER_STATUS), ConnectionRequestStatus.NOT_NOW));

        predicates.add(builder.or(firstUser, secondUser));

        predicates.add(builder.equal(root.get(ConnectionRequest.STATUS), status));
        criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()]));

        applySorting(builder, criteriaQuery, root, sort);
        TypedQuery<ConnectionRequest> query = entityManager.createQuery(criteriaQuery);
        applyPage(query, page);
        List<ConnectionRequest> list = query.getResultList();

        // Count Query
        CriteriaQuery<Long> countQuery = countQuery(ConnectionRequest.class, builder);
        countQuery.where(predicates.toArray(new Predicate[predicates.size()]));
        Long count = entityManager.createQuery(countQuery).getSingleResult();

        data[0] = list;
        data[1] = count;
        return data;
    }

    Object[] fetchListWithFilter(UUID userId, ConnectionRequestStatus status, HashMap<String, Object> filter, Page page,
            Sort... sort) {
        Object[] data = new Object[2];
        // List
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ConnectionRequest> criteriaQuery = builder.createQuery(ConnectionRequest.class);
        Root<ConnectionRequest> root = criteriaQuery.from(ConnectionRequest.class);
        criteriaQuery.select(root);

        List<Predicate> predicates = new ArrayList<>();
        String str = (String) filter.get(Filter.USER_NAME);

        Predicate predicateFirstUser = builder.and(
                builder.equal(root.get(ConnectionRequest.FIRST_USER).get(User.ID), userId),
                builder.or(builder.like(root.get(ConnectionRequest.SECOND_USER).get(User.FIRST_NAME), str),
                        builder.like(root.get(ConnectionRequest.SECOND_USER).get(User.LAST_NAME), str)),
                builder.equal(root.get(ConnectionRequest.FIRST_USER_STATUS), ConnectionRequestStatus.NOT_NOW));

        Predicate predicateSecondUser = builder.and(
                builder.equal(root.get(ConnectionRequest.SECOND_USER).get(User.ID), userId),
                builder.or(builder.like(root.get(ConnectionRequest.FIRST_USER).get(User.FIRST_NAME), str),
                        builder.like(root.get(ConnectionRequest.FIRST_USER).get(User.LAST_NAME), str)),
                builder.equal(root.get(ConnectionRequest.SECOND_USER_STATUS), ConnectionRequestStatus.NOT_NOW));

        predicates.add(builder.or(predicateFirstUser, predicateSecondUser));
        predicates.add(builder.equal(root.get(ConnectionRequest.STATUS), status));
        criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()]));

        applySorting(builder, criteriaQuery, root, sort);
        TypedQuery<ConnectionRequest> query = entityManager.createQuery(criteriaQuery);
        applyPage(query, page);
        List<ConnectionRequest> list = query.getResultList();

        // Count Query
        CriteriaQuery<Long> countQuery = countQuery(ConnectionRequest.class, builder);
        countQuery.where(predicates.toArray(new Predicate[predicates.size()]));
        Long count = entityManager.createQuery(countQuery).getSingleResult();

        data[0] = list;
        data[1] = count;
        return data;
    }
}