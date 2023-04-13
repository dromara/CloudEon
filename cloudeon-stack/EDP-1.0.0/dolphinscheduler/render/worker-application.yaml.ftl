#
# Licensed to the Apache Software Foundation (ASF) under one or more
# contributor license agreements.  See the NOTICE file distributed with
# this work for additional information regarding copyright ownership.
# The ASF licenses this file to You under the Apache License, Version 2.0
# (the "License"); you may not use this file except in compliance with
# the License.  You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#
spring:
  banner:
    charset: UTF-8
  application:
    name: worker-server
  jackson:
    time-zone: UTC
    date-format: "yyyy-MM-dd HH:mm:ss"
  datasource:
    driver-class-name: org.postgresql.Driver
    url: jdbc:postgresql://127.0.0.1:5432/dolphinscheduler
    username: root
    password: root
    hikari:
      connection-test-query: select 1
      minimum-idle: 5
      auto-commit: true
      validation-timeout: 3000
      pool-name: DolphinScheduler
      maximum-pool-size: 50
      connection-timeout: 30000
      idle-timeout: 600000
      leak-detection-threshold: 0
      initialization-fail-timeout: 1

registry:
  type: zookeeper
  zookeeper:
    namespace: dolphinscheduler
    connect-string: localhost:2181
    retry-policy:
      base-sleep-time: 60ms
      max-sleep: 300ms
      max-retries: 5
    session-timeout: 30s
    connection-timeout: 9s
    block-until-connected: 600ms
    digest: ~

worker:
  # worker listener port
  listen-port: ${conf['worker.server.listener.port']}
  # worker execute thread number to limit task instances in parallel
  exec-threads: 100
  # worker heartbeat interval
  heartbeat-interval: 10s
  # Worker heart beat task error threshold, if the continuous error count exceed this count, the worker will close.
  heartbeat-error-threshold: 5
  # worker host weight to dispatch tasks, default value 100
  host-weight: 100
  # worker tenant auto create
  tenant-auto-create: true
  # worker max cpuload avg, only higher than the system cpu load average, worker server can be dispatched tasks. default value -1: the number of cpu cores * 2
  max-cpu-load-avg: -1
  # worker reserved memory, only lower than system available memory, worker server can be dispatched tasks. default value 0.3, the unit is G
  reserved-memory: 0.3
  # for multiple worker groups, use hyphen before group name, e.g.
  # groups:
  #   - default
  #   - group1
  #   - group2
  groups:
    - default
  # alert server listen host
  alert-listen-host: ${serviceRoles['DS_ALERT_SERVER'][0].hostname}
  alert-listen-port: ${conf['alert.rpc.port']}

server:
  port: ${conf['worker.server.port']}

management:
  endpoints:
    web:
      exposure:
        include: health,metrics,prometheus
  endpoint:
    health:
      enabled: true
      show-details: always
  health:
    db:
      enabled: true
    defaults:
      enabled: false
  metrics:
    tags:
      application: ${r"${spring.application.name}"}

metrics:
  enabled: true

# Override by profile

---
spring:
  config:
    activate:
      on-profile: mysql
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: ${conf['jdbc.mysql.address']}
    username: ${conf['jdbc.mysql.username']}
    password: ${conf['jdbc.mysql.password']}
