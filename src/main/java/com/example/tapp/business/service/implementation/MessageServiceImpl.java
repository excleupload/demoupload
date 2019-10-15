package com.example.tapp.business.service.implementation;

import java.util.Collections;
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

import com.example.tapp.business.service.MessageService;
import com.example.tapp.common.discriminator.ConnectionRequestStatus;
import com.example.tapp.common.discriminator.MessageStatus;
import com.example.tapp.common.dto.MessageDialogDto;
import com.example.tapp.common.dto.MessageDto;
import com.example.tapp.common.dto.UserDto;
import com.example.tapp.common.list.helper.Order;
import com.example.tapp.common.list.helper.Page;
import com.example.tapp.common.list.helper.PageResponse;
import com.example.tapp.common.list.helper.Sort;
import com.example.tapp.data.dao.MessageDao;
import com.example.tapp.data.dao.MessageDialogDao;
import com.example.tapp.data.dao.UserConnectionDao;
import com.example.tapp.data.dao.UserDao;
import com.example.tapp.data.entities.ConnectionRequest;
import com.example.tapp.data.entities.Message;
import com.example.tapp.data.entities.MessageClear;
import com.example.tapp.data.entities.MessageDialog;
import com.example.tapp.data.entities.User;
import com.example.tapp.data.entities.UserConnection;
import com.example.tapp.data.exception.GeneralException;
import com.example.tapp.data.exception.RecordNotFoundException;
import com.example.tapp.data.exception.UserNotFoundException;

@Service
public class MessageServiceImpl implements MessageService {

    private static final Logger log = LoggerFactory.getLogger(MessageServiceImpl.class);

    @Autowired
    private MessageDialogDao dialogDao;

    @Autowired
    private MessageDao messageDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserConnectionDao connDao;

    @Override
    public MessageDialogDto create(MessageDialogDto dto) throws GeneralException {
        try {
            if (dto.getOccupantIds() == null || dto.getOccupantIds().isEmpty() || dto.getOccupantIds().size() != 2)
                throw new GeneralException("OccupantIds is empty or it must be contains 2 value.");

            UUID ownerId = dto.getOccupantIds().get(0);
            UUID connectedUserId = dto.getOccupantIds().get(1);
            UserConnection connection = connDao.getByOwnerAndConnectedUser(ownerId, connectedUserId);
            if (connection == null) {
                throw new GeneralException("Both users are not connected.");
            }

            MessageDialog dialog = null;
            if (connection.getMessageDialogId() == null) {
                dialog = new MessageDialog();
                for (UUID occupantId : dto.getOccupantIds()) {
                    dialog.getOccupants().add(userDao.getUserById(occupantId));
                }
                dialogDao.save(dialog);
                updateConnection(dto.getOccupantIds(), dialog.getId());
            } else {
                dialog = dialogDao.getDialoagById(connection.getMessageDialogId());
            }

            return toDto(dialog);
        } catch (Exception ex) {
            log.info(ex.getMessage());
            throw new GeneralException(ex.getMessage());
        }
    }

    private void updateConnection(List<UUID> occupatIds, UUID dialogId) throws RecordNotFoundException {
        UUID first = occupatIds.get(0);
        UUID second = occupatIds.get(1);

        //
        UserConnection connection = connDao.getByOwnerAndConnectedUser(first, second);
        connection.setMessageDialogId(dialogId);
        connDao.update(connection);
        //
        connection = connDao.getByOwnerAndConnectedUser(second, first);
        connection.setMessageDialogId(dialogId);
        connDao.update(connection);
    }

