package com.example.tapp.business.service.implementation;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.tapp.business.service.ConnectionRequestService;
import com.example.tapp.business.service.INotification;
import com.example.tapp.business.service.UserNotifyService;
import com.example.tapp.common.discriminator.ConnectionRequestStatus;
import com.example.tapp.common.dto.ConnectionRequestDto;
import com.example.tapp.common.dto.RequestOperation;
import com.example.tapp.common.dto.UserDto;
import com.example.tapp.common.list.helper.Filter;
import com.example.tapp.common.list.helper.Order;
import com.example.tapp.common.list.helper.Page;
import com.example.tapp.common.list.helper.PageResponse;
import com.example.tapp.common.list.helper.Sort;
import com.example.tapp.data.dao.UserConnectionDao;
import com.example.tapp.data.dao.UserDao;
import com.example.tapp.data.dao.implementation.ConnectionRequestDaoImpl;
import com.example.tapp.data.entities.ConnectionRequest;
import com.example.tapp.data.entities.User;
import com.example.tapp.data.entities.UserConnection;
import com.example.tapp.data.exception.GeneralException;
import com.example.tapp.data.exception.RecordNotFoundException;
import com.example.tapp.data.exception.UserNotFoundException;

@Service
public class ConnectionRequestServiceImpl implements ConnectionRequestService {

    private static final Logger log = LoggerFactory.getLogger(ConnectionRequestServiceImpl.class);

    @Autowired
    private ConnectionRequestDaoImpl conReqDao;

    @Autowired
    private UserConnectionDao userConnDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserNotifyService notifyService;

    @Autowired
    private INotification iNotify;

    @Override
    public synchronized ConnectionRequestDto add(ConnectionRequestDto dto) throws GeneralException {

        ConnectionRequest conReq = conReqDao.swapGet(dto.getFirstUserId(), dto.getSecondUserId());
        if (conReq != null && conReq.getStatus().equals(ConnectionRequestStatus.ACCEPT))
            throw new GeneralException("Both user already connected!");

        boolean isNew = conReq == null ? true : false;
        if (isNew) {
            conReq = new ConnectionRequest();
            setUserInRequest(dto, conReq);
            conReq.setCreatedOn(new Date());
            conReq.setFirstUserStatus(ConnectionRequestStatus.PENDING);
            conReq.setSecondUserStatus(ConnectionRequestStatus.PENDING);
        }
        conReq.setPlace(dto.getPlace());
        conReq.setLatitude(dto.getLatitude());
        conReq.setLongitude(dto.getLongitude());
        conReq.setStatus(conReq.getStatus() == null ? ConnectionRequestStatus.PENDING : conReq.getStatus());
        ConnectionRequestDto connReqDto = isNew ? conReqDao.save(conReq).dto() : conReqDao.update(conReq).dto();

        // Check for data swap
        if (!connReqDto.getFirstUser().getId().equals(dto.getFirstUserId())) {

            // Swap User
            UserDto temp = connReqDto.getFirstUser();
            connReqDto.setFirstUser(connReqDto.getSecondUser());
            connReqDto.setSecondUser(temp);

            // Swap User Accepted Status
            boolean tempStatus = connReqDto.getFirstUserAccepted();
            connReqDto.setFirstUserAccepted(connReqDto.getSeconUserAccepted());
            connReqDto.setSeconUserAccepted(tempStatus);

        }
        return connReqDto;
    }

    private void setUserInRequest(ConnectionRequestDto dto, ConnectionRequest req) throws GeneralException {
        try {
            User firstUser = userDao.getUserById(dto.getFirstUserId());
            req.setFirstUser(firstUser);
        } catch (UserNotFoundException e) {
            log.info(e.getMessage());
            throw new GeneralException("First user not found.");
        }

        try {
            User secondUser = userDao.getUserById(dto.getSecondUserId());
            req.setSecondUser(secondUser);
        } catch (UserNotFoundException e) {
            log.info(e.getMessage());
            throw new GeneralException("Second user not found.");
        }
    }

    @Override
    public void operation(RequestOperation operation) throws GeneralException {
        try {
            ConnectionRequest conReq = conReqDao.getById(operation.getId());
            if (conReq.getStatus().equals(ConnectionRequestStatus.ACCEPT))
                throw new GeneralException("Request already accepted.");

            if (operation.getOperation().equals(ConnectionRequestStatus.CANCEL)
                    || operation.getOperation().equals(ConnectionRequestStatus.REJECT)) {

                // Send Reject Notification
                iNotify.onConnectionReject(conReq, operation.getId());

                // Delete Connection request data from Database
                conReqDao.delete(conReq);
                return;
            }

            if (conReq.getFirstUser().getId().equals(operation.getBy())) {
                conReq.setFirstUserStatus(operation.getOperation());
                conReq.setFirstUserAccepted(operation.getOperation().equals(ConnectionRequestStatus.ACCEPT));

                // Notify
                notifyService.requestAccepted(conReq.getFirstUser().getId(), conReq.getSecondUser().getId());
            } else if (conReq.getSecondUser().getId().equals(operation.getBy())) {
                conReq.setSecondUserStatus(operation.getOperation());
                conReq.setSecondUserAccepted(operation.getOperation().equals(ConnectionRequestStatus.ACCEPT));

                // Notify
                notifyService.requestAccepted(conReq.getSecondUser().getId(), conReq.getFirstUser().getId());
            }

            if (conReq.getFirstUserStatus().equals(ConnectionRequestStatus.ACCEPT)
                    && conReq.getSecondUserStatus().equals(ConnectionRequestStatus.ACCEPT)) {
                conReq.setStatus(ConnectionRequestStatus.ACCEPT);

                //
                makeConnection(conReq);

            }
            conReqDao.update(conReq);
        } catch (RecordNotFoundException e) {
            log.info(e.getMessage());
            throw new GeneralException(e.getMessage());
        } catch (UserNotFoundException e) {
            log.info(e.getMessage());
            throw new GeneralException(e.getMessage());
        }
    }

