package com.wang.springboot.controller;

import com.wang.springboot.impl.IMessageProviderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lucksoul 王吉慧
 * @version 1.0
 * @date 2021-01-17 14:10
 */
@RestController
public class SendMessageController {

    @Autowired
    private IMessageProviderImpl messageProvider;

    @GetMapping(value = "/sendMessage")
    public String sendMessage(){
        return messageProvider.send();
    }


}
