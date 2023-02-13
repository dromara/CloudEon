package com.data.udh.controller;

import cn.hutool.core.bean.BeanUtil;
import com.data.udh.controller.response.CommandDetailVO;
import com.data.udh.dao.ClusterInfoRepository;
import com.data.udh.dao.CommandRepository;
import com.data.udh.dao.CommandTaskRepository;
import com.data.udh.entity.ClusterInfoEntity;
import com.data.udh.entity.CommandEntity;
import com.data.udh.entity.CommandTaskEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.powerjob.common.response.ResultDTO;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/command")
public class CommandController {

    @Resource
    private CommandRepository commandRepository;

    @Resource
    private CommandTaskRepository commandTaskRepository;


    @GetMapping("/list")
    public ResultDTO<List<CommandEntity>> listCommand(Integer clusterId) {
        List<CommandEntity> result;
        result = commandRepository.findByClusterId(clusterId);
        return ResultDTO.success(result);
    }

    @GetMapping("/detail")
    public ResultDTO<CommandDetailVO> commandDetail(Integer commandId) {
        CommandDetailVO result=new CommandDetailVO();

        // 查出command
        CommandEntity commandEntity = commandRepository.findById(commandId).get();
        BeanUtil.copyProperties(commandEntity,result);
        // 查出关联的commandTask
        List<CommandTaskEntity> taskEntities = commandTaskRepository.findByCommandId(commandId);
        Map<String, List<CommandTaskEntity>> tasksMap = taskEntities.stream().sorted(new Comparator<CommandTaskEntity>() {
            @Override
            public int compare(CommandTaskEntity o1, CommandTaskEntity o2) {
                return o1.getTaskShowSortNum() - o2.getTaskShowSortNum();
            }
        }).collect(Collectors.groupingBy(CommandTaskEntity::getServiceInstanceName));
        result.setTasksMap(tasksMap);

        return ResultDTO.success(result);
    }
}
