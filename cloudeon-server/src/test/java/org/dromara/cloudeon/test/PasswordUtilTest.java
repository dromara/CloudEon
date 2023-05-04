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
package org.dromara.cloudeon.test;

import cn.hutool.crypto.digest.MD5;

import java.security.NoSuchAlgorithmException;

public class PasswordUtilTest {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        // 495c2bbdc8c80d8a16b60311e34667ae
        String passwd = MD5.create().digestHex("admin"+"ioAs7orSYY2fd0PeOgKf907A1l9MwycE");

        System.out.println(passwd);
    }
}
