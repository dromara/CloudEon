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
import com.data.udh.utils.ByteConverter;
import com.data.udh.utils.SshUtils;
import io.fabric8.kubernetes.api.model.Node;
import io.fabric8.kubernetes.api.model.NodeList;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.apache.sshd.client.session.ClientSession;
import org.apache.sshd.sftp.client.SftpClientFactory;
import org.apache.sshd.sftp.client.fs.SftpFileSystem;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/node")
public class NodeController {

    @Value("${udh.remote.script.path}")
    private String remoteScriptPath;

    @Resource
    private ClusterNodeRepository clusterNodeRepository;

    @Resource
    private KubernetesClient kubeClient;

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

    /**
     *  根据集群id查询绑定的k8s节点信息
     */
    @GetMapping("/list")
    public ResultDTO<List<NodeInfoVO>> listNode(Integer clusterId) {
        List<NodeInfoVO> result;
        // 获取k8s集群节点信息
        NodeList nodeList = kubeClient.nodes().list();
        List<Node> items = nodeList.getItems();
        Map<String, Node> nodeMap = items.stream().collect(Collectors.toMap(new Function<Node, String>() {
            @Override
            public String apply(Node node) {
                return node.getStatus().getAddresses().get(0).getAddress();
            }
        }, node -> node));
        // 从数据库查出当前集群绑定的节点
        List<ClusterNodeEntity> nodeEntities = clusterNodeRepository.findByClusterId(clusterId);
        result = nodeEntities.stream().map(n -> {
            // 从map中获得k8s上最新的节点信息
            Node node = nodeMap.get(n.getIp());
            NodeInfoVO nodeInfoVO = getNodeInfoVO(node);
            return nodeInfoVO;
        }).collect(Collectors.toList());
        return ResultDTO.success(result);
    }


    /**
     * 查询k8s节点信息详情
     * todo  过滤出未绑定的节点
     */
    @GetMapping("/listK8sNode")
    public ResultDTO<List<NodeInfoVO>> listK8sNode() {

        NodeList nodeList = kubeClient.nodes().list();
        List<Node> items = nodeList.getItems();
        List<NodeInfoVO> result = items.stream().map(e -> {
            NodeInfoVO nodeInfoVO = getNodeInfoVO(e);

            return nodeInfoVO;

        }).collect(Collectors.toList());

        return ResultDTO.success(result);
    }

    private NodeInfoVO getNodeInfoVO(Node e) {
        int cpu = e.getStatus().getCapacity().get("cpu").getNumericalAmount().intValue();
        long memory = e.getStatus().getCapacity().get("memory").getNumericalAmount().longValue();
        long storage = e.getStatus().getCapacity().get("ephemeral-storage").getNumericalAmount().longValue();
        String ip = e.getStatus().getAddresses().get(0).getAddress();
        String hostname = e.getStatus().getAddresses().get(1).getAddress();
        String architecture = e.getStatus().getNodeInfo().getArchitecture();
        String containerRuntimeVersion = e.getStatus().getNodeInfo().getContainerRuntimeVersion();
        String kubeletVersion = e.getStatus().getNodeInfo().getKubeletVersion();
        String kernelVersion = e.getStatus().getNodeInfo().getKernelVersion();
        String osImage = e.getStatus().getNodeInfo().getOsImage();

        NodeInfoVO nodeInfoVO = NodeInfoVO.builder()
                .ip(ip)
                .hostname(hostname)
                .cpuArchitecture(architecture)
                .coreNum(cpu)
                .totalMem(ByteConverter.convertKBToGB(memory) + " GB")
                .totalDisk(ByteConverter.convertKBToGB(storage) + " GB")
                .kernelVersion(kernelVersion)
                .kubeletVersion(kubeletVersion)
                .containerRuntimeVersion(containerRuntimeVersion)
                .osImage(osImage)
                .build();
        return nodeInfoVO;
    }
}
