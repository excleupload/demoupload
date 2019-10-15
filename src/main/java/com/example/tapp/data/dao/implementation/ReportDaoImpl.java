package com.example.tapp.data.dao.implementation;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Repository;

import com.example.tapp.common.discriminator.ReportStatus;
import com.example.tapp.common.list.helper.Filter;
import com.example.tapp.common.list.helper.Page;
import com.example.tapp.common.list.helper.Sort;
import com.example.tapp.data.dao.ReportDao;
import com.example.tapp.data.entities.Report;
import com.example.tapp.data.entities.User;
import com.example.tapp.data.exception.RecordNotFoundException;
import com.example.tapp.data.repository.ReportRepository;

@Repository
public class ReportDaoImpl extends UtilDao implements ReportDao {

    @Autowired
    private ReportRepository reportRepo;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Report save(Report report) {
        report.setCreatedOn(new Date());
        report.setModifiedOn(new Date());
        report.setDeleted(false);
        return reportRepo.save(report);
    }

    @Override
    public Report update(Report report) {
        report.setModifiedOn(new Date());
        return reportRepo.save(report);
    }

    @Override
    public Report getById(UUID id) throws RecordNotFoundException {
        return reportRepo.findById(id).orElseThrow(RecordNotFoundException::new);
    }

    @Override
    public void delete(Report report) {
        reportRepo.delete(report);
    }

    @Override
    public Object[] getListByReporter(UUID userId, Page page, Sort... sorts) {
        Object[] objects = new Object[2];

        // list query
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Report> criteriaQuery = builder.createQuery(Report.class);
        Root<Report> root = criteriaQuery.from(Report.class);
        criteriaQuery.select(root);
        criteriaQuery.where(builder.equal(root.get(Report.REPORTER_USER).get(User.ID), userId));
        applySorting(builder, criteriaQuery, root, sorts);

        TypedQuery<Report> query = entityManager.createQuery(criteriaQuery);
        applyPage(query, page);
        objects[0] = query.getResultList();

        // count query
        CriteriaQuery<Long> countQuery = countQuery(Report.class, builder);
        countQuery.where(builder.equal(root.get(Report.REPORTER_USER).get(User.ID), userId));
        objects[1] = entityManager.createQuery(countQuery).getSingleResult();
        return objects;
    }

    @Override
    public List<Report> getUserSupportList() {
        return reportRepo.findAll(org.springframework.data.domain.Sort.by(Direction.DESC, Report.CREATED_ON));
    }

    @Override
    public Object[] getListByReporter(Sort... sorts) {
        Object[] objects = new Object[2];

        // list query
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Report> criteriaQuery = builder.createQuery(Report.class);
        Root<Report> root = criteriaQuery.from(Report.class);
        criteriaQuery.select(root);
        applySorting(builder, criteriaQuery, root, sorts);

        TypedQuery<Report> query = entityManager.createQuery(criteriaQuery);
        objects[0] = query.getResultList();

        // count query
        CriteriaQuery<Long> countQuery = countQuery(Report.class, builder);
        objects[1] = entityManager.createQuery(countQuery).getSingleResult();
        return objects;
    }

    @Override
    public List<Report> getReportDataOfUser(UUID userId) {
        return reportRepo.findByReporterUserIdOrReportedUserId(userId, userId);
    }

    @Override
    public Object[] getListByReplyStatus() {
        // TODO Auto-generated method stub
        Object[] objects = new Object[2];

        // list query
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Report> criteriaQuery = builder.createQuery(Report.class);

        Root<Report> root = criteriaQuery.from(Report.class);
        criteriaQuery.select(root);
        List<Predicate> predicates = new ArrayList<>();

        Predicate predicateRole1 = builder.equal(root.get(Report.STATUS), ReportStatus.PENDING);
        predicates.add(predicateRole1);
        Predicate predicateRole2 = builder.equal(root.get(Report.IS_READ), false);
        predicates.add(predicateRole2);

        criteriaQuery.where(builder.and(predicates.toArray(new Predicate[predicates.size()])));

        TypedQuery<Report> query = entityManager.createQuery(criteriaQuery);

        objects[0] = query.getResultList();

        // count query
        CriteriaQuery<Long> countQuery = countQuery(Report.class, builder);
        objects[1] = entityManager.createQuery(countQuery).getSingleResult();
        // System.out.println(objects[1]);
        return objects;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Object[]> getUserSupportCount() {
        Query query = entityManager
                .createQuery("select count(*) as num from Report u where u.status=:status and u.isRead =:is_read");
        query.setParameter("status", ReportStatus.PENDING);
        query.setParameter("is_read", false);
        return (List<Object[]>) query.getResultList();
    }

    @Override
    @Transactional
    public Object[] getListByAdminnotificatonReply(UUID userId) {
        Object[] objects = new Object[2];

        // list query
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Report> criteriaQuery = builder.createQuery(Report.class);
        Root<Report> root = criteriaQuery.from(Report.class);
        criteriaQuery.select(root);
        criteriaQuery.where(builder.equal(root.get(User.ID), userId));
        applySorting(builder, criteriaQuery, root);

        TypedQuery<Report> query = entityManager.createQuery(criteriaQuery);

        objects[0] = query.getResultList();

        // count query
        CriteriaQuery<Long> countQuery = countQuery(Report.class, builder);
        countQuery.where(builder.equal(root.get(User.ID), userId));
        objects[1] = entityManager.createQuery(countQuery).getSingleResult();
        return objects;
    }

    @Override
    public Object[] getReportedUserList(HashMap<String, Object> filters) {
        Object[] objects = new Object[2];

        // List
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Report> query = builder.createQuery(Report.class);
        Root<Report> root = query.from(Report.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(builder.equal(root.get(Report.STATUS), ReportStatus.PENDING));

        filters.forEach((key, values) -> {
            if (key.equals(Filter.USER_NAME)) {
                Predicate predicate = builder.or(
                        builder.like(root.get(Report.REPORTED_USER).get(User.FIRST_NAME), (String) values),
                        builder.like(root.get(Report.REPORTED_USER).get(User.LAST_NAME), (String) values));
                predicates.add(predicate);
            }
        });

        query.where(predicates.toArray(new Predicate[predicates.size()]));

        query.groupBy(root.get(Report.REPORTED_USER).get(User.FACEBOOK_ID));
        TypedQuery<Report> typedQuery = entityManager.createQuery(query);
        objects[0] = typedQuery.getResultList();

        // Count
        CriteriaQuery<Long> countQuery = countQuery(Report.class, builder);
        countQuery.where(predicates.toArray(new Predicate[predicates.size()]));
        objects[1] = entityManager.createQuery(countQuery).getSingleResult();
        return objects;
    }

}
