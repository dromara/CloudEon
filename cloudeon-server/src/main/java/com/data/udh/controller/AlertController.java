package com.data.udh.controller;

import com.alibaba.fastjson.JSONObject;
import com.data.udh.dto.AlertMessage;
import com.data.udh.dto.ResultDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@Slf4j
@RestController
@RequestMapping("/alert")
public class AlertController {

    /**
     * 保存
     */
    @RequestMapping("/webhook")
    public ResultDTO<Void> save(@RequestBody String alertMessage){
        AlertMessage alertMes = JSONObject.parseObject(alertMessage, AlertMessage.class);
        log.info("接收alertmanager告警:"+alertMes);
        return ResultDTO.success(null);
    }
}
