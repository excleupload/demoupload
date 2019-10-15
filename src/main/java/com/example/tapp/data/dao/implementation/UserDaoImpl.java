package com.example.tapp.data.dao.implementation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
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

import com.example.tapp.common.discriminator.DeviceType;
import com.example.tapp.common.discriminator.Gender;
import com.example.tapp.common.discriminator.UserRole;
import com.example.tapp.common.discriminator.UserStatus;
import com.example.tapp.common.dto.UserDto;
import com.example.tapp.common.list.helper.Filter;
import com.example.tapp.common.list.helper.Sort;
import com.example.tapp.common.utils.TokenGenerator;
import com.example.tapp.data.dao.UserDao;
import com.example.tapp.data.entities.StaticPages;
import com.example.tapp.data.entities.User;
import com.example.tapp.data.entities.UserConnection;
import com.example.tapp.data.entities.UserDevice;
import com.example.tapp.data.exception.UserNotFoundException;
import com.example.tapp.data.repository.UserDeviceRepository;
import com.example.tapp.data.repository.UserRepository;

@Repository("userDao")
public class UserDaoImpl extends UtilDao implements UserDao {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private UserDeviceRepository deviceRepo;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public User save(User user) {
        user.setCreatedOn(new Date());
        user.setModifiedOn(new Date());
        user.setDeleted(false);
        user.setRole(UserRole.ROLE_USER);
        user.setStatus(UserStatus.ACTIVE);
        return userRepo.save(user);
    }

    @Override
    public User update(User user) {
        user.setModifiedOn(new Date());
        return userRepo.save(user);
    }

    @Override
    public boolean delete(UUID id) throws UserNotFoundException {
        User user = userRepo.findById(id).orElseThrow(UserNotFoundException::new);
        user.setDeleted(true);
        userRepo.save(user);
        return true;
    }

    @Override
    public User getUserByEmail(String email) {
        Optional<User> userOp = userRepo.findByEmail(email);
        return userOp.isPresent() ? userOp.get() : null;
    }

    @Override
    public List<User> getListforEmail() {
        return userRepo.findByRoleAndStatusIn(UserRole.ROLE_USER, Arrays.asList(UserStatus.ACTIVE, UserStatus.REPORTED),
                org.springframework.data.domain.Sort.by(Direction.DESC, User.EMAIL));
    }

    @Override
    public User getUserById(UUID id) throws UserNotFoundException {
        return userRepo.findById(id).orElseThrow(UserNotFoundException::new);
    }

    @Override
    public void addUserDevice(UUID id, DeviceType type, String deviceToken) throws UserNotFoundException {
        User persist = userRepo.findById(id).orElseThrow(UserNotFoundException::new);
        UserDevice device = null;
        if (persist.getDevice() == null) {
            device = new UserDevice();
            device.setUser(persist);
            persist.setDevice(device);
        } else {
            device = persist.getDevice();
        }
        device.setType(type);
        device.setDeviceToken(deviceToken);
        device.setCreatedOn(new Date());
        device.setModifiedOn(new Date());
        device.setDeleted(false);
        deviceRepo.save(device);
    }

    @Override
    public void removeUserDevice(UUID id) throws UserNotFoundException {
        User persist = userRepo.findById(id).orElseThrow(UserNotFoundException::new);
        if (persist.getDevice() == null)
            return;

        persist.setDevice(null);
        userRepo.save(persist);
    }

    @Override
    public void removeUserToken(UUID id) throws UserNotFoundException {
        User persist = userRepo.findById(id).orElseThrow(UserNotFoundException::new);
        persist.setAuthToken(TokenGenerator.createToken(persist.getId(), persist.getEmail()));
        userRepo.save(persist);
    }

    @Override
    public User getUserByFacebookId(String facebookId) {
        User user = userRepo.findByFacebookId(facebookId).orElse(null);
        if (user == null || user.getStatus().equals(UserStatus.DELETED)) {
            return null;
        }
        return user;
    }

    @Override
    public List<User> getManageUser() {
        return userRepo.findAll(org.springframework.data.domain.Sort.by(Direction.DESC, StaticPages.CREATED_ON));
    }

