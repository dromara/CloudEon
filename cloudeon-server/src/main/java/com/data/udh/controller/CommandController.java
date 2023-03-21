package com.data.udh.controller;

import cn.hutool.core.bean.BeanUtil;
import com.data.udh.controller.response.CommandDetailVO;
import com.data.udh.dao.CommandRepository;
import com.data.udh.dao.CommandTaskRepository;
import com.data.udh.dto.ResultDTO;
import com.data.udh.dto.ServiceProgress;
import com.data.udh.entity.CommandEntity;
import com.data.udh.entity.CommandTaskEntity;
import com.data.udh.utils.CommandState;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
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
        result = commandRepository.findByClusterIdOrderBySubmitTimeDesc(clusterId);
        return ResultDTO.success(result);
    }

    @GetMapping("/countActive")
    public ResultDTO<Long> countActive(Integer clusterId) {
        long result = commandRepository.countByCommandStateAndClusterId(CommandState.RUNNING,clusterId);
        return ResultDTO.success(result);
    }

    @GetMapping("/detail")
    public ResultDTO<CommandDetailVO> commandDetail(Integer commandId) {
        CommandDetailVO result = new CommandDetailVO();

        // 查出command
        CommandEntity commandEntity = commandRepository.findById(commandId).get();
        BeanUtil.copyProperties(commandEntity, result);
        // 查出关联的commandTask
        List<CommandTaskEntity> taskEntities = commandTaskRepository.findByCommandId(commandId);
        Map<String, List<CommandTaskEntity>> tasksMap = taskEntities.stream().sorted(new Comparator<CommandTaskEntity>() {
            @Override
            public int compare(CommandTaskEntity o1, CommandTaskEntity o2) {
                return o1.getTaskShowSortNum() - o2.getTaskShowSortNum();
            }
        }).collect(Collectors.groupingBy(CommandTaskEntity::getServiceInstanceName));
        // 计算各个服务的当前状态
        List<ServiceProgress> serviceProgresses = tasksMap.entrySet().stream().map(new Function<Map.Entry<String, List<CommandTaskEntity>>, ServiceProgress>() {
            @Override
            public ServiceProgress apply(Map.Entry<String, List<CommandTaskEntity>> serviceTaskMap) {
                List<CommandTaskEntity> commandTaskEntities = serviceTaskMap.getValue();
                String currentState = "";
                Map<CommandState, List<CommandTaskEntity>> commandStateListMap = commandTaskEntities.stream().collect(Collectors.groupingBy(CommandTaskEntity::getCommandState));
                if (commandStateListMap.get(CommandState.ERROR)!=null && commandStateListMap.get(CommandState.ERROR).size() > 0) {
                    currentState = CommandState.ERROR.name();
                }
                if (commandStateListMap.get(CommandState.RUNNING)!=null && commandStateListMap.get(CommandState.RUNNING).size() > 0) {
                    currentState = CommandState.RUNNING.name();
                }
                if (commandStateListMap.get(CommandState.WAITING)!=null && commandStateListMap.get(CommandState.WAITING).size() == commandTaskEntities.size()) {
                    currentState = CommandState.WAITING.name();
                }
                if (commandStateListMap.get(CommandState.SUCCESS)!=null && commandStateListMap.get(CommandState.SUCCESS).size() == commandTaskEntities.size()) {
                    currentState = CommandState.SUCCESS.name();
                }
                return new ServiceProgress(currentState, serviceTaskMap.getKey(),serviceTaskMap.getValue());
            }
        }).collect(Collectors.toList());

        result.setServiceProgresses(serviceProgresses);

        return ResultDTO.success(result);
    }
}
