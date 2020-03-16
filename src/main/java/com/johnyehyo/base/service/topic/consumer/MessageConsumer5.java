package com.johnyehyo.base.service.topic.consumer;

import com.johnyehyo.base.entity.UserEntity;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;


/**
 * 消息消费者 topic模式 手动确认
 *
 * @author JohnYehyo
 * @date 2020-3-12
 */
@Component
public class MessageConsumer5 {

    @RabbitListener(bindings = @QueueBinding(
            exchange = @Exchange(value = "topic-exchange", durable = "true", type = ExchangeTypes.TOPIC),
            value = @Queue(value = "topic-queue", durable = "true"),
            key = "#.delete,user.add"
    ))
    @RabbitHandler
    public void onMessage(@Payload UserEntity body, @Headers Map<String, Object> headers, Channel channel){
        Long tag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
        System.out.println("[TOPIC 2]接收到消息: " + body );
        try {
            channel.basicAck(tag, false);
        } catch (IOException e) {
            try {
                channel.basicReject(tag, false);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
    }
}
