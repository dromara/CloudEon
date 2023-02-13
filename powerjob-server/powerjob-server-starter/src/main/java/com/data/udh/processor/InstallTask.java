package com.data.udh.processor;

import cn.hutool.extra.spring.SpringUtil;
import com.data.udh.dao.StackServiceRepository;
import com.data.udh.entity.CommandTaskEntity;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class InstallTask extends BaseUdhTask {




    @Override
    public void internalExecute() {
        // 查询服务实例需要创建的目录
        StackServiceRepository serviceRepository = SpringUtil.getBean(StackServiceRepository.class);
//        serviceRepository.findById()

        // ssh执行创建目录

        // todo 日志采集的相关执行
        System.out.println(taskParam.getCommandTaskId() + ":模拟执行。。。。");
        if (taskParam.getCommandTaskId() == 10) {
            int a = 1 / 0;
        }

    }
}
