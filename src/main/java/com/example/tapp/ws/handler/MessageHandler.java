package com.example.tapp.ws.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.example.tapp.common.utils.AppUtils;
import com.example.tapp.ws.common.request.PacketName;
import com.example.tapp.ws.common.request.RequestPayload;
import com.example.tapp.ws.common.response.ProcessorResponseUtils;
import com.example.tapp.ws.processor.RequestProcessor;

@Component
public class MessageHandler extends TextWebSocketHandler {

    private static final Logger log = LoggerFactory.getLogger(MessageHandler.class);

    @Autowired
    private RequestProcessor processor;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        log.info("Text Message Received : Session Id [" + session.getId() + "]");
        try {
            if (!message.getPayload().isEmpty()) {
                // String json = DataCrypto.decrypt(message.getPayload());
                String json = message.getPayload();
                RequestPayload payload = AppUtils.jsonParse(json, RequestPayload.class);
                processor.process(session, payload);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            log.info("Exception : " + ex.getMessage());
            ProcessorResponseUtils.error(session, PacketName.SERVER_ERROR, ex.getMessage());
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        processor.closeSession(session);
        log.info("Transport error : Session Id [" + session.getId() + "]");
        log.info("TE : [" + exception.getMessage() + "]");
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("Connected : Session Id [" + session.getId() + "]");
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        processor.closeSession(session);
        log.info("Disconnected : Session Id [" + session.getId() + "] Status[" + status.getCode() + "]");
    }
}