    @Override
    @Transactional
    public MessageDto addMessage(MessageDto messageDto) throws GeneralException {
        try {

            MessageDialog dialog = dialogDao.getDialoagById(messageDto.getDialogId());

            Message message = new Message();
            message.setDialog(dialog);
            message.setMessage(messageDto.getMessage());
            //message.setType(messageDto.getType());
            dialog.getOccupants().stream().forEach((occupants) -> {
                if (occupants.getId().equals(messageDto.getSender())) {
                    message.setSenderId(occupants.getId());
                } else {
                    message.setReceiverId(occupants.getId());
                }
            });

            UserConnection connection = connDao.getByOwnerAndConnectedUser(message.getReceiverId(),
                    message.getSenderId());
            message.setStatus(connection.getStatus().equals(ConnectionRequestStatus.BLOCKED) ? MessageStatus.NOT_SENDABLE
                    : MessageStatus.PENDING);

            messageDao.save(message);

            // Update Dialog
            dialog.setLastMessage(message.getMessage());
            dialog.setLastMessageUserId(message.getSenderId());
            dialogDao.update(dialog);
            MessageDto dto = MessageDto(message);

            User senderUser = userDao.getUserById(dto.getSender());
            dto.setSenderProfileImage(senderUser.getProfile().getProfileImageName());

            User receiverUser = userDao.getUserById(dto.getReceiver());
            dto.setReceiverProfileImage(receiverUser.getProfile().getProfileImageName());
            return dto;

        } catch (RecordNotFoundException | UserNotFoundException e) {
            log.info(e.getMessage());
            throw new GeneralException(e.getMessage());
        }
    }

    @Override
    @Transactional
    public MessageDto messageSent(UUID messageId) throws GeneralException {
        try {
            Message message = messageDao.getMessageById(messageId);
            message.setStatus(MessageStatus.SENT);
            MessageDto dto = messageDto(messageDao.update(message));

            User senderUser = userDao.getUserById(dto.getSender());
            dto.setSenderProfileImage(senderUser.getProfile().getProfileImageName());

            User receiverUser = userDao.getUserById(dto.getReceiver());
            dto.setReceiverProfileImage(receiverUser.getProfile().getProfileImageName());

            return dto;
        } catch (RecordNotFoundException | UserNotFoundException e) {
            log.info(e.getMessage());
            throw new GeneralException(e.getMessage());
        }
    }

    public MessageDialogDto ConversionList(MessageDialogDto dto) throws GeneralException {
        try {
            MessageDialog dialog = dialogDao.getDialoagById(dto.getDialogId());
            return toDto(dialog);
        } catch (Exception ex) {
            log.info(ex.getMessage());
            throw new GeneralException(ex.getMessage());
        }
    }

