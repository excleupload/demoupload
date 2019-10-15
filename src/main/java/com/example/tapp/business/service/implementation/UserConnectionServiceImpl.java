package com.example.tapp.business.service.implementation;

import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.Filter;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import com.example.tapp.business.service.UserConnectionService;
import com.example.tapp.common.discriminator.ConnectionRequestStatus;
import com.example.tapp.common.dto.UserConnectionDto;
import com.example.tapp.common.dto.UserDto;
import com.example.tapp.common.list.helper.Order;
import com.example.tapp.common.list.helper.Page;
import com.example.tapp.common.list.helper.PageResponse;
import com.example.tapp.data.dao.ConnectionRequestDao;
import com.example.tapp.data.dao.MessageDao;
import com.example.tapp.data.dao.MessageDialogDao;
import com.example.tapp.data.dao.UserConnectionDao;
import com.example.tapp.data.entities.ConnectionRequest;
import com.example.tapp.data.entities.MessageDialog;
import com.example.tapp.data.entities.User;
import com.example.tapp.data.entities.UserConnection;
import com.example.tapp.data.exception.RecordNotFoundException;
import com.example.tapp.ws.common.request.PacketName;
import com.example.tapp.ws.common.response.ProcessorResponseUtils;
import com.example.tapp.ws.manager.SessionManager;

@Service
public class UserConnectionServiceImpl implements UserConnectionService {

    private static final Logger log = LoggerFactory.getLogger(UserConnectionServiceImpl.class);

    @Autowired
    private UserConnectionDao connDao;

    @Autowired
    private MessageDialogDao dialogDao;

    @Autowired
    private MessageDao messageDao;

    @Autowired
    private ConnectionRequestDao connReqDao;

    @SuppressWarnings("unchecked")
    @Override
    public PageResponse<?> getListByUserId(UUID userId, HashMap<String, Object> options) {
        Page page = (Page) options.get(Page.PAGE);
        HashMap<String, Object> filter = (HashMap<String, Object>) options.get(com.example.tapp.common.list.helper.Filter.FILTER);
        Object[] objects = connDao.getListByOwnerId(userId, ConnectionRequestStatus.ACTIVE, filter, page);
        PageResponse<UserConnectionDto> pageResponse = new PageResponse<>();
        pageResponse.setTotal((long) objects[1], page);
        pageResponse
                .setList(((List<UserConnection>) objects[0]).stream().map(this::conert).collect(Collectors.toList()));
        return pageResponse;
    }

    private UserConnectionDto conert(UserConnection connection) {
        UserConnectionDto dto = new UserConnectionDto();
        dto.setId(connection.getId());
        dto.setTime(connection.getRequestReference().getCreatedOn().getTime());
        dto.setPlace(connection.getRequestReference().getPlace());
        dto.setLongitude(connection.getRequestReference().getLongitude());
        dto.setLatitude(connection.getRequestReference().getLatitude());
        UserDto userDto = new UserDto();
        userDto.setId(connection.getConnectedUser().getId());
        userDto.setFirstName(connection.getConnectedUser().getFirstName());
        userDto.setLastName(connection.getConnectedUser().getLastName());
        userDto.setProfileImage(connection.getConnectedUser().getProfile().getProfileImageName());
        dto.setUser(userDto);
        dto.setDialogId(connection.getMessageDialogId() == null ? "" : connection.getMessageDialogId().toString());
        dto.setStatus(connection.getStatus());
        dto.setVisited(connection.isProfileVisited());

        try {
            UserConnection userConnection = connDao.getByOwnerAndConnectedUser(connection.getConnectedUser().getId(),
                    connection.getOwnerUser().getId());
            dto.setSecondUserStatus(userConnection.getStatus());
        } catch (RecordNotFoundException e) {
            e.printStackTrace();
        }
        return dto;
    }

    @Override
    public List<UserDto> getListUserNameByUserId(UUID userId) {
        com.example.tapp.common.list.helper.Sort sort = com.example.tapp.common.list.helper.Sort.of(Order.ASC, UserConnection.OWNER_USER + "." + User.FIRST_NAME);
        List<Object[]> objects = connDao.getListofUserName(userId, ConnectionRequestStatus.ACTIVE, sort);
        List<UserDto> list = objects.stream().map(this::convertObjToUserDto).collect(Collectors.toList());
        list.sort(Comparator.comparing(UserDto::getFirstName));
        return list;
    }

    private UserDto convertObjToUserDto(Object[] objects) {
        UserDto dto = new UserDto();
        dto.setId((UUID) objects[0]);
        dto.setFirstName((String) objects[1]);
        dto.setLastName((String) objects[2]);
        return dto;
    }

    @Override
    public List<UUID> getConnectedUserIds(UUID userId) {
        return connDao.getConnectedUserIdByOwner(userId, ConnectionRequestStatus.ACTIVE).stream().map((id) -> (UUID) id)
                .collect(Collectors.toList());
    }

    @Override
    public void removeConnection(UUID connectionId) throws RecordNotFoundException {
        UserConnection persist = connDao.getById(connectionId);

        try {
            if (persist.getMessageDialogId() != null) {
                // remove message dialog
                MessageDialog persistDialog = dialogDao.getDialoagById(persist.getMessageDialogId());

                // Clear All Messages
                messageDao.deleteMessage(persistDialog.getId(), new Date());

                // delete dialog
                dialogDao.deleteDialog(persistDialog);
            }

        } catch (Exception ex) {
            log.info(ex.getMessage());
        }

        UserConnection secondConn = connDao.getByOwnerAndConnectedUser(persist.getConnectedUser().getId(),
                persist.getOwnerUser().getId());

        connDao.delete(persist);
        connDao.delete(secondConn);

        // remove connection request
        ConnectionRequest connReq = connReqDao.getById(persist.getRequestReference().getId());
        connReqDao.delete(connReq);

        // Send data from socket
        sendRemoveConnectionStatus(secondConn);
    }

    @Override
    public void changeStatus(UUID connId, ConnectionRequestStatus status) throws RecordNotFoundException {
        UserConnection connection = connDao.getById(connId);
        connection.setStatus(status);
        connDao.update(connection);
    }

    private void sendRemoveConnectionStatus(UserConnection connection) {
        User statusReceiver = connection.getOwnerUser();

        WebSocketSession session = SessionManager.getUserSession(statusReceiver.getId());
        if (session == null || !session.isOpen())
            return;

        try {
            UserConnectionDto dto = new UserConnectionDto();
            dto.setId(connection.getId());

            UserDto userDto = connection.getOwnerUser().toDto();
            userDto.setAuthToken("");
            dto.setUser(userDto);
            ProcessorResponseUtils.success(session, PacketName.REMOVE_CONNECTION, dto);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    @Transactional
    public Object[] getConnByOwnerUserAndConnectedUser(UUID oId, UUID cId) throws RecordNotFoundException {
        Object[] objects = new Object[3];
        UserConnection connection = connDao.getByOwnerAndConnectedUser(oId, cId);
        objects[0] = connection.getOwnerUser().toDto();
        objects[1] = connection.getConnectedUser().toDto();
        objects[2] = connection.getStatus();
        return objects;
    }

}
