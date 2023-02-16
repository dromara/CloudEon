package com.data.udh.processor;

import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.extra.template.Template;
import cn.hutool.extra.template.TemplateConfig;
import cn.hutool.extra.template.TemplateEngine;
import cn.hutool.extra.template.TemplateUtil;
import com.data.udh.dao.ClusterNodeRepository;
import com.data.udh.dao.ServiceInstanceRepository;
import com.data.udh.dao.StackServiceRepository;
import com.data.udh.entity.ClusterNodeEntity;
import com.data.udh.entity.CommandTaskEntity;
import com.data.udh.entity.ServiceInstanceEntity;
import com.data.udh.entity.StackServiceEntity;
import com.data.udh.utils.SshUtils;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.sshd.client.session.ClientSession;

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
                log.info("成功在节点：" + taskParam.getHostName() + " 上创建目录：" + path);

            }
        });
        SshUtils.closeConnection(clientSession);


        // todo 服务日志采集的相关执行
        System.out.println(taskParam.getCommandTaskId() + ":模拟执行。。。。");

    }
}
