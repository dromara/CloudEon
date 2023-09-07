package org.dromara.cloudeon.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RolePodEventVO {

  String type;
  Integer count;
  String lastTimestamp;
  String reason;
  String message;
}
