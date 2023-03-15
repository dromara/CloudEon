package com.data.udh.controller.request;

import lombok.Data;

@Data
public class SaveNodeRequest {
    private String sshUser;
    private String sshPassword;
    private Integer sshPort;
    private Integer clusterId;
    private String ip;
}
