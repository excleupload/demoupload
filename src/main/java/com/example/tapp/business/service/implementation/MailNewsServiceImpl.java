package com.example.tapp.business.service.implementation;

import static com.example.tapp.ws.common.response.ProcessorResponseUtils.success;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import com.amazonaws.services.sns.AmazonSNS;
import com.example.tapp.business.service.INotification;
import com.example.tapp.business.service.MailNewsService;
import com.example.tapp.business.service.MessageService;
import com.example.tapp.business.service.UserConnectionService;
import com.example.tapp.common.dto.MessageDialogDto;
import com.example.tapp.common.dto.MessageDto;
import com.example.tapp.common.dto.NotificationDto;
import com.example.tapp.common.notification.MailNotification;
import com.example.tapp.data.dao.UserConnectionDao;
import com.example.tapp.data.dao.UserDao;
import com.example.tapp.data.entities.User;
import com.example.tapp.data.entities.UserConnection;
import com.example.tapp.data.repository.UserRepository;
import com.example.tapp.ws.common.request.PacketName;
import com.example.tapp.ws.manager.SessionManager;

/**
 * @author paras
 *
 */
@Service
public class MailNewsServiceImpl implements MailNewsService {

    private static final Logger log = LoggerFactory.getLogger(MailNewsServiceImpl.class);

    public static final String EMAILS = "emails";
    public static final String TITLE = "title";
    public static final String CONTENT = "content";

    @Autowired
    private MailNotification mailNotify;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserConnectionDao connDao;

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserConnectionService userConnService;

    @Autowired
    private INotification iNotification;

    // private Notification notification;

    @Autowired
    AmazonSNS amazonSNS;

    // @PostConstruct
    // public void init() {
    // notification = new PushNotification(amazonSNS);
    // }

    @Override
    public boolean sendmail(NotificationDto newsMail) {
        new Thread(() -> {
            newsMail.getEmails().parallelStream().forEach((email) -> {
                mailNotify.sendMailForNotication(email, newsMail.getSubject(), newsMail.getBody());
                log.info(email + " : SUCCESS");
            });
        }).start();
        return true;
    }

    @Override
    public boolean sendPush(NotificationDto pushNotification) {
        for (UUID userId : pushNotification.getUserIds()) {
            Optional<User> userOp = userRepository.findById(userId);
            if (userOp.isPresent()) {
                User user = userOp.get();
                System.out.println(user.getFirstName() + " " + user.getLastName());
                this.sendSystemMessage(userOp.get(), pushNotification.getBody());
            }
        }
        return true;
    }

    private void sendSystemMessage(User user, String body) {
        User sysUser = userDao.getSystemUser();
        User receiver = user;
        try {
            UserConnection connection = connDao.getByOwnerAndConnectedUser(sysUser.getId(), receiver.getId());
            UUID dialogId = null;
            if (connection.getMessageDialogId() == null) {
                MessageDialogDto dialogDto = new MessageDialogDto();
                dialogDto.setOccupantIds(Arrays.asList(sysUser.getId(), receiver.getId()));
                dialogId = messageService.create(dialogDto).getDialogId();
            }else {
                dialogId = connection.getMessageDialogId();
            }

            MessageDto messageDto = new MessageDto();
            messageDto.setDialogId(dialogId);
            messageDto.setType(com.example.tapp.common.discriminator.MessageType.TEXT);
            messageDto.setMessage(body);
            messageDto.setSender(sysUser.getId());
            messageDto = messageService.addMessage(messageDto);

            /** Send Message to receiver **/
            WebSocketSession receiverSession = SessionManager.getUserSession(messageDto.getReceiver());
            if (receiverSession != null && receiverSession.isOpen()) {
                Object[] notifyData = userConnService.getConnByOwnerUserAndConnectedUser(messageDto.getSender(),
                        messageDto.getReceiver());
                iNotification.onMessage(notifyData, messageDto);
                success(receiverSession, PacketName.MESSAGE, messageDto);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("Error {}", e.getMessage());
        }
    }

	

}
