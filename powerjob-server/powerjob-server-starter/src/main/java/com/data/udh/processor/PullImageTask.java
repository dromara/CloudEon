package com.data.udh.processor;

import cn.hutool.extra.spring.SpringUtil;
import com.data.udh.dao.ClusterNodeRepository;
import com.data.udh.dao.StackServiceRepository;
import com.data.udh.entity.ClusterNodeEntity;
import com.data.udh.entity.StackServiceEntity;
import com.data.udh.utils.SshUtils;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.sshd.client.session.ClientSession;

import java.io.IOException;
import java.util.Arrays;
import java.util.function.Consumer;

@NoArgsConstructor
@Slf4j
public class PullImageTask extends BaseUdhTask {


    @Override
    public void internalExecute() {
        StackServiceRepository stackServiceRepository = SpringUtil.getBean(StackServiceRepository.class);
        ClusterNodeRepository clusterNodeRepository = SpringUtil.getBean(ClusterNodeRepository.class);

        // 查询安装服务的镜像
        StackServiceEntity stackServiceEntity = stackServiceRepository.findById(taskParam.getStackServiceId()).get();
        String dockerImage = stackServiceEntity.getDockerImage();
        ClusterNodeEntity nodeEntity = clusterNodeRepository.findByHostname(taskParam.getHostName());
        log.info("节点：" + taskParam.getHostName() + " 上拉取镜像：" + dockerImage);
        // ssh执行拉镜像
        ClientSession clientSession = SshUtils.openConnectionByPassword(nodeEntity.getHostname(), nodeEntity.getSshPort(), nodeEntity.getSshUser(), nodeEntity.getSshPassword());
        try {
            String command = "docker pull " + dockerImage;
            log.info("节点：" + taskParam.getHostName() + " 上执行命令：" + command);
            SshUtils.execCmdWithResult(clientSession, command);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        log.info("成功在节点：" + taskParam.getHostName() + " 上拉取镜像：" + dockerImage);
        SshUtils.closeConnection(clientSession);




    }
}
