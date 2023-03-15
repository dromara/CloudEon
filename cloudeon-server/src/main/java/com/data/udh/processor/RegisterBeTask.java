package com.data.udh.processor;

import cn.hutool.db.ds.simple.SimpleDataSource;
import cn.hutool.db.sql.SqlExecutor;
import cn.hutool.extra.spring.SpringUtil;
import com.data.udh.dao.ClusterNodeRepository;
import com.data.udh.dao.ServiceInstanceConfigRepository;
import com.data.udh.dao.ServiceRoleInstanceRepository;
import com.data.udh.entity.ClusterNodeEntity;
import com.data.udh.entity.ServiceRoleInstanceEntity;
import lombok.NoArgsConstructor;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@NoArgsConstructor
public class RegisterBeTask extends BaseUdhTask {

    @Override
    public void internalExecute() {
        ServiceInstanceConfigRepository configRepository = SpringUtil.getBean(ServiceInstanceConfigRepository.class);
        ServiceRoleInstanceRepository roleInstanceRepository = SpringUtil.getBean(ServiceRoleInstanceRepository.class);
        ClusterNodeRepository clusterNodeRepository = SpringUtil.getBean(ClusterNodeRepository.class);

        log.info("开始通过jdbc接口注册be节点到doris中....");

        // 查询服务角色实例中的所有be
        Integer serviceInstanceId = taskParam.getServiceInstanceId();
        List<ServiceRoleInstanceEntity> beInstanceList = roleInstanceRepository
                .findByServiceInstanceIdAndServiceRoleName(serviceInstanceId, "DORIS_BE");

        List<ServiceRoleInstanceEntity> feInstanceList = roleInstanceRepository
                .findByServiceInstanceIdAndServiceRoleName(serviceInstanceId, "DORIS_FE");

        ServiceRoleInstanceEntity masterFe = feInstanceList.stream().findFirst().get();

        ClusterNodeEntity masterFeNode = clusterNodeRepository.findById(masterFe.getNodeId()).get();
        // fe ip
        String masterFeIp = masterFeNode.getIp();
        // fe mysql port
        int masterFeMysqlPort = Integer.parseInt(configRepository.findByServiceInstanceIdAndName(serviceInstanceId, "query_port").getValue());
        // be heartbeat_service_port
        int beHeartBeatPort = Integer.parseInt(configRepository.findByServiceInstanceIdAndName(serviceInstanceId, "heartbeat_service_port").getValue());

        // 建立mysql连接
        DataSource ds = new SimpleDataSource("jdbc:mysql://" + masterFeIp + ":" + masterFeMysqlPort, "root", null);
        try (Connection conn = ds.getConnection();) {

            for (ServiceRoleInstanceEntity beRoleEntity : beInstanceList) {
                // 执行非查询语句，返回影响的行数
                String beIp = clusterNodeRepository.findById(beRoleEntity.getNodeId()).get().getIp();
                String sql = String.format("ALTER SYSTEM ADD BACKEND \"%s:%s\" ", beIp, beHeartBeatPort);
                log.info("执行sql：{}", sql);
                SqlExecutor.execute(conn, sql);
            }


        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }


    }
}
