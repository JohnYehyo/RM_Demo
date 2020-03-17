package com.johnyehyo.base.service.confim.producer;

import com.johnyehyo.base.entity.UserEntity;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author JohnYehyo
 * @date 2020-3-17
 */
@Service
public class ConfirmProducer implements RabbitTemplate.ConfirmCallback {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void send(){
        CorrelationData correlationData = new CorrelationData();
        correlationData.setId(UUID.randomUUID().toString().replace("-", ""));
        UserEntity user = new UserEntity();
        user.setUsername("测试订单" + UUID.randomUUID().toString().replace("-", ""));
        user.setPassword("123456");
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.convertAndSend("topic-exchange123", "order.add", user, correlationData);

    }


    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        System.out.println("correlationData--->"+correlationData);
        System.out.println(ack);
        if(ack){
            System.out.println("正常投递回复...");
            //后续执行其他业务...
        }else{
            System.out.println("投递异常....");
            //后续记录等操作...
        }
    }
}
