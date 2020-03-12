package com.johnyehyo.base.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.johnyehyo.base.entity.UserEntity;
import com.johnyehyo.base.service.MessageProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author JohnYehyo
 * @date 2020-3-12
 */
@Controller
@RequestMapping(value = "base")
public class BaseController {

    @Autowired
    private MessageProducer messageProducer;

    @RequestMapping(value = "init")
    public String init() {
        return "test";
    }

    @RequestMapping(value = "login")
    public void login() {
        ObjectMapper mapper = new ObjectMapper();
        for (int i = 0; i < 10; i++) {
            doUserService(mapper, "submitKey");
        }
    }

    @RequestMapping(value = "submit")
    public void submit() {
        ObjectMapper mapper = new ObjectMapper();
        for (int i = 0; i < 10; i++) {
            doUserService(mapper, "submitKey");
        }
    }

    /**
     * 创建用户模拟提交信息
     * @param mapper
     * @param messageKey
     */
    private void doUserService(ObjectMapper mapper, String messageKey) {
        UserEntity user;
        user = new UserEntity();
        user.setUsername("测试王"+Math.random());
        user.setPassword("123456");
        try {
            String message = mapper.writeValueAsString(user);
            messageProducer.sendMessage(message, messageKey);
            System.out.println("执行成功");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("执行失败");
        }
    }


}
