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
public class HdfsZkfcFormatTask extends BaseCloudeonTask {


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
        // containerd ==> ctr  run --mount  type=bind,src=/tmp,dst=/hostdir,options=rbind:rw --rm  --net-host -t  registry.cn-hangzhou.aliyuncs.com/udh/doris:1.1.5 cc2 bash
        // hdfs 这个zkfc format命令每次执行都会删除然后创建的。todo 能捕获到执行日志吗？
        String cmd = String.format("sudo docker  run --net=host -v /opt/udh/%s/conf:/opt/udh/%s/conf  %s sh -c \"  yes Y| hdfs --config  /opt/udh/%s/conf zkfc -formatZK \"   ",
                serviceName,serviceName,stackServiceEntity.getDockerImage(),serviceName);

        // 选择namenode所在节点执行
        List<ServiceRoleInstanceEntity> roleInstanceEntities = roleInstanceRepository.findByServiceInstanceIdAndServiceRoleName(serviceInstanceId,"HDFS_NAMENODE");
        ServiceRoleInstanceEntity firstNamenode = roleInstanceEntities.get(0);
        Integer nodeId = firstNamenode.getNodeId();
        ClusterNodeEntity nodeEntity = clusterNodeRepository.findById(nodeId).get();
        String ip = nodeEntity.getIp();
        log.info("在节点"+ip+"上执行命令:" + cmd);
        ClientSession clientSession = SshUtils.openConnectionByPassword(ip, nodeEntity.getSshPort(), nodeEntity.getSshUser(), nodeEntity.getSshPassword());
        try {
            SshUtils.execCmdWithResult(clientSession, cmd);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        SshUtils.closeConnection(clientSession);


    }
}
