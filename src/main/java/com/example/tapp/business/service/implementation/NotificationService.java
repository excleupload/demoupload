package com.example.tapp.business.service.implementation;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import com.amazonaws.services.sns.AmazonSNS;
import com.example.tapp.business.service.INotification;
import com.example.tapp.common.discriminator.ConnectionRequestStatus;
import com.example.tapp.common.discriminator.DeviceType;
import com.example.tapp.common.dto.MessageDto;
import com.example.tapp.common.dto.Push;
import com.example.tapp.common.dto.UserDto;
import com.example.tapp.common.utils.YamlPropertySourceFactory;
import com.example.tapp.data.entities.ConnectionRequest;
import com.example.tapp.data.entities.User;
import com.example.tapp.data.entities.UserConnection;
import com.example.tapp.data.entities.UserDevice;
import com.example.tapp.data.repository.UserRepository;
import com.example.tapp.library.Notification;
import com.example.tapp.library.PushNotification;

@Service("pushNotification")
@PropertySource(factory = YamlPropertySourceFactory.class, value = "classpath:utils.yml")
public class NotificationService implements INotification {

    private static final Logger log = LoggerFactory.getLogger(Notification.class.getName());

    @Value("${tapp.application.push-notify.title}")
    private String NOTIFICATION_TITLE;

    @Autowired
    private AmazonSNS amazonSns;

    @Autowired
    private UserRepository userRepository;

    private Notification notification;

    @PostConstruct
    public void init() {
        notification = new PushNotification(amazonSns);
    }

    @Override
    public void onMessage(Object[] notifyData, MessageDto message) {
        try {
            UserDto ownerUser = (UserDto) notifyData[0];
            UserDto connecteduser = (UserDto) notifyData[1];
            ConnectionRequestStatus status = (ConnectionRequestStatus) notifyData[2];
            new Thread(() -> {
                try {
                    Push push = createPushForMessage(ownerUser, status, message);
                    sendNotification(connecteduser.getId(), push);
                } catch (Exception ex) {
                    log.info("ERROR : " + ex.getMessage());
                    ex.printStackTrace();
                }
            }).start();
        } catch (Exception ex) {
            log.info("ERROR : " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @Override
    public void onConnectionAccept(UserConnection connection) {
        new Thread(() -> {
            Push push = this.createPushConnectionAccept(connection);
            this.sendNotification(connection.getConnectedUser().getId(), push);
        }).start();
    }

    @Override
    public void onConnectionReject(ConnectionRequest request, UUID cUserId) {
        new Thread(() -> {
            Push push = this.createPushConnectionReject(request, cUserId);
            User user = request.getFirstUser().getId().equals(cUserId) ? request.getSecondUser()
                    : request.getFirstUser();
            this.sendNotification(user.getId(), push);
        }).start();
    }

    private void sendNotification(UUID userid, Push push) {
        try {

            Optional<User> userOp = userRepository.findById(userid);
            if (userOp.isPresent()) {
                UserDevice device = userOp.get().getDevice();
                if (device != null) {
                    if (device.getType().equals(DeviceType.ANDROID)) {
                        if (notification.android(device.getDeviceToken(), push) != null) {
                            //log.log(Level.INFO, "Send push notification on android device.");
                        }
                    } else if (device.getType().equals(DeviceType.IOS)) {
                        if (notification.apple(device.getDeviceToken(), push) != null) {
                            //log.log(Level.INFO, "Send push notification on Ios device.");
                        }
                    }
                }
            }
        } catch (Exception ex) {
            //log.log(Level.INFO, "Push Notification error : " + ex.getMessage());
        }
    }

    private Push createPushForMessage(UserDto senderUser, ConnectionRequestStatus status, MessageDto messageDto) {
        Push push = new Push();
        push.setType(Push.MESSAGE);
        push.setTitle(NOTIFICATION_TITLE);
        push.setTime(System.currentTimeMillis());

        if (messageDto.getType().equals(com.example.tapp.common.discriminator.MessageType.TEXT)) {
            String temp = messageDto.getMessage().length() <= 15 ? messageDto.getMessage()
                    : messageDto.getMessage().substring(0, 14);
            String message = String.format("%s %s : %s", senderUser.getFirstName(), senderUser.getLastName(), temp);
            push.setMessage(message);
        } else if (messageDto.getType().equals(com.example.tapp.common.discriminator.MessageType.IMAGE)) {
            String message = String.format("%s %s : %s", senderUser.getFirstName(), senderUser.getLastName(), "Image");
            push.setMessage(message);
        }

        HashMap<String, Object> appData = new HashMap<>();
        appData.put(MessageDto.DIALOG_ID, messageDto.getDialogId());
        appData.put(UserConnection.STATUS, status);
        push.setAppData(appData);
        return push;
    }

    private Push createPushConnectionAccept(UserConnection connection) {
        Push push = new Push();
        push.setType(Push.CONNECTION_REQUEST_ACCEPT);
        push.setTitle(NOTIFICATION_TITLE);
        push.setTime(System.currentTimeMillis());

        User user = connection.getOwnerUser();
        String message = String.format("%s %s just accepted your request and you are a connection now.",
                user.getFirstName(), user.getLastName());
        push.setMessage(message);

        HashMap<String, Object> appData = new HashMap<>();
        appData.put(UserConnection.ID, connection.getId());
        appData.put(UserConnection.OWNER_USER, connection.getOwnerUser().getId());
        appData.put(UserConnection.CONNECTED_USER, connection.getConnectedUser().getId());
        appData.put(UserConnection.STATUS, connection.getStatus());
        push.setAppData(appData);
        return push;
    }

    private Push createPushConnectionReject(ConnectionRequest request, UUID cUserId) {
        Push push = new Push();
        push.setType(Push.CONNECTION_REQUEST_REJECT);
        push.setTitle(NOTIFICATION_TITLE);
        push.setTime(System.currentTimeMillis());
        push.setMessage("Conection request rejected.");

        HashMap<String, Object> appData = new HashMap<>();
        appData.put(ConnectionRequest.ID, request.getId());
        push.setAppData(appData);

        return push;
    }

}

