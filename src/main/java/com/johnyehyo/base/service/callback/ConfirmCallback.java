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
    private static final Logger logger = LoggerFactory.getLogger(ConfirmCallback.class);


    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (ack) {
//            logger.info("投递成功correlationData--->" + correlationData);
        } else {
            logger.error("投递失败correlationData--->" + correlationData);
            logger.error("cause--->" + cause);
        }
    }
}
