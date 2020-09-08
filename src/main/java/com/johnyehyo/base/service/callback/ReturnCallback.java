package com.johnyehyo.base.service.callback;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * @Description
 * @Author JohnYehyo
 * @Date 2020-09-08 09:51
 * @Version 1.0
 */
@Component
public class ReturnCallback implements RabbitTemplate.ReturnCallback {


    @Override
    public void returnedMessage(Message message, int replyCode, String replyText,
                                String exchange, String routingKey) {
        System.out.println("消息体:" + message);
        System.out.println("code:" + replyCode);
        System.out.println("描述:" + replyText);
        System.out.println("交换器:" + exchange);
        System.out.println("路由键:" + routingKey);
    }
}
