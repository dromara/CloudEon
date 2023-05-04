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
package org.dromara.cloudeon.utils;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.io.FileUtil;

import java.io.File;

public class ImageUtil {

    /**
     * 读取图片转base64
     * @param imgFile
     * @return
     */
    public static String GetImageStr(String imgFile) {//将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        // 读取图片文件
        File imageFile = new File(imgFile);
        // 将图片文件转换为 Base64 编码
        String base64 = Base64.encode(FileUtil.readBytes(imageFile));
        // 输出 Base64 编码
        return base64;
    }

    public static void main(String[] args) {
        System.out.println(GetImageStr("/Users/huzekang/openSource/e-mapreduce/cloudeon-stack/UDH-1.0.0/hdfs/icons/app.png"));;
    }

}
