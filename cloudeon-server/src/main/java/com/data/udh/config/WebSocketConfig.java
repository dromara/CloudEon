package com.data.udh.config;

import cn.hutool.extra.spring.SpringUtil;
import com.data.udh.websocket.ChatWebSocketHandler;
import com.data.udh.websocket.LogWebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
 
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        LogWebSocketHandler logWebSocketHandler = SpringUtil.getBean(LogWebSocketHandler.class);
        registry.addHandler(new ChatWebSocketHandler(), "/chat");
        registry.addHandler(logWebSocketHandler, "/log");
    }
 
}
