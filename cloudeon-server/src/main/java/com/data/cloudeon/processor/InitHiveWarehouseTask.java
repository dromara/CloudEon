package com.data.cloudeon.processor;

import cn.hutool.extra.spring.SpringUtil;
import com.data.cloudeon.dao.ClusterNodeRepository;
import com.data.cloudeon.dao.ServiceInstanceRepository;
import com.data.cloudeon.dao.ServiceRoleInstanceRepository;
import com.data.cloudeon.dao.StackServiceRepository;
import com.data.cloudeon.entity.ClusterNodeEntity;
import com.data.cloudeon.entity.ServiceInstanceEntity;
import com.data.cloudeon.entity.ServiceRoleInstanceEntity;
import com.data.cloudeon.entity.StackServiceEntity;
import com.data.cloudeon.utils.SshUtils;
import lombok.NoArgsConstructor;
import org.apache.sshd.client.session.ClientSession;

import java.io.IOException;
import java.util.List;

@NoArgsConstructor
public class InitHiveWarehouseTask extends BaseCloudeonTask {


    @Override
    public void internalExecute() {
        ServiceInstanceRepository serviceInstanceRepository = SpringUtil.getBean(ServiceInstanceRepository.class);
        StackServiceRepository stackServiceRepository = SpringUtil.getBean(StackServiceRepository.class);
        ServiceRoleInstanceRepository roleInstanceRepository = SpringUtil.getBean(ServiceRoleInstanceRepository.class);
        ClusterNodeRepository clusterNodeRepository = SpringUtil.getBean(ClusterNodeRepository.class);

        TaskParam taskParam = getTaskParam();
        Integer serviceInstanceId = taskParam.getServiceInstanceId();

        ServiceInstanceEntity serviceInstanceEntity = serviceInstanceRepository.findById(serviceInstanceId).get();
        StackServiceEntity stackServiceEntity = stackServiceRepository.findById(serviceInstanceEntity.getStackServiceId()).get();
        String serviceName = serviceInstanceEntity.getServiceName();
        // todo 能捕获到执行日志吗？
        String cmd = String.format("sudo docker  run --net=host -v /opt/udh/%s/conf:/opt/udh/%s/conf  %s sh -c \"  /opt/udh/%s/conf/init-warehouse-dir.sh \"   ",
                serviceName,serviceName,stackServiceEntity.getDockerImage(),serviceName);

        // 选择metastore所在节点执行
        List<ServiceRoleInstanceEntity> roleInstanceEntities = roleInstanceRepository.findByServiceInstanceIdAndServiceRoleName(serviceInstanceId,"HIVE_METASTORE");
        ServiceRoleInstanceEntity firstNamenode = roleInstanceEntities.get(0);
        Integer nodeId = firstNamenode.getNodeId();
        ClusterNodeEntity nodeEntity = clusterNodeRepository.findById(nodeId).get();
        String ip = nodeEntity.getIp();
        log.info("在节点"+ip+"上执行命令:" + cmd);
        ClientSession clientSession = SshUtils.openConnectionByPassword(ip, nodeEntity.getSshPort(), nodeEntity.getSshUser(), nodeEntity.getSshPassword());
        try {
            String result = SshUtils.execCmdWithResult(clientSession, cmd);
            log.info(result);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        SshUtils.closeConnection(clientSession);


    }
}