    @Override
    @Transactional
    public List<MessageDialogDto> conversationList(UUID userId, HashMap<String, Object> filter) {
        List<Object> dialogIds = connDao.getConnectedUsersDialogId(userId, ConnectionRequestStatus.ACTIVE, filter);

        Sort sort = Sort.of(Order.DESC, ConnectionRequest.MODIFIED_ON);
        List<UUID> _dialogIds = dialogIds.stream().map((obj) -> (UUID) obj).collect(Collectors.toList());
        return dialogDao.getMessageDialogList(_dialogIds, sort).stream().map((dialogs) -> {

            // Map entity to DTO
            MessageDialogDto dto = new MessageDialogDto();
            dto.setDialogId(dialogs.getId());
            dto.setLastMessageTime(dialogs.getModifiedOn().getTime());

            Date clearOn = getLastClearTime(dialogs.getId(), userId);
            String lastMessage = "";
            if (clearOn == null || dialogs.getModifiedOn().after(clearOn)) {
                lastMessage = dialogs.getLastMessage() == null ? "" : dialogs.getLastMessage();
            }
            dto.setLastMessage(lastMessage);

            UserDto userDto = dialogs.getOccupants().stream().filter((occupant) -> !occupant.getId().equals(userId))
                    .findFirst().orElse(null).toDto();
            userDto.setAuthToken(null);
            dto.setOccupant(userDto);

            try {
                UserConnection connection = connDao.getByOwnerAndConnectedUser(userId, userDto.getId());
                dto.setStatus(connection.getStatus());
            } catch (RecordNotFoundException e) {
                e.printStackTrace();
            }

            // Set Last message when connection is BLOCKED.
            if (dto.getStatus().equals(ConnectionRequestStatus.BLOCKED)) {
                Message message = messageDao.getLastMessage(userId);
                dto.setLastMessage(message == null ? "" : message.getMessage());
            }

            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<MessageDto> getPendingMessage(UUID userId) {
        return messageDao.pendingMessage(userId).stream().map(message -> {
            MessageDto dto = this.messageDto(message);
            try {
                User senderUser = userDao.getUserById(dto.getSender());
                dto.setSenderProfileImage(senderUser.getProfile().getProfileImageName());

                User receiverUser = userDao.getUserById(dto.getReceiver());
                dto.setReceiverProfileImage(receiverUser.getProfile().getProfileImageName());
            } catch (UserNotFoundException e) {
                log.info("ERROR : {}", e.getMessage());
                e.printStackTrace();
            }

            return dto;
        }).collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional
    public PageResponse<MessageDto> messageThread(UUID dialogId, UUID userId, Page page) {

        MessageClear clear = dialogDao.getMessageClear(dialogId, userId);
        long count = messageDao.listCount(dialogId, clear == null ? null : clear.getClearOn(), userId);

        if (count == 0) {
            PageResponse<MessageDto> pageResponse = new PageResponse<>();
            pageResponse.setTotal(0);
            pageResponse.setList(Collections.emptyList());
            return pageResponse;
        }

        int limit = page.limit();
        int offset = (int) (count - (limit * page.offset()));
        if (offset < 0) {
            limit += offset;
            offset = 0;
        }
        Page _page = Page.of(offset, limit);
        Object[] objects = messageDao.list(dialogId, clear == null ? null : clear.getClearOn(), userId, _page);
        PageResponse<MessageDto> pageResponse = new PageResponse<>();
        pageResponse.setTotal((long) objects[1], page);
        List<MessageDto> list = ((List<Message>) objects[0]).stream().map(message -> {
            MessageDto dto = this.messageDto(message);
            try {
                User senderUser = userDao.getUserById(dto.getSender());
                dto.setSenderProfileImage(senderUser.getProfile().getProfileImageName());

                User receiverUser = userDao.getUserById(dto.getReceiver());
                dto.setReceiverProfileImage(receiverUser.getProfile().getProfileImageName());
            } catch (Exception ex) {
                log.info("Error : {}", ex.getMessage());
            }
            return dto;
        }).collect(Collectors.toList());
        pageResponse.setList(list);
        return pageResponse;
    }

    @Override
    public void dialogClear(UUID dialogId, UUID occupantId) {
        MessageClear clear = dialogDao.getMessageClear(dialogId, occupantId);
        boolean isNew = clear == null ? true : false;
        clear = isNew ? new MessageClear() : clear;

        clear.setClearOn(new Date());
        if (isNew) {
            clear.setDialogId(dialogId);
            clear.setOccupantId(occupantId);
            dialogDao.dialogClearSave(clear);
        }
        dialogDao.dialogClearUpdate(clear);

        // Background Process
        clearMessage(dialogId);
    }

    @Override
    public Date getLastClearTime(UUID dialogId, UUID occupantId) {
        MessageClear clear = dialogDao.getMessageClear(dialogId, occupantId);
        return clear == null ? null : clear.getClearOn();
    }

    private void clearMessage(UUID dialogId) {
        new Thread(() -> {
            try {
                MessageDialog dialog = dialogDao.getDialoagById(dialogId);
                List<UUID> occupantIds = dialog.getOccupants().stream().map((occupants) -> occupants.getId())
                        .collect(Collectors.toList());
                MessageClear fClear = dialogDao.getMessageClear(dialogId, occupantIds.get(0));
                MessageClear sClear = dialogDao.getMessageClear(dialogId, occupantIds.get(1));
                if (fClear != null & sClear != null) {
                    Date clearDate = fClear.getClearOn().before(sClear.getClearOn()) ? fClear.getClearOn()
                            : sClear.getClearOn();
                    messageDao.deleteMessage(dialogId, clearDate);
                }
            } catch (Exception ex) {
                log.info(ex.getMessage());
            }
        }).start();
    }
   
}
