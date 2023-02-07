package com.data.udh.processor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UdhTaskContext {
    protected Integer commandTaskId;
    protected Integer commandId;
    protected Integer serviceInstanceId;
}
