package com.data.cloudeon.processor;

import cn.hutool.extra.spring.SpringUtil;
import com.data.cloudeon.dao.ClusterNodeRepository;
import com.data.cloudeon.dao.ServiceInstanceRepository;
import com.data.cloudeon.entity.ClusterNodeEntity;
import com.data.cloudeon.entity.ServiceInstanceEntity;
import com.data.cloudeon.utils.SshUtils;
import lombok.NoArgsConstructor;
import org.apache.sshd.client.session.ClientSession;
import org.apache.sshd.sftp.client.SftpClientFactory;
import org.apache.sshd.sftp.client.fs.SftpFileSystem;

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
        SftpFileSystem sftp;
        try {
            sftp = SftpClientFactory.instance().createSftpFileSystem(clientSession);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("打开sftp失败："+e);
        }
        // 查询节点
        Arrays.stream(paths).forEach(new Consumer<String>() {
            @Override
            public void accept(String path) {
                log.info("节点：" + taskParam.getHostName() + " 上创建目录：" + path);
                // ssh执行创建目录
                SshUtils.createDir(clientSession, path, sftp);
                try {
                    String command = "chmod 777 -R " + path;
                    log.info("执行远程命令：" + command);
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
