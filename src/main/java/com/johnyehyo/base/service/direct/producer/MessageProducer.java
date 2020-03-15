package com.johnyehyo.base.service.direct.producer;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.MessagePostProcessor;

import java.io.IOException;

/**
 * 消息生产者
 * @author JohnYehyo
 * @date 2020-3-12
 */
public interface MessageProducer {

    /**
     * 发送消息到指定队列(简单的发送不指定交换机和路由、topic)
     * @param message
     * @throws IOException
     */
    void sendMessage(Object message)throws IOException;

    /**
     * 发送消息到指定队列(指定路由)
     * @param message
     * @param routingKey
     * @throws IOException
     */
    void sendMessage(Object message, String routingKey)throws IOException;

    /**
     * 发送消息到指定队列(指定路由)
     * @param message
     * @param routingKey
     * @throws IOException
     */
   void sendMessage(Object message, String routingKey, String exchange)throws IOException;


   void sendMessage(Object message, String routingKey, String exchange, MessagePostProcessor messagePostProcessor)throws IOException;

}
