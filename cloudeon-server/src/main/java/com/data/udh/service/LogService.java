package com.data.udh.service;

import com.data.udh.dao.ServiceInstanceRepository;
import com.data.udh.dao.ServiceRoleInstanceRepository;
import com.data.udh.dao.StackServiceRoleRepository;
import com.data.udh.dto.WsSessionBean;
import com.data.udh.entity.ServiceInstanceEntity;
import com.data.udh.entity.ServiceRoleInstanceEntity;
import com.data.udh.websocket.LogWebSocketHandler;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
@Slf4j
@Service
public class LogService {


    @Resource
    private ServiceRoleInstanceRepository roleInstanceRepository;

    @Resource
    private ServiceInstanceRepository serviceInstanceRepository;
    @Resource
    private StackServiceRoleRepository stackServiceRoleRepository;

    /**
     * 主要逻辑
     * 1. 准备要执行的Shell命令：tail -1f 日志文件的绝对路径，例如：tail -1f /data/blog.hackyle.com/blog-business-logs/blog-business.log
     * 2. 获取sshSession，创建一个执行Shell命令的Channel
     * 3. 从Channel中读取流，包装为字符流，一次读取一行日志数据
     * 4. 获取WebSocket Session，只要它没有被关闭，就将日志数据通过该Session推送出去
     *
     * @param wsSessionBean 前端Client与后端WebSocketServer建立的连接实例
     */
    public void sendLog2BrowserClient(WsSessionBean wsSessionBean, Integer roleId) throws Exception {
        ServiceRoleInstanceEntity roleInstanceEntity = roleInstanceRepository.findById(roleId).get();
        ServiceInstanceEntity serviceInstanceEntity = serviceInstanceRepository.findById(roleInstanceEntity.getServiceInstanceId()).get();

        WebSocketSession wsSession = wsSessionBean.getWebSocketSession();
        Session sshSession = wsSessionBean.getSshSession();

        //String command = "ssh tpbbsc01 \"tail -" +count+ "f " +logPath+ "\""; //二级SSH跳板机在这里修改
        String command = String.format("tail -20f  /opt/udh/%s/log/%s", serviceInstanceEntity.getServiceName(), "zookeeper-zookeeper-server-fl001.log");
        log.info("查看角色实例日志command: " + command);

        //创建一个执行Shell命令的Channel
        ChannelExec channelExec = (ChannelExec) sshSession.openChannel("exec");
        channelExec.setCommand(command);
        channelExec.connect();
        InputStream inputStream = channelExec.getInputStream();

        //包装为字符流，方便每次读取一行
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        String buf = "";
        while ((buf = reader.readLine()) != null) {
            if (wsSession.isOpen()) {
                //往WebSocket中推送数据
                wsSession.sendMessage(new TextMessage(buf));
            }

        }

        //while(wsSession.isOpen()) {
        //    buf = reader.readLine();
        //    //log.info(buf.length());
        //    if(null != buf && buf.length() > 0 && wsSession.isOpen()) {
        //        //往WebSocket中推送数据
        //        wsSession.sendMessage(new TextMessage(buf));
        //    }
        //}

        //WebSocket、SSH Session的关闭，通过本类下的‘closeWebSocketServer’方法控制
    }

//    public boolean closeWebSocketServer(String sid) {
//        return logWebSocketHandler.closeWebSocketServer(sid);
//    }
}
