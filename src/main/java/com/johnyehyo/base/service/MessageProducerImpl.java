package com.johnyehyo.base.service;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * 消息生产者
 * @author JohnYehyo
 * @date 2020-3-12
 */
@Service
public class MessageProducerImpl implements MessageProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送消息到指定队列
     * @param message
     * @param mesageKey
     * @throws IOException
     */
    @Override
    public void sendMessage(Object message, String mesageKey)throws IOException {
        try {
            rabbitTemplate.convertAndSend(mesageKey, message);
        } catch (AmqpException e) {
            e.printStackTrace();
        }

    }

}
