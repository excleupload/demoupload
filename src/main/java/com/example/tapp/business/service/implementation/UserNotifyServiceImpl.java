package com.example.tapp.business.service.implementation;

import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.example.tapp.business.service.UserNotifyService;
import com.example.tapp.common.dto.UserDto;
import com.example.tapp.common.dto.UserNotificationDto;
import com.example.tapp.data.dao.UserDao;
import com.example.tapp.data.dao.UserNotificationDao;
import com.example.tapp.data.entities.User;
import com.example.tapp.data.entities.UserNotification;
import com.example.tapp.data.exception.RecordNotFoundException;
import com.example.tapp.data.exception.UserNotFoundException;

@Service
public class UserNotifyServiceImpl implements UserNotifyService {

    private static final Logger log = LoggerFactory.getLogger(UserNotifyServiceImpl.class);

    private static final String MSG_REQ_ACCEPTED = "connection.request.accepted";

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserNotificationDao notifyDao;

    @Override
    public void requestAccepted(UUID senderId, UUID receiverId) {
        CompletableFuture.runAsync(() -> {
            try {
                User _sender = userDao.getUserById(senderId);
                User _receiver = userDao.getUserById(receiverId);

                Object[] args = new Object[2];
                // args[0] = _sender.getFirstName();
                // args[1] = _sender.getLastName();
               
                String message = messageSource.getMessage(MSG_REQ_ACCEPTED, args, "", Locale.US);
                UserNotification notification = new UserNotification();
                notification.setUserId(_receiver.getId());
                notification.setSenderId(_sender.getId());
                notification.setMessage(message);
                notification.setSent(false);
                notifyDao.save(notification);
            } catch (UserNotFoundException e) {
                log.info(e.getMessage());
            }
        });
    }

    @Override
    public List<UserNotificationDto> list(UUID userId) {
        return notifyDao.list(userId).stream().map((notify) -> {
            UserNotificationDto dto = new UserNotificationDto();
            dto.setId(notify.getId());
            dto.setMessage(notify.getMessage());
            dto.setTime(notify.getCreatedOn().getTime());

            try {
                UserDto sender = userDao.getUserById(notify.getSenderId()).toDto();
                sender.setAuthToken(null);
                dto.setSender(sender);
            } catch (UserNotFoundException e) {
                log.info(e.getMessage());
            }
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public void clear(UUID userId, UUID lastNotify) {
        try {
            UserNotification persist = notifyDao.getByuserIdAndId(userId, lastNotify);
            notifyDao.clear(persist);
        } catch (RecordNotFoundException e) {
            log.info(e.getMessage());
        }
    }
}