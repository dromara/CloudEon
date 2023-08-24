/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.dromara.cloudeon.controller;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.dromara.cloudeon.config.CloudeonConfigProp;
import org.dromara.cloudeon.dao.ServiceInstanceRepository;
import org.dromara.cloudeon.dao.ServiceRoleInstanceRepository;
import org.dromara.cloudeon.entity.ServiceInstanceEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

@RestController
@RequestMapping("/download")
public class DownloadController {

    @Resource
    private ServiceInstanceRepository serviceInstanceRepository;

    @Resource
    private ServiceRoleInstanceRepository roleInstanceRepository;
    @Resource
    private CloudeonConfigProp cloudeonConfigProp;


    /**
     * 下载服务实例的client配置
     */
    @GetMapping("/clientConfig")
    @ResponseBody
    public void downloadFolder(Integer serviceInstanceId, HttpServletResponse response) throws IOException {
        // 查询服务实例
        ServiceInstanceEntity serviceInstanceEntity = serviceInstanceRepository.findById(serviceInstanceId).get();
        String serviceName = serviceInstanceEntity.getServiceName();
        // todo 目前先固定选择第一节点的配置
        String workHome = cloudeonConfigProp.getWorkHome();
        // 拼接路径 服务实例名加workHome
        String folderPath = workHome + File.separator + serviceName;
        // 获取folderPath目录下的第一个目录
        File serviceWorkHome = Arrays.stream(new File(folderPath).listFiles()).filter(File::isDirectory).findFirst().get();
        String confPath = serviceWorkHome.getAbsolutePath() + File.separator + "conf";

        // 创建一个临时zip文件
        String zipFilePath = serviceName + "-client-config.zip";
        FileOutputStream fos = new FileOutputStream(zipFilePath);
        ZipArchiveOutputStream zos = new ZipArchiveOutputStream(fos);

        // 遍历文件夹中的所有文件，并将它们添加到zip文件中
        Path folder = Paths.get(confPath);
        Files.walk(folder)
                .filter(Files::isRegularFile)
                .forEach(file -> {
                    try {
                        String entryName = folder.relativize(file).toString();
                        ZipArchiveEntry entry = new ZipArchiveEntry(file.toFile(), entryName);
                        zos.putArchiveEntry(entry);
                        FileInputStream fis = new FileInputStream(file.toFile());
                        byte[] buffer = new byte[1024];
                        int len;
                        while ((len = fis.read(buffer)) > 0) {
                            zos.write(buffer, 0, len);
                        }
                        fis.close();
                        zos.closeArchiveEntry();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

        zos.finish();
        zos.close();
        fos.close();

        // 设置响应头，告诉浏览器下载文件
        File zipFile = new File(zipFilePath);
        String fileName = zipFile.getName();
        String contentType = "application/zip";
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        response.setContentType(contentType);

        // 将zip文件内容写入响应流
        Files.copy(zipFile.toPath(), response.getOutputStream());
        response.flushBuffer();

        // 删除临时zip文件
        Files.deleteIfExists(zipFile.toPath());
    }
}
