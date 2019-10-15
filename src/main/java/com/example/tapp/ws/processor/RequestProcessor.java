package com.example.tapp.ws.processor;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import com.example.tapp.business.service.INotification;
import com.example.tapp.business.service.MessageService;
import com.example.tapp.business.service.UserConnectionService;
import com.example.tapp.business.service.UserService;
import com.example.tapp.common.discriminator.MessageStatus;
import com.example.tapp.common.dto.MessageDialogDto;
import com.example.tapp.common.dto.MessageDto;
import com.example.tapp.common.dto.UserDto;
import com.example.tapp.common.list.helper.Page;
import com.example.tapp.common.list.helper.PageResponse;
import com.example.tapp.data.exception.GeneralException;
import com.example.tapp.data.exception.UserNotFoundException;
import com.example.tapp.ws.common.request.PacketName;
import com.example.tapp.ws.common.request.RequestPayload;
import com.example.tapp.ws.common.response.ProcessorResponseUtils;
import com.example.tapp.ws.common.utils.RequestKeys;
import com.example.tapp.ws.manager.SessionManager;
import com.example.tapp.ws.validation.Validation;
import com.fasterxml.jackson.databind.ObjectMapper;
import static com.example.tapp.ws.common.response.ProcessorResponseUtils.success;
import static com.example.tapp.ws.common.response.ProcessorResponseUtils.error;
@Component
public class RequestProcessor {

    private static final Logger log = LoggerFactory.getLogger(RequestProcessor.class);

    HashMap<UUID, UUID> connectedDialog = new HashMap<>();

    BiFunction<HashMap<String, Object>, Class<?>, Object> mapper = (map, domain) -> new ObjectMapper().convertValue(map,
            domain);

    @Autowired
    private UserService userService;

    @Autowired
    private UserConnectionService userConnService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private INotification iNotification;

    public void process(WebSocketSession session, RequestPayload payload) throws Exception {
        // Authentication
        auth(session, payload.getHeader());

        // Process payload
        switch (payload.getPacketName()) {
        case AUTH:
            return;
        case MESSAGE:
            sendMessage(session, payload);
            break;
        case CONVERSATION_LIST:
            conversationList(session, payload);
            break;
        case MESSAGE_LIST:
            messageList(session, payload);
            break;
        case CLOSE_DIALOG:
            closeDialog(session, payload);
            break;
        default:
            break;
        }
    }

    public void closeSession(WebSocketSession session) throws IOException {
        UserDto user = SessionManager.getUserDetails(session.getId());
        if (user == null)
            return;

        sendUserStatus(user, false, session);
        SessionManager.removeSession(session.getId());
        this.connectedDialog.remove(user.getId());
        log.info("Remove Session [" + session.getId() + "]");
    }

    private void startNewSession(WebSocketSession session, String authToken) throws UserNotFoundException, IOException {
        UserDto user = userService.getUserDetailsByToken(authToken);
        user.setAuthToken(null);
        if (SessionManager.checkSession(user.getId())) {
            WebSocketSession _session = SessionManager.getUserSession(user.getId());
            if (_session.isOpen() && !_session.getId().equals(session.getId())) {
                ProcessorResponseUtils.success(_session, PacketName.ANOTHER_DEVICE,
                        "You loggedin with another device.");
                _session.close();
            }

            if (!_session.getId().equals(session.getId())) {

                addSessionAndResponse(user, session);
                sendUserStatus(user, true, session);
                sendPendingMessage(session, user);
            }
        } else {

            addSessionAndResponse(user, session);
            sendUserStatus(user, true, session);
            sendPendingMessage(session, user);
        }
    }

    private void addSessionAndResponse(UserDto user, WebSocketSession session) throws IOException {
        SessionManager.addSession(user, session);
        HashMap<String, Object> sessionInfo = new HashMap<>();
        sessionInfo.put("sessionId", session.getId());
        sessionInfo.put("startTime", System.currentTimeMillis());
        sessionInfo.put("user", user);
        success(session, PacketName.AUTH, sessionInfo);
    }

    private void sendUserStatus(UserDto user, boolean isOnline, WebSocketSession session) throws IOException {
        if (user == null) // check
            return;

        // send user online status to all connected users
        List<UUID> connectedUserIds = userConnService.getConnectedUserIds(user.getId());
        if (connectedUserIds != null && !connectedUserIds.isEmpty()) {
            List<WebSocketSession> sessions = SessionManager.getUserSessionList(connectedUserIds);
            HashMap<String, Object> userInfo = new HashMap<>();
            userInfo.put("user", user);
            userInfo.put("status", isOnline);
            success(sessions, PacketName.USER_STATUS, userInfo);
        }

        // if (isOnline) {
        // // Online User List (CS)
        // List<UserDto> onlineUsers = new ArrayList<>();
        // for (UUID cuId : connectedUserIds) {
        // if (SessionManager.checkSession(cuId)) {
        // WebSocketSession onlineSession = SessionManager.getUserSession(cuId);
        // UserDto onlineUser = SessionManager.getUserDetails(onlineSession.getId());
        // onlineUsers.add(onlineUser);
        // }
        // }
        // if (!onlineUsers.isEmpty()) {
        // success(session, PacketName.ONLINE_USERS, onlineUsers);
        // }
        // }
    }

