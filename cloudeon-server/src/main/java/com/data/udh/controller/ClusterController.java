package com.data.udh.controller;

import com.data.udh.controller.request.ModifyClusterInfoRequest;
import com.data.udh.dao.ClusterInfoRepository;
import com.data.udh.dao.StackInfoRepository;
import com.data.udh.dto.ResultDTO;
import com.data.udh.entity.ClusterInfoEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

import static com.data.udh.utils.Constant.AdminUserName;

/**
 * Cluster Controller
 * vue axios 的POST请求必须使用 @RequestBody 接收
 *
 */
@RestController
@RequestMapping("/cluster")
public class ClusterController {


    @Resource
    private ClusterInfoRepository clusterInfoRepository;

    @Resource
    private StackInfoRepository stackInfoRepository;


    @PostMapping("/save")
    public ResultDTO<Void> saveCluster(@RequestBody ModifyClusterInfoRequest req) {

        ClusterInfoEntity clusterInfoEntity;

        // 检查框架id是否存在
        Integer stackId = req.getStackId();
        stackInfoRepository.findById(stackId).orElseThrow(() -> new IllegalArgumentException("can't find stack info by stack Id:" + stackId));;

        Integer id = req.getId();
        if (id == null) {
            clusterInfoEntity = new ClusterInfoEntity();
            clusterInfoEntity.setCreateTime(new Date());
        }else {
            clusterInfoEntity = clusterInfoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("can't find cluster info by id:" + id));
        }
        BeanUtils.copyProperties(req, clusterInfoEntity);
        clusterInfoEntity.setCreateTime(new Date());
        clusterInfoEntity.setCreateBy(AdminUserName);

        clusterInfoRepository.saveAndFlush(clusterInfoEntity);
        return ResultDTO.success(null);
    }


    @GetMapping("/delete")
    public ResultDTO<Void> deleteCluster(Integer appId) {
        // todo 删除前确认节点和服务都删除了
        clusterInfoRepository.deleteById(appId);
        return ResultDTO.success(null);
    }

    @GetMapping("/list")
    public ResultDTO<List<ClusterInfoEntity>> listClusterInfo() {
        List<ClusterInfoEntity> result;
        result = clusterInfoRepository.findAll();
        return ResultDTO.success(result);
    }




}
