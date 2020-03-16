package com.johnyehyo.base.controller;

import com.johnyehyo.base.entity.UserEntity;
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
