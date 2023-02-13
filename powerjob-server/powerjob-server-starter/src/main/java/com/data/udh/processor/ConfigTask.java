package com.data.udh.processor;

import cn.hutool.core.io.FileUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.data.udh.config.UdhConfigProp;
import com.data.udh.dao.ClusterNodeRepository;
import com.data.udh.dao.StackServiceRepository;
import com.data.udh.entity.ClusterNodeEntity;
import com.data.udh.entity.StackServiceEntity;
import com.data.udh.utils.SshUtils;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.sshd.client.session.ClientSession;

import java.io.IOException;

@NoArgsConstructor
@Slf4j
public class ConfigTask extends BaseUdhTask {


    @Override
    public void internalExecute() {
        StackServiceRepository stackServiceRepository = SpringUtil.getBean(StackServiceRepository.class);
        ClusterNodeRepository clusterNodeRepository = SpringUtil.getBean(ClusterNodeRepository.class);

        // 创建工作目录  xxx/node001/zookeeper1
        UdhConfigProp udhConfigProp = SpringUtil.getBean(UdhConfigProp.class);
        String workHome = udhConfigProp.getWorkHome();

//        FileUtil

        // 用freemarker在本地生成服务实例的所有配置文件

        // ssh上传所有配置文件到指定目录




    }
}
