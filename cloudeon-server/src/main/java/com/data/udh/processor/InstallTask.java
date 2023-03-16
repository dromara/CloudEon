package com.data.udh.processor;

import cn.hutool.extra.spring.SpringUtil;
import com.data.udh.dao.ClusterNodeRepository;
import com.data.udh.dao.ServiceInstanceRepository;
import com.data.udh.entity.ClusterNodeEntity;
import com.data.udh.entity.ServiceInstanceEntity;
import com.data.udh.utils.SshUtils;
import lombok.NoArgsConstructor;
import org.apache.sshd.client.session.ClientSession;

import java.io.IOException;
import java.util.Arrays;
import java.util.function.Consumer;

@NoArgsConstructor
public class InstallTask extends BaseUdhTask {


    @Override
    public void internalExecute() {
        // 查询服务实例需要创建的目录
        ServiceInstanceRepository serviceInstanceRepository = SpringUtil.getBean(ServiceInstanceRepository.class);
        ClusterNodeRepository clusterNodeRepository = SpringUtil.getBean(ClusterNodeRepository.class);
        ServiceInstanceEntity serviceInstanceEntity = serviceInstanceRepository.findById(taskParam.getServiceInstanceId()).get();
        String persistencePaths = serviceInstanceEntity.getPersistencePaths();
        String[] paths = persistencePaths.split(",");

        ClusterNodeEntity nodeEntity = clusterNodeRepository.findByHostname(taskParam.getHostName());
        ClientSession clientSession = SshUtils.openConnectionByPassword(nodeEntity.getIp(), nodeEntity.getSshPort(), nodeEntity.getSshUser(), nodeEntity.getSshPassword());
        // 查询节点
        Arrays.stream(paths).forEach(new Consumer<String>() {
            @Override
            public void accept(String path) {
                log.info("节点：" + taskParam.getHostName() + " 上创建目录：" + path);
                // ssh执行创建目录
                SshUtils.createDir(clientSession, path);
                try {
                    String command = "chmod 777 -R " + path;
                    log.info("执行远程命令："+command);
                    SshUtils.execCmdWithResult(clientSession, command);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                log.info("成功在节点：" + taskParam.getHostName() + " 上创建目录：" + path);

            }
        });
        SshUtils.closeConnection(clientSession);


        // todo 服务日志采集的相关执行

    }
}
