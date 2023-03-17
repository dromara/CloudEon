package com.data.udh.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.data.udh.dao.*;
import com.data.udh.dto.StackConfiguration;
import com.data.udh.dto.StackServiceInfo;
import com.data.udh.dto.StackServiceRole;
import com.data.udh.entity.StackInfoEntity;
import com.data.udh.entity.StackServiceConfEntity;
import com.data.udh.entity.StackServiceEntity;
import com.data.udh.entity.StackServiceRoleEntity;
import com.data.udh.utils.ConfValueType;
import com.data.udh.utils.ImageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import static com.data.udh.utils.Constant.*;

/**
 * 重启后，将正在执行的指令都变成失败的
 */
@Component
@Slf4j
public class FailureOverCommandService implements ApplicationRunner {


    @Resource
    CommandRepository commandRepository;

    @Resource
    CommandTaskRepository commandTaskRepository;

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        commandRepository.updateRunningCommand2Error();
    }
}
