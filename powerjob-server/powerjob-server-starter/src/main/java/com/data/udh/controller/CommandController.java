package com.data.udh.controller;

import com.data.udh.controller.response.CommandDetailVO;
import com.data.udh.dao.ClusterInfoRepository;
import com.data.udh.dao.CommandRepository;
import com.data.udh.dao.CommandTaskRepository;
import com.data.udh.entity.ClusterInfoEntity;
import com.data.udh.entity.CommandEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.powerjob.common.response.ResultDTO;

import javax.annotation.Resource;
import java.util.List;

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

//    @GetMapping("/detail")
//    public ResultDTO<CommandDetailVO> commandDetail(Integer commandId) {
//        CommandDetailVO result;
//
//        // 查出command
//
//        // 查出关联的commandTask
//
//        return ResultDTO.success(result);
//    }
}