    @Override
    public Object[] getListByUser(Sort... sorts) {
        Object[] objects = new Object[2];

        // list query
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = builder.createQuery(User.class);
        Root<User> root = criteriaQuery.from(User.class);
        criteriaQuery.select(root);

        applySorting(builder, criteriaQuery, root, sorts);

        TypedQuery<User> query = entityManager.createQuery(criteriaQuery);
        objects[0] = query.getResultList();

        // count query
        CriteriaQuery<Long> countQuery = countQuery(User.class, builder);
        objects[1] = entityManager.createQuery(countQuery).getSingleResult();
        return objects;
    }

    @Override
    public Object[] getListByUserProfile(HashMap<String, Object> filter,Sort... sorts) {
        Object[] objects = new Object[2];

        // list query
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = builder.createQuery(User.class);
        Root<User> root = criteriaQuery.from(User.class);
        criteriaQuery.select(root);

        List<Predicate> predicates = new ArrayList<>();

        Predicate predicateRole = builder.equal(root.get(User.ROLE), UserRole.ROLE_USER);
        predicates.add(predicateRole);

        if (filter != null && !filter.isEmpty()) {
            filter.forEach((key, value) -> {
                if (key.equals(UserDto.STATUS)) {
                    Predicate predicateUserStatus = builder.equal(root.get(User.STATUS),
                            UserStatus.valueOf(value.toString()));
                    predicates.add(predicateUserStatus);
                }
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
        applySorting(builder, criteriaQuery, root, sorts);
        TypedQuery<User> query = entityManager.createQuery(criteriaQuery);

        // applyPage(query, page);
        objects[0] = query.getResultList();

        // count query
        CriteriaQuery<Long> countQuery = countQuery(User.class, builder);
        objects[1] = entityManager.createQuery(countQuery).getSingleResult();
        return objects;
    }

    @Override
    public void deleteManageUser(UUID id) {
        userRepo.deleteById(id);
    }

    @Override
    public Long getUsersCount() {
        return userRepo.countByRoleAndStatus(UserRole.ROLE_USER, UserStatus.ACTIVE);
    }

    @Override
    public Long getMaleUserCount() {
        return userRepo.countByProfileGenderAndStatus(Gender.MALE, UserStatus.ACTIVE);
    }

    @Override
    public Long getFemaleUserCount() {
        return userRepo.countByProfileGenderAndStatus(Gender.FEMALE, UserStatus.ACTIVE);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Object[]> getUserCountByYear(Integer year) {
        Query query = entityManager.createQuery(
                "select count(u) as num, MONTH(u.createdOn) as mon from User u  where YEAR(u.createdOn) = :year AND u.role = :role AND u.status = :status group by MONTH(u.createdOn)");
        query.setParameter("year", year);
        query.setParameter("role", UserRole.ROLE_USER);
        query.setParameter("status", UserStatus.ACTIVE);

        return (List<Object[]>) query.getResultList();
    }

    @Override
    public List<User> getUserNotification() {
        return userRepo.findAll(org.springframework.data.domain.Sort.by(Direction.DESC, StaticPages.CREATED_ON));
    }

    @Override
    public User getSystemUser() {
        return userRepo.findByRole(UserRole.ROLE_SYSTEM).orElse(null);
    }

    @SuppressWarnings("unchecked")
    public List<Object[]> findByIsRead() {
        Query query = entityManager
                .createQuery("select count(u) as num from User u where  (u.isRead = false) ORDER BY u.id desc");
        return (List<Object[]>) query.getResultList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Object[]> findByList() {
        Query query1 = entityManager
                .createQuery("select u.firstName from User u where  (u.isRead = false) ORDER BY u.id desc");
        return (List<Object[]>) query1.getResultList();
    }

    @Override
    @Transactional
    public Object[] getListByIsRead() {
        Object[] objects = new Object[2];
       

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = builder.createQuery(User.class);

        Root<User> root = criteriaQuery.from(User.class);
        criteriaQuery.select(root);
        List<Predicate> predicates = new ArrayList<>();

        Predicate predicateRole = builder.equal(root.get(User.ROLE), UserRole.ROLE_USER);
        predicates.add(predicateRole);
        Predicate predicateRole1 = builder.equal(root.get(User.IsRead), false);
        predicates.add(predicateRole1);

        criteriaQuery.where(builder.and(predicates.toArray(new Predicate[predicates.size()])));
        TypedQuery<User> query = entityManager.createQuery(criteriaQuery);

        objects[0] = query.getResultList();

        // count query
        CriteriaQuery<Long> countQuery = countQuery(User.class, builder);
        objects[1] = entityManager.createQuery(countQuery).getSingleResult();
        return objects;
    }

}
