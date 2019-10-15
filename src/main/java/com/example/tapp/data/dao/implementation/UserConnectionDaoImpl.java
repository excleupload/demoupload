package com.example.tapp.data.dao.implementation;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
import com.example.tapp.common.discriminator.UserRole;
import com.example.tapp.common.list.helper.Filter;
import com.example.tapp.common.list.helper.Page;
import com.example.tapp.common.list.helper.Sort;
import com.example.tapp.data.dao.UserConnectionDao;
import com.example.tapp.data.entities.ConnectionRequest;
import com.example.tapp.data.entities.User;
import com.example.tapp.data.entities.UserConnection;
import com.example.tapp.data.exception.RecordNotFoundException;
import com.example.tapp.data.repository.UserConnectionRepository;

@Repository
public class UserConnectionDaoImpl extends UtilDao implements UserConnectionDao {

    @Autowired
    private UserConnectionRepository connRepo;

    @PersistenceContext
    private EntityManager entityManager;
    User user;

    @Override
    public UserConnection save(UserConnection entity) {
        entity.setCreatedOn(new Date());
        entity.setModifiedOn(new Date());
        entity.setDeleted(false);
        return connRepo.save(entity);
    }

    @Override
    public UserConnection update(UserConnection entity) {
        entity.setModifiedOn(new Date());
        return connRepo.save(entity);
    }