    private void sendPendingMessage(WebSocketSession session, UserDto dto) {
        messageService.getPendingMessage(dto.getId()).stream().forEach((msg) -> {
            try {
                success(session, PacketName.MESSAGE, msg);
                messageService.messageSent(msg.getId());

                Thread.sleep(200);
            } catch (Exception e) {
                log.info(e.getMessage());
            }
        });
    }

    private void auth(WebSocketSession session, HashMap<String, Object> header) throws GeneralException, IOException {
        if (header == null || header.isEmpty() || header.get(RequestKeys.AUTH_TOKEN) == null) {
            throw new GeneralException("Authentication token is required.");
        }
        try {
            boolean auth = userService.checkToken((String) header.get(RequestKeys.AUTH_TOKEN));
            if (auth) {
                startNewSession(session, (String) header.get(RequestKeys.AUTH_TOKEN));
            } else {
                throw new GeneralException("Authentication failed.");
            }
        } catch (Exception ex) {
            throw new GeneralException("Authentication failed.");
        }

    }

    private void sendMessage(WebSocketSession session, RequestPayload payload) throws IOException, GeneralException {
        MessageDto messageDto = (MessageDto) mapper.apply(payload.getPayload(), MessageDto.class);
        HashMap<String, String> errors = Validation.message(messageDto);
        if (!errors.isEmpty()) {
            error(session, PacketName.MESSAGE, errors);
            return;
        }
        messageDto.setSender(SessionManager.getUserDetails(session.getId()).getId());
        MessageDto dto = messageService.addMessage(messageDto);
        if (dto.getStatus().equals(MessageStatus.NOT_SENDABLE)) {
            /** Send Message details to session owner **/
            success(session, PacketName.MESSAGE, dto);
            return;
        }

        /** Send Message to receiver **/
        WebSocketSession receiverSession = SessionManager.getUserSession(dto.getReceiver());

        if (receiverSession != null && receiverSession.isOpen()) {
            UUID cDialogId = this.connectedDialog.get(dto.getReceiver());
            if (cDialogId == null || !cDialogId.equals(dto.getDialogId())) {
                try {
                    Object[] notifyData = userConnService.getConnByOwnerUserAndConnectedUser(dto.getSender(),
                            dto.getReceiver());
                    iNotification.onMessage(notifyData, dto);
                    System.out.println("Send Push notification");
                } catch (Exception ex) {
                    log.info("Error : {}", ex.getMessage());
                }

            }
            success(receiverSession, PacketName.MESSAGE, dto);

            /** Update Message Status (PENDING -> SENT) **/
            dto = messageService.messageSent(dto.getId());
        }

        /** Send Message details to session owner **/

        success(session, PacketName.MESSAGE, dto);
    }

    private void conversationList(WebSocketSession session, RequestPayload payload) throws IOException {
        UserDto userDto = SessionManager.getUserDetails(session.getId());
        HashMap<String, Object> params = payload.getPayload();
        List<MessageDialogDto> dtos = messageService.conversationList(userDto.getId(), params);

        success(session, PacketName.CONVERSATION_LIST, dtos.stream().map((_dto) -> {
            WebSocketSession _session = SessionManager.getUserSession(_dto.getOccupant().getId());
            if (_session != null && _session.isOpen()) {
                _dto.setOnline(true);
            }
            return _dto;
        }).collect(Collectors.toList()));
    }

    private void messageList(WebSocketSession session, RequestPayload payload) throws IOException {
        HashMap<String, Object> params = payload.getPayload();
        HashMap<String, String> errors = Validation.messageList(params);
        if (!errors.isEmpty()) {
            error(session, PacketName.MESSAGE, errors);
            return;
        }

        UserDto userDto = SessionManager.getUserDetails(session.getId());
        UUID dialogId = UUID.fromString((String) params.get(MessageDialogDto.DIALOG_ID));
        int limit = (Integer) params.get(Page.LIMIT);
        int offset = (Integer) params.get(Page.OFFSET);

        PageResponse<MessageDto> response = messageService.messageThread(dialogId, userDto.getId(),
                Page.of(offset, limit));
        success(session, PacketName.MESSAGE_LIST, response);
        this.connectedDialog.put(userDto.getId(), dialogId);
    }

    private void closeDialog(WebSocketSession session, RequestPayload payload) {
        UserDto userDto = SessionManager.getUserDetails(session.getId());
        this.connectedDialog.remove(userDto.getId());
    }

    @PreDestroy
    public void shutdown() {
        this.connectedDialog.clear();
        SessionManager.closeAll();
    }
}
