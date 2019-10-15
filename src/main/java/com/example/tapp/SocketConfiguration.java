package com.example.tapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.example.tapp.common.utils.YamlPropertySourceFactory;
import com.example.tapp.ws.handler.MessageHandler;
import com.example.tapp.ws.interceptor.ChatInterceptor;

@Configuration
@EnableWebSocket
@PropertySource(factory = YamlPropertySourceFactory.class, value = "classpath:utils.yml")
public class SocketConfiguration implements WebSocketConfigurer {

    @Value("${tapp.application.scoket.base-path}")
    private String BASE_PATH;

    @Value("${tapp.application.scoket.chat-path}")
    private String CHAT_PATH;

    @Autowired
    private MessageHandler messageHandler;

    @Autowired
    private ChatInterceptor chatInterceptor;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        String chatPath = BASE_PATH + CHAT_PATH;
        registry.addHandler(messageHandler, chatPath).setAllowedOrigins("*").addInterceptors(chatInterceptor);
    }

}