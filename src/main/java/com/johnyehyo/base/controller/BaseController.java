package com.johnyehyo.base.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.johnyehyo.base.entity.UserEntity;
import org.junit.Test;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;


/**
 * @author JohnYehyo
 * @date 2020-3-12
 */
@Controller
@RequestMapping(value = "base")
public class BaseController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RequestMapping(value = "init")
    public String init() {
        return "test";
    }

    /**
     * direct类型
     */
    @RequestMapping(value = "login")
    public void login() {
        UserEntity user = new UserEntity();
        user.setPassword("123456");
        ObjectMapper mapper = new ObjectMapper();
        for (int i = 0; i < 10; i++) {
            user.setUsername("测试王" + UUID.randomUUID().toString().replace("_", ""));
            try {
                String s = mapper.writeValueAsString(user);
                rabbitTemplate.convertAndSend("direct-exchange", "direct-queue-key", s);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * direct类型 使用@RabbitListener监听功能
     */
    @RequestMapping(value = "submit")
    public void submit() {
        UserEntity user = new UserEntity();
        user.setPassword("123456");
        for (int i = 0; i < 30; i++) {
            user.setUsername("测试王" + UUID.randomUUID().toString().replace("_", ""));
            rabbitTemplate.convertAndSend("direct-exchange", "direct-queue-key", user);
        }
    }

    /**
     * topic类型 使用@RabbitListener绑定和监听功能
     */
    @RequestMapping(value = "submitWithReturn")
    public void submitWithReturn() {
        CorrelationData correlationData = new CorrelationData();
        correlationData.setId(UUID.randomUUID().toString().replace("_", ""));
        UserEntity user = new UserEntity();
        user.setUsername("测试王" + UUID.randomUUID().toString().replace("_", ""));
        user.setPassword("123456");
        rabbitTemplate.convertAndSend("topic-exchange", "user.add", user, correlationData);
        System.out.println("执行成功");
    }

}
