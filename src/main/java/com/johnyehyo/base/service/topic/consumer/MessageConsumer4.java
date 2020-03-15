package com.johnyehyo.base.service.topic.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.johnyehyo.base.entity.UserEntity;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Map;


/**
 * 消息消费者 direct模式 手动确认
 *
 * @author JohnYehyo
 * @date 2020-3-12
 */
@Component
@RabbitListener(bindings = @QueueBinding(
        exchange = @Exchange(value = "topic-exchange",durable = "true",type = ExchangeTypes.TOPIC),
        value = @Queue(value = "topic-queue",durable = "true"),
        key = "user.#"
))
public class MessageConsumer4 {

//    @Autowired
//    private BaseService baseService;

//    private final CountDownLatch listenLatch = new CountDownLatch(1);



    @RabbitHandler
    public void onMessage(@Payload String body, @Headers Map<String, Object> headers, Channel channel) throws Exception {
        System.out.println("body：" + body);
        System.out.println("Headers：" + headers);
        System.out.println("接收到消息: " + body);
//        this.listenLatch.countDown();
        channel.basicAck((Long) headers.get(AmqpHeaders.DELIVERY_TAG), false);
    }
}
