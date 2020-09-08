package com.johnyehyo.base.service.callback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(ReturnCallback.class);


    @Override
    public void returnedMessage(Message message, int replyCode, String replyText,
                                String exchange, String routingKey) {
        logger.info("return-1:" + message);
        logger.info("return-2:" + replyCode);
        logger.info("return-3:" + replyText);
        logger.info("return-4:" + exchange);
        logger.info("return-5:" + routingKey);
    }
}
