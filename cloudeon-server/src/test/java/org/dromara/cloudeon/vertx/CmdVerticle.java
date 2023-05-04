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
package org.dromara.cloudeon.vertx;

import io.vertx.core.AbstractVerticle;

public class CmdVerticle extends AbstractVerticle {
  // 接收command消息，然后用blockexecute执行，并且要同时执行多个command
  @Override
  public void start() {
    vertx.eventBus().consumer("command", message -> {
      Object actionId = message.body();
      // 1. 从message中获取command
      vertx.executeBlocking(future -> {
        System.out.println("开始处理命令：" + actionId);
        // Imagine this was a call to a blocking API to get the result
        try {
          Thread.sleep(5000);
        } catch (Exception ignore) {
        }
        String result = "ok!!! " + actionId;

        System.out.println("完成处理命令：" + result);
        future.complete(result);
      }, false, res -> {
        if (res.succeeded()) {
          message.reply(res.result());
        } else {
          message.fail(1, res.cause().getMessage());
        }
      });
      System.out.println("CmdVerticle end .... "+actionId);
    });
  }
}
