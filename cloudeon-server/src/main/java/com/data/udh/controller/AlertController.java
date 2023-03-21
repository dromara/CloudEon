package com.data.udh.controller;

import com.alibaba.fastjson.JSONObject;
import com.data.udh.dto.AlertMessage;
import com.data.udh.dto.ResultDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/alert")
public class AlertController {

    /**
     * 保存
     */
    @RequestMapping("/webhook")
    public ResultDTO<Void> save(@RequestBody String alertMessage){
        AlertMessage alertMes = JSONObject.parseObject(alertMessage, AlertMessage.class);
        System.out.println(alertMes);
        return ResultDTO.success(null);
    }
}
