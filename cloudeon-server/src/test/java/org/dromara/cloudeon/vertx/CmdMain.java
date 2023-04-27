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

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;

import java.util.Random;

public class CmdMain {
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new CmdVerticle(), new DeploymentOptions()
                    .setWorker(true)
                    .setWorkerPoolName("dedicated-pool")
                    .setMaxWorkerExecuteTime(2000)
                    .setWorkerPoolSize(5)
            )
            .onSuccess(id -> {
              System.out.println("Deployed worker verticle " + id);
            });

    EventBus eventBus = vertx.eventBus();

    vertx.createHttpServer().requestHandler(request -> {
      int i = new Random().nextInt(100);
      eventBus.request("command", i, reply -> {
        if (reply.succeeded()) {
          request.response().putHeader("content-type", "text/plain").end(reply.result().body().toString());
        } else {
          reply.cause().printStackTrace();
        }
      });

      System.out.println(" http server main thread end , request: " + i + " ....");
    }).listen(8080);
  }



}
