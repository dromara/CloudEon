package com.data.udh.test;

import cn.hutool.core.io.FileUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.Tag;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class FileTest {
    @Test
    public void mkdir() {
        // 只删除conf目录
        FileUtil.del("/Volumes/Samsung_T5/opensource/e-mapreduce/work" + File.separator + "zookeeper1" + File.separator + "conf");
        // 只创建到全路径的父目录
//        FileUtil.mkParentDirs("/Volumes/Samsung_T5/opensource/e-mapreduce/work"+File.separator+"zookeeper1"+File.separator+"conf");

        // 创建全目录
//        FileUtil.mkdir("/Volumes/Samsung_T5/opensource/e-mapreduce/work"+File.separator+"zookeeper1"+File.separator+"conf");

    }





}
