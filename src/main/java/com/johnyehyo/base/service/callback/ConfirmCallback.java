package com.johnyehyo.base.service.callback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * @Description
 * @Author JohnYehyo
 * @Date 2020-09-08 09:52
 * @Version 1.0
 */
@Component
public class ConfirmCallback implements RabbitTemplate.ConfirmCallback {


    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (ack) {
            System.out.println("投递成功correlationData--->" + correlationData);
        } else {
            //业务处理
            System.out.println("投递失败correlationData--->" + correlationData);
            System.out.println("cause--->" + cause);
        }
    }
}
