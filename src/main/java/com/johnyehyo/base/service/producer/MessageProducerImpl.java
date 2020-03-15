package com.johnyehyo.base.service.producer;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * 消息生产者
 *
 * @author JohnYehyo
 * @date 2020-3-12
 */
@Component
public class MessageProducerImpl implements MessageProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送消息到指定队列(简单的发送不指定交换机和路由、topic)
     *
     * @param message
     * @throws IOException
     */
    @Override
    public void sendMessage(Object message) throws IOException {
        try {
            rabbitTemplate.convertAndSend(message);
        } catch (AmqpException e) {
            e.printStackTrace();
        }

    }

    /**
     * 发送消息到指定队列(指定路由)
     *
     * @param message
     * @param routingKey
     * @throws IOException
     */
    @Override
    public void sendMessage(Object message, String routingKey) throws IOException {
        try {
            rabbitTemplate.convertAndSend(routingKey, message);
        } catch (AmqpException e) {
            e.printStackTrace();
        }

    }

    /**
     * 发送消息到指定队列(指定路由)
     *
     * @param message
     * @param routingKey
     * @throws IOException
     */
    @Override
    public void sendMessage(Object message, String routingKey, String exchange) throws IOException {
        try {
            rabbitTemplate.convertAndSend(exchange, routingKey, message);
        } catch (AmqpException e) {
            e.printStackTrace();
        }

    }

    /**
     * 发送消息到指定队列(指定路由)
     *
     * @param message
     * @param routingKey
     * @param exchange
     * @param correlationData
     * @throws IOException
     */
    @Override
    public void sendMessage(Object message, String routingKey, String exchange, CorrelationData correlationData) throws IOException {
        try {
            rabbitTemplate.convertAndSend(exchange, routingKey, message, correlationData);
        } catch (AmqpException e) {
            e.printStackTrace();
        }

    }


    /**
     * 发送消息到指定队列(指定路由)
     *
     * @param message
     * @param routingKey
     * @param exchange
     * @param messagePostProcessor
     * @throws IOException
     */
    @Override
    public void sendMessage(Object message, String routingKey, String exchange, MessagePostProcessor messagePostProcessor) throws IOException {
        try {
            rabbitTemplate.convertAndSend(exchange, routingKey, message, messagePostProcessor);
        } catch (AmqpException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送消息到指定队列(指定路由)
     *
     * @param message
     * @param routingKey
     * @param exchange
     * @param messagePostProcessor
     * @param correlationData
     * @throws IOException
     */
    @Override
    public void sendMessage(Object message, String routingKey, String exchange,
                            MessagePostProcessor messagePostProcessor, CorrelationData correlationData) throws IOException {
        try {
            rabbitTemplate.convertAndSend(exchange, routingKey, message, messagePostProcessor, correlationData);
        } catch (AmqpException e) {
            e.printStackTrace();
        }
    }
}
