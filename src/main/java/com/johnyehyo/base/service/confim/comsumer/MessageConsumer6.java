package com.johnyehyo.base.service.confim.comsumer;

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
public class MessageConsumer6 {

    @RabbitListener(bindings = @QueueBinding(
            exchange = @Exchange(value = "topic-exchange", durable = "true", type = ExchangeTypes.TOPIC),
            value = @Queue(value = "topic-queue", durable = "true"),
            key = "order.#"
    ))
    @RabbitHandler
    public void onMessage(@Payload UserEntity body, @Headers Map<String, Object> headers, Channel channel) throws IOException {
        Long tag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
        try {
            System.out.println("[corfim]接收到消息: " + body);
            //后续业务处理...
            channel.basicAck(tag, false);
        } catch (Exception e) {
            System.out.println("异常...");
            e.printStackTrace();
            channel.basicNack(tag, false,false);
        }

    }
}
