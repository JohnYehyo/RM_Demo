package com.johnyehyo.base.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    /**
     * direct类型
     */
    @RequestMapping(value = "login")
    public void login() {
        UserEntity user = new UserEntity();
        user.setPassword("123456");
        ObjectMapper mapper = new ObjectMapper();
        for (int i = 0; i < 10; i++) {
            user.setUsername("测试王" + UUID.randomUUID().toString().replace("-", ""));
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
            user.setUsername("测试王" + UUID.randomUUID().toString().replace("-", ""));
            rabbitTemplate.convertAndSend("direct-exchange", "direct-queue-key", user);
        }
    }

    /**
     * topic类型 使用@RabbitListener绑定和监听功能
     */
    @RequestMapping(value = "submitWithReturn")
    public void submitWithReturn() {
        CorrelationData correlationData = new CorrelationData();
        correlationData.setId(UUID.randomUUID().toString().replace("-", ""));
        UserEntity user = new UserEntity();
        user.setUsername("测试王" + UUID.randomUUID().toString().replace("-", ""));
        user.setPassword("123456");
        rabbitTemplate.convertAndSend("topic-exchange", "user.add", user, correlationData);
        System.out.println("执行成功");
    }

   final RabbitTemplate.ConfirmCallback confirmCallback = (correlationData, ack, cause) -> {
       System.out.println("correlationData--->"+correlationData);
       System.out.println("ack--->"+ack);
       System.out.println("cause--->"+cause);
       System.out.println(ack);
       if(ack){
           System.out.println("正常投递回复...");
           //后续执行其他业务...
       }else{
           System.out.println("投递异常....");
           //后续记录等操作...
       }
   };

    /**
     * topic-corfim类型 使用@RabbitListener绑定和监听功能
     */
    @RequestMapping(value = "submitConfirm")
    public void submitConfirm() {
        CorrelationData correlationData = new CorrelationData();
        correlationData.setId(UUID.randomUUID().toString().replace("-", ""));
        UserEntity user = new UserEntity();
        user.setUsername("测试订单" + UUID.randomUUID().toString().replace("-", ""));
        user.setPassword("123456");
//        两种皆可
//        confirmProducer.send();
        rabbitTemplate.setConfirmCallback(confirmCallback);
        rabbitTemplate.convertAndSend("topic-exchange", "order.add", user, correlationData);
    }


}
