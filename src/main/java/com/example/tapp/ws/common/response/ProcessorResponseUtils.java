package com.example.tapp.ws.common.response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.example.tapp.common.utils.AppUtils;
import com.example.tapp.ws.common.request.PacketName;

public class ProcessorResponseUtils {

    public static boolean success(WebSocketSession session, PacketName packetName, String message) throws IOException {
        ProcessorResponse<String> response = new ProcessorResponse<>();
        response.setStatus(ResponseStatus.SUCCESS);
        response.setStatusCode(0);
        response.setPacketName(packetName.getName());
        response.setPacketCode(packetName.getValue());
        response.setData(message);
        response.setType(DataType.MESSAGE);
        if (session != null && session.isOpen()) {
            session.sendMessage(new TextMessage(AppUtils.jsonStringify(response)));
            return true;
        }
        return false;
    }

    public static List<Boolean> success(List<WebSocketSession> sessions, PacketName packetName, String message) {
        List<Boolean> statusList = new ArrayList<>();
        sessions.parallelStream().forEach((session) -> {
            try {
                statusList.add(success(session, packetName, message));
            } catch (IOException e) {
                e.printStackTrace();
                statusList.add(false);
            }
        });
        return statusList;
    }

    public static boolean error(WebSocketSession session, PacketName packetName, String message) throws IOException {
        ProcessorResponse<String> response = new ProcessorResponse<>();
        response.setStatus(ResponseStatus.ERROR);
        response.setStatusCode(1);
        response.setPacketName(packetName.getName());
        response.setPacketCode(packetName.getValue());
        response.setData(message == null ? "Internal Server error." : message);
        response.setType(DataType.MESSAGE);
        if (session != null && session.isOpen()) {
            session.sendMessage(new TextMessage(AppUtils.jsonStringify(response)));
            return true;
        }
        return false;
    }

    public static boolean error(WebSocketSession session, PacketName packetName, Object object) throws IOException {
        ProcessorResponse<Object> response = new ProcessorResponse<>();
        response.setStatus(ResponseStatus.ERROR);
        response.setStatusCode(1);
        response.setPacketName(packetName.getName());
        response.setPacketCode(packetName.getValue());
        response.setData(object);
        response.setType(DataType.OBJECT);
        if (session != null && session.isOpen()) {
            session.sendMessage(new TextMessage(AppUtils.jsonStringify(response)));
            return true;
        }
        return false;
    }

    public static boolean success(WebSocketSession session, PacketName packetName, Object object) throws IOException {
        ProcessorResponse<Object> response = new ProcessorResponse<>();
        response.setStatus(ResponseStatus.SUCCESS);
        response.setStatusCode(0);
        response.setPacketName(packetName.getName());
        response.setPacketCode(packetName.getValue());
        response.setData(object);
        response.setType(DataType.OBJECT);
        if (session != null && session.isOpen()) {
            session.sendMessage(new TextMessage(AppUtils.jsonStringify(response)));
            return true;
        }
        return false;
    };

    public static List<Boolean> success(List<WebSocketSession> sessions, PacketName packetName, Object object) {
        List<Boolean> statusList = new ArrayList<>();
        sessions.parallelStream().forEach((session) -> {
            try {
                statusList.add(success(session, packetName, object));
            } catch (IOException e) {
                e.printStackTrace();
                statusList.add(false);
            }
        });
        return statusList;
    }

    public static boolean success(WebSocketSession session, PacketName packetName, List<?> list) throws IOException {
        ProcessorResponse<Object> response = new ProcessorResponse<>();
        response.setStatus(ResponseStatus.SUCCESS);
        response.setStatusCode(0);
        response.setPacketName(packetName.getName());
        response.setPacketCode(packetName.getValue());
        response.setData(list);
        response.setType(DataType.LIST);
        if (session != null && session.isOpen()) {
            session.sendMessage(new TextMessage(AppUtils.jsonStringify(response)));
            return true;
        }
        return false;
    }
}