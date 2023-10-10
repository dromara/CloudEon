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
package org.dromara.cloudeon.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dromara.cloudeon.entity.CommandTypeConverter;
import org.dromara.cloudeon.enums.CommandState;
import org.dromara.cloudeon.enums.CommandType;

import javax.persistence.Convert;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommandVO {
  private Integer id;

  /**
   * 指令名称
   */
  private String name;

  /**
   * 指令类型
   */
  @Convert(converter = CommandTypeConverter.class)
  private CommandType type;

  /**
   * 指令运行状态
   */
  @Enumerated(EnumType.STRING)
  private CommandState commandState;

  /**
   * 提交时间
   */
  private Date submitTime;

  /**
   * 开始时间
   */
  private Date startTime;

  /**
   * 结束时间
   */
  private Date endTime;

  /**
   * 总进度
   */
  private Integer currentProgress;

  private List<String> serviceNames;
}
