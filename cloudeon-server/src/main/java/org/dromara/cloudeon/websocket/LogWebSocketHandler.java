/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.dromara.cloudeon.websocket;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.dromara.cloudeon.dto.WsSessionBean;
import org.dromara.cloudeon.service.LogService;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;

@Component
@Slf4j
public class LogWebSocketHandler extends TextWebSocketHandler implements WebSocketHandler {
    /**
     * 保存与本个WebSocket建立起连接的客户端，Map<wsSessionId, wsSession Instance>
     */
    //private static Map<String, WebSocketSession> livingSessions = new ConcurrentHashMap<>();
    private static Map<String, WsSessionBean> livingSessionMap = new ConcurrentHashMap<>(); //使用线程安全的Map

    @Resource
    private LogService logService;

    private ThreadPoolExecutor threadPoolExecutor = ThreadUtil.newExecutor(5, 100);

    /**
     * 连接建立成功时调用
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("WebSocketServer连接建立成功：" + session.getId());
        //先把SessionId发给前端，规定好一个格式，方便前端判定
        session.sendMessage(new TextMessage("##sessionId:" + session.getId()));
    }

    /**
     * 当客户端有消息发来时调用
     *
     * @param session 客户端连接
     * @param message 传来的消息
     */
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        String payload = message.getPayload();
        log.info("WebSocketServer收到客户端" + session.getId() + "的消息：" + payload);
        if (StrUtil.isNotBlank(payload) && payload.startsWith("##roleId:")) {
            // 提取角色实例id
            String roleIdStr = payload.split(":")[1];
            Integer roleId = Integer.valueOf(roleIdStr);
            try {
                //缓存当前已经创建的连接
                WsSessionBean wsSessionBean = new WsSessionBean();
                wsSessionBean.setWebSocketSession(session);
                wsSessionBean.setWsSessionId(session.getId());
                livingSessionMap.put(session.getId(), wsSessionBean);

                //WebSocket的前后端建立连接成功，立即调用日志推动逻辑，将数据推送给客户端
                threadPoolExecutor.execute(() -> {
                    try {
                        logService.sendLog2BrowserClient(wsSessionBean, roleId);
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                    }
                });
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }

    }

    /**
     * 当有出错信息时调用
     */
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        if (session.isOpen()) {
            session.close();
        }
        livingSessionMap.remove(session.getId());

        log.info("WebSocketServer出现错误：" + session.getId() + exception);
    }

    /**
     * 关闭连接后调用
     *
     * @param session 连接
     * @param status  状态
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String sessionId = session.getId();
        livingSessionMap.remove(sessionId);
        log.info("WebSocketServer已关闭：" + sessionId);
    }

}
