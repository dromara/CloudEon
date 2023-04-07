package com.data.udh.processor;

import cn.hutool.extra.spring.SpringUtil;
import com.data.udh.dao.ClusterNodeRepository;
import com.data.udh.dao.StackServiceRepository;
import com.data.udh.entity.ClusterNodeEntity;
import com.data.udh.entity.StackServiceEntity;
import com.data.udh.utils.SshUtils;
import lombok.NoArgsConstructor;
import org.apache.sshd.client.session.ClientSession;

import java.io.IOException;

@NoArgsConstructor
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
        String command = "";
        String runtimeContainer = nodeEntity.getRuntimeContainer();
        // 兼容containerd
        if (runtimeContainer.startsWith("docker")) {
            command=  "docker pull " + dockerImage;
        } else if (runtimeContainer.startsWith("containerd")) {
            command=  "ctr image pull " + dockerImage;
        }
        // ssh执行拉镜像
        ClientSession clientSession = SshUtils.openConnectionByPassword(nodeEntity.getIp(), nodeEntity.getSshPort(), nodeEntity.getSshUser(), nodeEntity.getSshPassword());
        try {
            log.info("节点：" + taskParam.getHostName() + " 上执行命令：" + command);
            String result = SshUtils.execCmdWithResult(clientSession, command);
            log.info(result);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        log.info("成功在节点：" + taskParam.getHostName() + " 上拉取镜像：" + dockerImage);
        SshUtils.closeConnection(clientSession);




    }
}
