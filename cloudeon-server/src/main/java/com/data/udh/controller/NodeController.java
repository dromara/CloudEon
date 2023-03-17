package com.data.udh.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSONObject;
import com.data.udh.controller.request.SaveNodeRequest;
import com.data.udh.controller.response.NodeInfoVO;
import com.data.udh.dao.ClusterNodeRepository;
import com.data.udh.dto.CheckHostInfo;
import com.data.udh.dto.ResultDTO;
import com.data.udh.entity.ClusterNodeEntity;
import com.data.udh.utils.SshUtils;
import org.apache.sshd.client.session.ClientSession;
import org.apache.sshd.sftp.client.SftpClientFactory;
import org.apache.sshd.sftp.client.fs.SftpFileSystem;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/node")
public class NodeController {

    @Value("${udh.remote.script.path}")
    private String remoteScriptPath;

    @Resource
    private ClusterNodeRepository clusterNodeRepository;

    @PostMapping("/add")
    public ResultDTO<Void> addNode(@RequestBody SaveNodeRequest req) throws IOException {
        String ip = req.getIp();
        Integer sshPort = req.getSshPort();
        String sshUser = req.getSshUser();
        String sshPassword = req.getSshPassword();
        // 检查ip不能重复
        if (clusterNodeRepository.countByIp(ip) > 0) {
           return ResultDTO.failed("已添加ip为：" + ip + " 的节点(服务器)");
        }
        // 检查ssh通讯是否ok
        CheckHostInfo checkHostInfo = checkHostInfo(ip, sshPort, sshUser, sshPassword);

        // 保存到数据库
        ClusterNodeEntity newClusterNodeEntity = new ClusterNodeEntity();
        BeanUtil.copyProperties(req, newClusterNodeEntity);
        newClusterNodeEntity.setHostname(checkHostInfo.getHostname());
        newClusterNodeEntity.setCoreNum(checkHostInfo.getCoreNum());
        newClusterNodeEntity.setCpuArchitecture("");
        newClusterNodeEntity.setTotalDisk(checkHostInfo.getTotalDisk());
        newClusterNodeEntity.setTotalMem(checkHostInfo.getTotalMem());
        newClusterNodeEntity.setCpuArchitecture(checkHostInfo.getArch());
        newClusterNodeEntity.setCreateTime(new Date());
        clusterNodeRepository.save(newClusterNodeEntity);


        return ResultDTO.success(null);
    }

    /**
     * 查询服务器基础信息
     */
    public CheckHostInfo checkHostInfo(String sshHost, Integer sshPort, String sshUser, String password) throws IOException {

        ClientSession session = SshUtils.openConnectionByPassword(sshHost, sshPort, sshUser, password);
        SftpFileSystem sftp = SftpClientFactory.instance().createSftpFileSystem(session);
        SshUtils.uploadFile(session, "/tmp/", remoteScriptPath + FileUtil.FILE_SEPARATOR + "host-info-collect.sh",sftp);
        String result = SshUtils.execCmdWithResult(session, "sh /tmp/host-info-collect.sh");
        CheckHostInfo checkHostInfo = JSONObject.parseObject(result, CheckHostInfo.class);
        session.close();
        return checkHostInfo;
    }

    @GetMapping("/list")
    public ResultDTO<List<NodeInfoVO>> listNode(Integer clusterId) {
        List<NodeInfoVO> result;
        List<ClusterNodeEntity> nodeEntities = clusterNodeRepository.findByClusterId(clusterId);
        result = nodeEntities.stream().map(n -> {
            NodeInfoVO nodeInfoVO = new NodeInfoVO();
            BeanUtil.copyProperties(n, nodeInfoVO);
            return nodeInfoVO;
        }).collect(Collectors.toList());
        return ResultDTO.success(result);
    }
}
