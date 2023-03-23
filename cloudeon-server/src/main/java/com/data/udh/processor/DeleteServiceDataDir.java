package com.data.udh.processor;

import cn.hutool.extra.spring.SpringUtil;
import com.data.udh.dao.ClusterNodeRepository;
import com.data.udh.entity.ClusterNodeEntity;
import com.data.udh.utils.SshUtils;
import lombok.NoArgsConstructor;
import org.apache.sshd.client.session.ClientSession;

import java.io.IOException;

@NoArgsConstructor
public class DeleteServiceDataDir extends BaseUdhTask {

    @Override
    public void internalExecute() {
        ClusterNodeRepository clusterNodeRepository = SpringUtil.getBean(ClusterNodeRepository.class);
        String serviceInstanceName = taskParam.getServiceInstanceName();
        ClusterNodeEntity nodeEntity = clusterNodeRepository.findByHostname(taskParam.getHostName());

        try (ClientSession clientSession = SshUtils.openConnectionByPassword(nodeEntity.getIp(), nodeEntity.getSshPort(), nodeEntity.getSshUser(), nodeEntity.getSshPassword());) {
            String remoteDataDirPath = "/opt/udh/" + serviceInstanceName;
            String command = "rm -rf " + remoteDataDirPath;
            log.info("执行远程命令：" + command);
            SshUtils.execCmdWithResult(clientSession, command);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("打开sftp失败：" + e);
        }

    }
}
