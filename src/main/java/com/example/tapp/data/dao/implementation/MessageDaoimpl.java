package com.example.tapp.data.dao.implementation;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Repository;

import com.example.tapp.common.discriminator.MessageStatus;
import com.example.tapp.common.list.helper.Page;
import com.example.tapp.common.list.helper.Sort;
import com.example.tapp.data.dao.MessageDao;
import com.example.tapp.data.entities.Message;
import com.example.tapp.data.entities.MessageDialog;
import com.example.tapp.data.exception.RecordNotFoundException;
import com.example.tapp.data.repository.MessageRepository;

@Repository
public class MessageDaoimpl extends UtilDao implements MessageDao {

    @Autowired
    private MessageRepository messageRepo;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Message save(Message entity) {
        entity.setCreatedOn(new Date());
        entity.setModifiedOn(new Date());
        entity.setDeleted(false);
        return messageRepo.save(entity);
    }

    @Override
    public Message update(Message entity) {
        entity.setModifiedOn(new Date());
        return messageRepo.save(entity);
    }

    @Override
    public Message getMessageById(UUID messageId) throws RecordNotFoundException {
        return messageRepo.findById(messageId).orElseThrow(RecordNotFoundException::new);
    }

    @Override
    public List<Message> pendingMessage(UUID receiverId) {
        org.springframework.data.domain.Sort sort = org.springframework.data.domain.Sort.by(Direction.ASC, Message.CREATED_ON);
        return messageRepo.findByReceiverIdAndStatus(receiverId, MessageStatus.PENDING, sort);
    }

    @Override
    public Object[] list(UUID dialogId, Date clearOn, UUID userId, Page page) {
        Object[] objects = new Object[2];

        // List
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Message> criteriaQuery = builder.createQuery(Message.class);
        Root<Message> root = criteriaQuery.from(Message.class);

        Predicate predicateDialog = builder.equal(root.get(Message.DIALOG).get(MessageDialog.ID), dialogId);
        Predicate predicateReceiverId = builder.and(builder.equal(root.get(Message.RECEIVER_ID), userId),
                builder.notEqual(root.get(Message.STATUS), MessageStatus.NOT_SENDABLE));
        Predicate predicateSenderId = builder.equal(root.get(Message.SENDER_ID), userId);

        Predicate predicateUser = builder.or(predicateReceiverId, predicateSenderId);
        if (clearOn == null) {
            criteriaQuery.where(builder.and(predicateDialog, predicateUser));
        } else {
            Predicate predicateClear = builder.greaterThan(root.get(Message.CREATED_ON), clearOn);
            criteriaQuery.where(builder.and(predicateDialog, predicateUser, predicateClear));
        }
        criteriaQuery.orderBy(builder.asc(root.get(Message.CREATED_ON)));
        TypedQuery<Message> query = entityManager.createQuery(criteriaQuery);
        applyPage(query, page);

        objects[0] = query.getResultList();

        // Count
        CriteriaQuery<Long> countQuery = countQuery(Message.class, builder);
        if (clearOn == null) {
            countQuery.where(predicateDialog);
        } else {
            Predicate predicateClear = builder.greaterThan(root.get(Message.CREATED_ON), clearOn);
            countQuery.where(predicateDialog, predicateClear);
        }
        objects[1] = entityManager.createQuery(countQuery).getSingleResult();
        return objects;
    }

    @Override
    public Long listCount(UUID dialogId, Date clearOn, UUID userId) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = builder.createQuery(Long.class);
        Root<Message> root = criteriaQuery.from(Message.class);
        criteriaQuery.select(builder.count(root));

        Predicate predicateDialog = builder.equal(root.get(Message.DIALOG).get(MessageDialog.ID), dialogId);
        Predicate predicateReceiverId = builder.and(builder.equal(root.get(Message.RECEIVER_ID), userId),
                builder.notEqual(root.get(Message.STATUS), MessageStatus.NOT_SENDABLE));

        Predicate predicateSenderId = builder.equal(root.get(Message.SENDER_ID), userId);

        Predicate predicateUser = builder.or(predicateReceiverId, predicateSenderId);

        if (clearOn == null) {
            criteriaQuery.where(builder.and(predicateDialog, predicateUser));
        } else {
            Predicate predicateClear = builder.greaterThan(root.get(Message.CREATED_ON), clearOn);
            criteriaQuery.where(builder.and(predicateDialog, predicateUser, predicateClear));
        }
        return entityManager.createQuery(criteriaQuery).getSingleResult();
    }

    @Override
    @Transactional
    public void deleteMessage(UUID dialogId, Date clearDate) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaDelete<Message> criteriaDelete = builder.createCriteriaDelete(Message.class);
        Root<Message> root = criteriaDelete.from(Message.class);

        Predicate predicateDialog = builder.equal(root.get(Message.DIALOG).get(MessageDialog.ID), dialogId);
        Predicate predicateDate = builder.lessThan(root.get(Message.CREATED_ON), clearDate);
        criteriaDelete.where(predicateDialog, predicateDate);

        entityManager.createQuery(criteriaDelete).executeUpdate();
    }

    @Override
    public Message getLastMessage(UUID userId) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Message> criteriaQuery = builder.createQuery(Message.class);
        Root<Message> root = criteriaQuery.from(Message.class);

        Predicate predicateUser = builder.or(builder.equal(root.get(Message.SENDER_ID), userId),
                builder.equal(root.get(Message.RECEIVER_ID), userId));
        Predicate predicateStatus = builder.equal(root.get(Message.STATUS), MessageStatus.SENT);
        criteriaQuery.where(predicateUser, predicateStatus);
        criteriaQuery.orderBy(builder.desc(root.get(Message.CREATED_ON)));
        TypedQuery<Message> query = entityManager.createQuery(criteriaQuery);
        query.setFirstResult(0);
        query.setMaxResults(1);
        return query.getSingleResult();
    }
}