    private void makeConnection(ConnectionRequest connReq) throws UserNotFoundException {

        User fUser = userDao.getUserById(connReq.getFirstUser().getId());
        User sUser = userDao.getUserById(connReq.getSecondUser().getId());

        UserConnection fConnection = createConnection(fUser, sUser);
        fConnection.setRequestReference(connReq);

        UserConnection sConnection = createConnection(sUser, fUser);
        sConnection.setRequestReference(connReq);

        userConnDao.save(fConnection);
        userConnDao.save(sConnection);

        iNotify.onConnectionAccept(fConnection);
        iNotify.onConnectionAccept(sConnection);

    }

    private UserConnection createConnection(User fUser, User sUser) {
        UserConnection connection = new UserConnection();
        connection.setOwnerUser(fUser);
        connection.setConnectedUser(sUser);
        connection.setStatus(ConnectionRequestStatus.ACTIVE);
        connection.setProfileVisited(false);
        fUser.getConnections().add(connection);
        sUser.getConnected().add(connection);
        return connection;
    }

    @SuppressWarnings("unchecked")
    @Override
    public PageResponse<?> getList(UUID userId, HashMap<String, Object> options) {
        Page page = (Page) options.get(Page.PAGE);
        Sort sort = Sort.of(Order.DESC, ConnectionRequest.CREATED_ON);
        HashMap<String, Object> filters = (HashMap<String, Object>) options.get(Filter.FILTER);
        Object[] objects = conReqDao.getListByUserId(userId, ConnectionRequestStatus.PENDING, filters, page, sort);

        PageResponse<ConnectionRequestDto> pageResponse = new PageResponse<>();
        pageResponse.setTotal((long) objects[1], page);
        pageResponse.setList(((List<ConnectionRequest>) objects[0]).stream().map(cr -> convert(cr, userId))
                .collect(Collectors.toList()));
        return pageResponse;

    }

    public ConnectionRequestDto convert(ConnectionRequest connReq, UUID userId) {
        ConnectionRequestDto connReqDto = new ConnectionRequestDto();
        connReqDto.setId(connReq.getId());
        connReqDto.setTime(connReq.getCreatedOn().getTime());
        connReqDto.setPlace(connReq.getPlace());
        connReqDto.setLatitude(connReq.getLatitude());
        connReqDto.setLongitude(connReq.getLongitude());
        User user = null;
        if (connReq.getFirstUser().getId().equals(userId)) {
            user = connReq.getSecondUser();
            connReqDto.setUserAccepted(connReq.getFirstUserStatus().equals(ConnectionRequestStatus.ACCEPT));
        } else {
            user = connReq.getFirstUser();
            connReqDto.setUserAccepted(connReq.getSecondUserStatus().equals(ConnectionRequestStatus.ACCEPT));
        }
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setProfileImage(user.getProfile().getProfileImageName());
        connReqDto.setUser(dto);
        return connReqDto;
    }

    @Override
    @Transactional
    public void createRequestForSystemUser(UUID userId) {
        User systemUser = userDao.getSystemUser();
        try {
            User genUser = userDao.getUserById(userId);
            ConnectionRequest conReq = conReqDao.swapGet(systemUser.getId(), genUser.getId());
            if (conReq != null)
                return;

            conReq = new ConnectionRequest();
            conReq.setFirstUser(systemUser);
            conReq.setSecondUser(genUser);
            conReq.setLatitude(new BigDecimal(0));
            conReq.setLongitude(new BigDecimal(0));
            conReq.setStatus(ConnectionRequestStatus.ACCEPT);
            conReq.setFirstUserStatus(ConnectionRequestStatus.ACCEPT);
            conReq.setSecondUserStatus(ConnectionRequestStatus.ACCEPT);
            conReq.setFirstUserAccepted(true);
            conReq.setSecondUserAccepted(true);
            conReq.setPlace("");
            // Save connection request
            conReqDao.save(conReq);

            // Create Connection
            makeConnection(conReq);

            UserConnection connection = userConnDao.getByOwnerAndConnectedUser(systemUser.getId(), genUser.getId());
            connection.setStatus(ConnectionRequestStatus.BLOCKED);
            userConnDao.save(connection);
        } catch (UserNotFoundException | RecordNotFoundException ex) {
            log.info("Error : " + ex.getMessage());
        }
    }

}

