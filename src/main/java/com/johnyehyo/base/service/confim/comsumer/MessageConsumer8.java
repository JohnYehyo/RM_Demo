package com.johnyehyo.base.service.confim.comsumer;

import com.johnyehyo.base.entity.UserEntity;
import com.johnyehyo.base.enums.RabbitResult;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description
 * @Author JohnYehyo
 * @Date 2020-09-08 11:20
 * @Version 1.0
 */
@Component
public class MessageConsumer8 {


    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "callback-queue", durable = "true"),
            exchange = @Exchange(value = "test-exchange", durable = "true", type = ExchangeTypes.TOPIC),
            key = "callback.item"
    ))
    @RabbitHandler
    public void onMessage(@Payload UserEntity user, @Headers Map<String, Object> headers, Channel channel) throws IOException {
        Long tag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
        RabbitResult rr = RabbitResult.RETRY;
        try {
            System.out.println("[corfim-back]接收到消息:" + user);
            //漫长的业务操作
            Thread.sleep(3000);
            rr = RabbitResult.SUCCESS;
            System.out.println("[corfim-back]消息消费完毕:" + user);
        } catch (Exception e) {
            rr = RabbitResult.DISCARDED;
            e.printStackTrace();
            System.out.println("[corfim-back]消费异常:"+user);
        } finally {
            if (rr == RabbitResult.SUCCESS) {
                channel.basicAck(tag, false);
            } else if (rr == RabbitResult.RETRY) {
                channel.basicNack(tag, false, true);
            } else {
                channel.basicNack(tag, false, false);
            }
        }

    }
}
