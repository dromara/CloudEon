package com.data.udh.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServiceInstanceWebUrlVO {


    private String hostnameUrl;
    private String ipUrl;
    private String name;

}
