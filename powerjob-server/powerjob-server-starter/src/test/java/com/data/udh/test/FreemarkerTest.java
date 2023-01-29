package com.data.udh.test;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class FreemarkerTest {

    private static final String RENDER_TEMPLATES_DIR = "/Volumes/Samsung_T5/opensource/PowerJob/render_templates";
    private static final String RENDER_OUTPUT_DIR = "/Volumes/Samsung_T5/opensource/PowerJob/render_out";
    ClusterNode node001 = new ClusterNode("node001", 1);
    ClusterNode node002 = new ClusterNode("node002", 2);
    ClusterNode node003 = new ClusterNode("node003", 3);
    ClusterNode node004 = new ClusterNode("node004", 4);
    ClusterNode node005 = new ClusterNode("node005", 5);

    @Test
    public void bean() throws IOException, TemplateException {
        // 1.加载模板
        // 创建核心配置对象
        Configuration config = new Configuration(Configuration.getVersion());
        // 设置加载的目录
        config.setDirectoryForTemplateLoading(new File(RENDER_TEMPLATES_DIR));

        // 数据对象
        Map<String, Object> data = new HashMap<>();
        data.put("itemList", Lists.newArrayList(
                new PropertieEntity("fs.defaultFS", "hdfs://node002"),
                new PropertieEntity("hadoop.tmp.dir", "/tmp/hadoop-${user.name}")
        ));

        // 得到模板对象
        Template template = config.getTemplate("xml.ftl");
        ;

        FileWriter out = new FileWriter(new File(RENDER_OUTPUT_DIR + File.separator + "free.xml"));
        template.process(data, out);
        out.close();
    }

    @Test
    public void demo() throws IOException, TemplateException {
        // 1.加载模板
        // 创建核心配置对象
        Configuration config = new Configuration(Configuration.getVersion());
        // 设置加载的目录
        config.setDirectoryForTemplateLoading(new File(RENDER_TEMPLATES_DIR));

        // 数据对象
        Map<String, Object> data = new HashMap<>();
        // ${r"${JAVA_HOME}"} 输出时只会输出固定字符串
        data.put("JAVA_HOME", "/opt/jdk");
        data.put("service", new ServiceEntity("hdfs1"));

        // 得到模板对象
        String templateName = "hadoop-env.sh.ftl";
        Template template = config.getTemplate(templateName);

        FileWriter out = new FileWriter(RENDER_OUTPUT_DIR + File.separator + StringUtils.substringBeforeLast(templateName, ".ftl"));
        template.process(data, out);
        out.close();
    }

    @Test
    public void map() throws IOException, TemplateException {
        // 1.加载模板
        // 创建核心配置对象
        Configuration config = new Configuration(Configuration.getVersion());
        // 设置加载的目录
        config.setDirectoryForTemplateLoading(new File(RENDER_TEMPLATES_DIR));

        // 数据对象
        Map<String, Object> data = new HashMap<>();
        data.put("service", ImmutableMap.of("sid", "YARN1", "resourcemanager.port", "8088"));
        data.put("memory", "1024");

        // 得到模板对象
        String templateName = "map.sh.ftl";
        Template template = config.getTemplate(templateName);

        FileWriter out = new FileWriter(RENDER_OUTPUT_DIR + File.separator + StringUtils.substringBeforeLast(templateName, ".ftl"));
        template.process(data, out);
        out.close();
    }

    @Test
    public void sub() {
        System.out.println(StringUtils.substringBeforeLast("hadoop-env.sh.ftl", ".ftl"));
    }

    @Test
    public void renderHdfs() throws IOException, TemplateException {

        // 1.加载模板
        // 创建核心配置对象
        Configuration config = new Configuration(Configuration.getVersion());
        // 设置加载的目录
        File templateDir = new File(RENDER_TEMPLATES_DIR + "/hdfs");
        config.setDirectoryForTemplateLoading(templateDir);

        // 数据对象
        Map<String, Object> dataModel = new HashMap<>();
        Map<String, Object> service = new HashMap<>();

        String sid = "HDFS1";
        // 固定项填充
        service.put("sid", sid);
        service.put("nameservices", Lists.newArrayList("nameservice1"));
        service.put("nameservice1", ImmutableMap.of(
                "HDFS_NAMENODE",Lists.newArrayList(node001, node002),
                "HDFS_JOURNALNODE",Lists.newArrayList(node003,node004,node005)
        ));

        service.put("namenode.rpc-port", "50075");
        service.put("namenode.http-port", "8084");
        service.put("namenode.use.wildcard", "false");
        service.put("journalnode.rpc-port", "9809");
        service.put("journalnode.http-port", "7809");
        service.put("journalnode.use.wildcard", "true");
        service.put("datanode.use.wildcard", "true");
        service.put("datanode.port", "5606");
        service.put("datanode.http-port", "7843");
        service.put("datanode.ipc-port", "9345");
        service.put("namenode.container.limits.memory", "-1");
        service.put("namenode.memory.ratio", "-1");
        service.put("namenode.memory", "1024");
        service.put("zkfc.container.limits.memory", "-1");
        service.put("zkfc.memory.ratio", "-1");
        service.put("zkfc.memory", "1024");
        service.put("datanode.container.limits.memory", "-1");
        service.put("datanode.memory.ratio", "-1");
        service.put("datanode.memory", "1024");
        service.put("journalnode.container.limits.memory", "-1");
        service.put("journalnode.memory.ratio", "-1");
        service.put("journalnode.memory", "1024");
        service.put("plugins", Lists.newArrayList("ranger"));

        // 安全
        service.put("auth", "kerberos");
        service.put("keytab", "YARN.keytab");
        service.put("realm", "HADOOP@COM");


        // 定义服务角色
        Map<String, List<ClusterNode>> serviceRoleMap = new HashMap<>();

        serviceRoleMap.put("HDFS_NAMENODE", Lists.newArrayList(node001, node002));
        serviceRoleMap.put("HDFS_DATANODE", Lists.newArrayList(node003, node002, node001));
        serviceRoleMap.put("HDFS_JOURNALNODE", Lists.newArrayList(node003,node004,node005));

        service.put("roles", serviceRoleMap);


        // 配置文件对应参数填充
        service.put("core-site.xml", ImmutableMap.of("fs.trash.interval","1440"));
        service.put("hdfs-site.xml", ImmutableMap.of("dfs.datanode.handler.count","30"));
        service.put("httpfs-site.xml", ImmutableMap.of());


        // 依赖项定义
        // 定义依赖服务
        Map<String, Map<String, Object>> depService = new HashMap<>();
        Map<String, List<ClusterNode>> zkDepServiceRoleMap = new HashMap<>();
        zkDepServiceRoleMap.put("ZOOKEEPER", Lists.newArrayList(node001, node003, node005));
        depService.put("ZOOKEEPER", ImmutableMap.of("sid", "ZOOKEEPER1", "roles", zkDepServiceRoleMap, "zookeeper.client.port", "2181"));

        // 填充freemarker数据模型
        dataModel.put("service", service);
        dataModel.put("dependencies", depService);
        dataModel.put("current.user","root");

        doRender(config, templateDir, dataModel, sid, serviceRoleMap);


    }
    @Test
    public void renderYarn() throws IOException, TemplateException {

        // 1.加载模板
        // 创建核心配置对象
        Configuration config = new Configuration(Configuration.getVersion());
        // 设置加载的目录
        File templateDir = new File(RENDER_TEMPLATES_DIR + "/yarn");
        config.setDirectoryForTemplateLoading(templateDir);

        // 数据对象
        Map<String, Object> dataModel = new HashMap<>();
        Map<String, Object> service = new HashMap<>();

        String sid = "YARN1";
        // 固定项填充
        service.put("sid", sid);
        service.put("resourcemanager.port", "8088");
        service.put("resourcemanager.resource-tracker.port", "9879");
        service.put("resourcemanager.scheduler.port", "8032");
        service.put("resourcemanager.admin.port", "8084");
        service.put("resourcemanager.webapp.port", "8044");
        service.put("resourcemanager.container.limits.memory", "1");
        service.put("resourcemanager.memory", "1");
        service.put("resourcemanager.memory.ratio", "1");
        service.put("nodemanager.container.limits.memory", "1");
        service.put("nodemanager.memory.ratio", "1");
        service.put("nodemanager.memory", "1");
        service.put("historyserver.container.limits.memory", "1");
        service.put("historyserver.memory.ratio", "1");
        service.put("historyserver.memory", "1");
        service.put("timelineserver.container.limits.memory", "1");
        service.put("timelineserver.memory.ratio", "1");
        service.put("timelineserver.memory", "1");
        service.put("plugins", Lists.newArrayList("ranger"));

        // 安全
        service.put("auth", "kerberos");
        service.put("keytab", "YARN.keytab");
        service.put("realm", "HADOOP@COM");


        // 定义服务角色
        Map<String, List<ClusterNode>> serviceRoleMap = new HashMap<>();

        serviceRoleMap.put("YARN_RESOURCEMANAGER", Lists.newArrayList(node001, node002));
        serviceRoleMap.put("YARN_NODEMANAGER", Lists.newArrayList(node003, node002, node001));
        serviceRoleMap.put("YARN_TIMELINESERVER", Lists.newArrayList(node003));
        serviceRoleMap.put("YARN_HISTORYSERVER", Lists.newArrayList(node004));

        service.put("roles", serviceRoleMap);


        // 配置文件对应参数填充
        service.put("yarn-site.xml", ImmutableMap.of("yarn.nodemanager.resource.memory-mb", "1024", "yarn.log-aggregation-enable", "false"));
        service.put("mapred_site", ImmutableMap.of("mapreduce.framework.name", "yarn", "mapreduce.map.memory.mb", "5120", "mapreduce.reduce.memory.mb", "5120"));
        service.put("capacity_scheduler", ImmutableMap.of("yarn.scheduler.capacity.root.acl_submit_applications", "*", "yarn.scheduler.capacity.root.maximum-applications", "10000"));


        // 依赖项定义
        // 定义依赖服务
        Map<String, Map<String, Object>> depService = new HashMap<>();
        Map<String, List<ClusterNode>> zkDepServiceRoleMap = new HashMap<>();
        zkDepServiceRoleMap.put("ZOOKEEPER", Lists.newArrayList(node001, node003, node005));
        depService.put("ZOOKEEPER", ImmutableMap.of("sid", "ZOOKEEPER1", "roles", zkDepServiceRoleMap, "zookeeper.client.port", "2181"));
        depService.put("HDFS", ImmutableMap.of("sid", "HDFS1"));

        // 填充freemarker数据模型
        dataModel.put("service", service);

        dataModel.put("dependencies", depService);

        doRender(config, templateDir, dataModel, sid, serviceRoleMap);


    }

    private void doRender(Configuration config, File templateDir, Map<String, Object> dataModel, String sid, Map<String, List<ClusterNode>> serviceRoleMap) throws IOException, TemplateException {
        // 根据角色绑定的节点，去重得出需要安装配置的节点
        List<String> roleInstallNodes = serviceRoleMap.entrySet().stream().flatMap(new Function<Map.Entry<String, List<ClusterNode>>, Stream<String>>() {
            @Override
            public Stream<String> apply(Map.Entry<String, List<ClusterNode>> stringListEntry) {
                List<String> hostNames = stringListEntry.getValue().stream().map(new Function<ClusterNode, String>() {
                    @Override
                    public String apply(ClusterNode clusterNode) {
                        return clusterNode.getHostname();
                    }
                }).collect(Collectors.toList());
                return hostNames.stream();
            }
        }).distinct().collect(Collectors.toList());

        for (String roleInstallNode : roleInstallNodes) {
            // 设置当前配置渲染节点
            dataModel.put("localhostname", roleInstallNode);
            String outDirPath = RENDER_OUTPUT_DIR + File.separator + sid.toLowerCase() + File.separator + roleInstallNode;
            FileUtils.forceMkdir(new File(outDirPath));

            // 得到模板对象
            for (String templateName : templateDir.list()) {
                if (templateName.endsWith(".ftl")) {
                    Template template = config.getTemplate(templateName);
                    FileWriter out = new FileWriter(outDirPath + File.separator + StringUtils.substringBeforeLast(templateName, ".ftl"));
                    template.process(dataModel, out);
                    out.close();
                }
                if (templateName.endsWith(".raw")) {
                    InputStream fileReader = new FileInputStream(templateDir.getPath() + File.separator + templateName);
                    FileOutputStream out = new FileOutputStream(outDirPath + File.separator + StringUtils.substringBeforeLast(templateName, ".raw"));
                    IOUtils.copy(fileReader, out);
                    IOUtils.close(fileReader);
                    IOUtils.close(out);
                }
            }
        }
    }


    @Test
    public void renderZookeeper() throws IOException, TemplateException {
        // 1.加载模板
        // 创建核心配置对象
        Configuration config = new Configuration(Configuration.getVersion());
        // 设置加载的目录
        File templateDir = new File(RENDER_TEMPLATES_DIR + "/zookeeper");
        config.setDirectoryForTemplateLoading(templateDir);

        // 数据对象
        Map<String, Object> dataModel = new HashMap<>();
        Map<String, Object> service = new HashMap<>();

        String sid = "ZOOKEEPER1";
        // 固定项填充
        service.put("sid", sid);
        service.put("zookeeper.client.port", "2181");
        service.put("zookeeper.peer.communicate.port", "2222");
        service.put("zookeeper.leader.elect.port", "2343");
        service.put("sasl.oauth2.enabled", "false");
        service.put("zookeeper.jmxremote.port", "9898");
        service.put("zookeeper.container.limits.memory", "-1");
        service.put("zookeeper.memory.ratio", "-1");
        service.put("zookeeper.server.memory", "9096");
        service.put("znode.container.checkIntervalMs", "3000");


        service.put("plugins", Lists.newArrayList("ranger"));

        // 安全
        service.put("auth", "kerberos");
        service.put("keytab", "zookeeper.keytab");
        service.put("realm", "HADOOP@COM");


        // 定义服务角色
        Map<String, List<ClusterNode>> serviceRoleMap = new HashMap<>();

        serviceRoleMap.put("ZOOKEEPER", Lists.newArrayList(node001, node003, node005));

        service.put("roles", serviceRoleMap);

        // 配置文件对应参数填充
        service.put("zoo_cfg", ImmutableMap.of("maxClientCnxns", "1024", "extendedTypesEnabled", "false"));


        // 依赖项定义
        // 定义依赖服务
        Map<String, Map<String, Object>> depService = new HashMap<>();

        // 填充freemarker数据模型
        dataModel.put("service", service);

        dataModel.put("dependencies", depService);

        doRender(config, templateDir, dataModel, sid, serviceRoleMap);
    }

    @Data
    @AllArgsConstructor
    public static class ClusterNode {
        private String hostname;
        private Integer id;
    }

    @Data
    @AllArgsConstructor
    public static class ServiceEntity {
        private String sid;
    }


    @Data
    @AllArgsConstructor
    public static class PropertieEntity {
        private String name;
        private String value;
    }
}