    @Override
    public Object[] getListByOwnerId(UUID ownerId, ConnectionRequestStatus status, HashMap<String, Object> filter, Page page,
            Sort... sorts) {
        Object[] objects = new Object[2];

        // List Query
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<UserConnection> criteriaQuery = builder.createQuery(UserConnection.class);
        Root<UserConnection> root = criteriaQuery.from(UserConnection.class);
        criteriaQuery.select(root);

        Predicate predicateOwner = builder.equal(root.get(UserConnection.OWNER_USER).get(User.ID), ownerId);
        Predicate predicateUserRole = builder.equal(root.get(UserConnection.CONNECTED_USER).get(User.ROLE),
                UserRole.ROLE_USER);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(predicateOwner);
        predicates.add(predicateUserRole);

        if (filter != null && !filter.isEmpty()) {
            filter.forEach((key, value) -> {
                if (key.equals(Filter.USER_NAME)) {
                    Predicate predicateFirstName = builder.like(
                            root.get(UserConnection.CONNECTED_USER).get(User.FIRST_NAME), "%" + (String) value + "%");
                    Predicate predicateLastName = builder.like(
                            root.get(UserConnection.CONNECTED_USER).get(User.LAST_NAME), "%" + (String) value + "%");
                    Predicate predicatePlace = builder.like(
                            root.get(UserConnection.REQUEST_REFERENCE).get(ConnectionRequest.PLACE),
                            "%" + (String) value + "%");

                    predicates.add(builder.or(predicateFirstName, predicateLastName, predicatePlace));

                }
            });
        }
        criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()]));

        applySorting(builder, criteriaQuery, root, sorts);

        TypedQuery<UserConnection> query = entityManager.createQuery(criteriaQuery);
        applyPage(query, page);
        objects[0] = query.getResultList();

        // Count Query
        CriteriaQuery<Long> countQuery = countQuery(UserConnection.class, builder);
        countQuery.where(predicates.toArray(new Predicate[predicates.size()]));
        objects[1] = entityManager.createQuery(countQuery).getSingleResult();
        return objects;
    }

    @Override
    public List<Object[]> getListofUserName(UUID ownerId, ConnectionRequestStatus status, Sort... sorts) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> criteriaQuery = builder.createQuery(Object[].class);
        Root<UserConnection> root = criteriaQuery.from(UserConnection.class);
        criteriaQuery.multiselect(root.get(UserConnection.CONNECTED_USER).get(User.ID),
                root.get(UserConnection.CONNECTED_USER).get(User.FIRST_NAME),
                root.get(UserConnection.CONNECTED_USER).get(User.LAST_NAME));

        Predicate predicateOwner = builder.equal(root.get(UserConnection.OWNER_USER).get(User.ID), ownerId);
        Predicate predicateStatus = builder.equal(root.get(UserConnection.STATUS), status);
        Predicate predicateUserRole = builder.equal(root.get(UserConnection.CONNECTED_USER).get(User.ROLE),
                UserRole.ROLE_USER);
        criteriaQuery.where(builder.and(predicateOwner, predicateStatus, predicateUserRole));

        TypedQuery<Object[]> query = entityManager.createQuery(criteriaQuery);
        return query.getResultList();
    }

    @Override
    public UserConnection getByOwnerAndConnectedUser(UUID ownerId, UUID connectedUserId)
            throws RecordNotFoundException {
        return connRepo.findByOwnerUserIdAndConnectedUserId(ownerId, connectedUserId)
                .orElseThrow(RecordNotFoundException::new);
    }

    @Override
    public List<Object> getConnectedUserIdByOwner(UUID ownerId, ConnectionRequestStatus status) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object> criteriaQuery = builder.createQuery(Object.class);
        Root<UserConnection> root = criteriaQuery.from(UserConnection.class);
        criteriaQuery.multiselect(root.get(UserConnection.CONNECTED_USER).get(User.ID));

        Predicate predicateOwner = builder.equal(root.get(UserConnection.OWNER_USER).get(User.ID), ownerId);
        Predicate predicateStatus = builder.equal(root.get(UserConnection.STATUS), status);
        criteriaQuery.where(builder.and(predicateOwner, predicateStatus));
        TypedQuery<Object> query = entityManager.createQuery(criteriaQuery);
        return query.getResultList();
    }

    @Override
    public List<Object> getConnectedUsersDialogId(UUID ownerId, ConnectionRequestStatus status) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object> criteriaQuery = builder.createQuery(Object.class);
        Root<UserConnection> root = criteriaQuery.from(UserConnection.class);
        criteriaQuery.select(root.get(UserConnection.DIALOG_ID));

        // Filter
        Predicate predicateOwner = builder.equal(root.get(UserConnection.OWNER_USER).get(User.ID), ownerId);
        Predicate predicateStatus = builder.equal(root.get(UserConnection.STATUS), status);
        Predicate predicateDialog = builder.isNotNull(root.get(UserConnection.DIALOG_ID));
        criteriaQuery.where(builder.and(predicateOwner, predicateStatus, predicateDialog));

        TypedQuery<Object> query = entityManager.createQuery(criteriaQuery);
        return query.getResultList();
    }

    @Override
    public List<Object> getConnectedUsersDialogId(UUID ownerId, ConnectionRequestStatus status, HashMap<String, Object> filter,
            Sort... sorts) {
        // TODO Auto-generated method stub

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object> criteriaQuery = builder.createQuery(Object.class);
        Root<UserConnection> root = criteriaQuery.from(UserConnection.class);
        criteriaQuery.multiselect(root.get(UserConnection.DIALOG_ID));

        // Filter
        Predicate predicateOwner = builder.equal(root.get(UserConnection.OWNER_USER).get(User.ID), ownerId);
        // Predicate predicateStatus = builder.equal(root.get(UserConnection.STATUS),
        // status);
        Predicate predicateDialog = builder.isNotNull(root.get(UserConnection.DIALOG_ID));
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(builder.and(predicateOwner, predicateDialog));

        if (filter != null && !filter.isEmpty()) {
            filter.forEach((key, value) -> {
                if (key.equals(Filter.USER_NAME)) {
                    Predicate predicateFirstName = builder
                            .like(root.get(UserConnection.CONNECTED_USER).get(User.FIRST_NAME), (String) value);
                    Predicate predicateLastName = builder
                            .like(root.get(UserConnection.CONNECTED_USER).get(User.LAST_NAME), (String) value);
                    predicates.add(builder.or(predicateFirstName, predicateLastName));

                }
            });
        }
        criteriaQuery.where(builder.and(predicates.toArray(new Predicate[predicates.size()])));

        // applySorting(builder, criteriaQuery, root, sorts);

        TypedQuery<Object> query = entityManager.createQuery(criteriaQuery);
        return query.getResultList();
    }

    @Override
    public UserConnection getById(UUID id) throws RecordNotFoundException {
        return connRepo.findById(id).orElseThrow(RecordNotFoundException::new);
    }

    @Override
    public void delete(UserConnection entity) {
        connRepo.delete(entity);
    }

    @Override
    public Long getTotalConnection() {
        return connRepo.countByOwnerUserRoleAndConnectedUserRole(UserRole.ROLE_USER, UserRole.ROLE_USER);
    }
}
