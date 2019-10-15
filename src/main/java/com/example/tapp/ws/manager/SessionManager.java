package com.example.tapp.ws.manager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import com.example.tapp.common.dto.UserDto;

@Component
public class SessionManager {

    private static final Logger log = LoggerFactory.getLogger(SessionManager.class);

    static HashMap<UUID, WebSocketSession> socketSession = new HashMap<>();

    static HashMap<String, UserDto> userDetails = new HashMap<>();

    public static synchronized void addSession(UserDto user, WebSocketSession session) {
        socketSession.put(user.getId(), session);
        userDetails.put(session.getId(), user);
    }

    public static synchronized WebSocketSession getUserSession(UUID userId) {
        return socketSession.get(userId);
    }

    public static synchronized List<WebSocketSession> getUserSessionList(List<UUID> userIds) {
        List<WebSocketSession> sessions = new ArrayList<>();
        for (UUID userId : userIds) {
            sessions.add(socketSession.get(userId));
        }
        return sessions;
    }

    public static synchronized UserDto getUserDetails(String sessionId) {
        return userDetails.get(sessionId);
    }

    public static synchronized void removeSession(String sessionId) throws IOException {
        UserDto userDto = userDetails.get(sessionId);
        if (userDto != null) {
            WebSocketSession session = socketSession.get(userDto.getId());
            if (session != null) {
                session.close();
            }
            socketSession.remove(userDto.getId());
        }
        userDetails.remove(sessionId);
    }

    public static synchronized boolean checkSession(UUID userId) {
        return hasSession(userId) && sessionIsOpen(userId);
    }

    public static synchronized boolean hasSession(UUID userId) {
        return socketSession.get(userId) != null;
    }

    public static synchronized boolean sessionIsOpen(UUID userId) {
        return socketSession.get(userId).isOpen();
    }

    public static synchronized void closeAll() {
        for (Entry<UUID, WebSocketSession> entry : socketSession.entrySet()) {
            try {
                removeSession(entry.getValue().getId());
            } catch (Exception ex) {
                log.info(ex.getMessage());
            }
        }
    }

}
