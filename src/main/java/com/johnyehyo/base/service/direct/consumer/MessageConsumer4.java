package com.johnyehyo.base.service.direct.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.johnyehyo.base.entity.UserEntity;
import com.johnyehyo.base.service.BaseService;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CountDownLatch;


/**
 * 消息消费者 direct模式 手动确认
 * @author JohnYehyo
 * @date 2020-3-12
 */
@Service
public class MessageConsumer4{

    @Autowired
    private BaseService baseService;

    private final CountDownLatch listenLatch = new CountDownLatch(1);

    private static final String QUEUE = "submitKey";

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = QUEUE, durable = "true"),
                    exchange = @Exchange(value = "SpringRabbit-RedPack-Exchange", durable = "true", type = "direct")
            )
    )
    public void onMessage(String message) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        UserEntity user = mapper.readValue(message, UserEntity.class);
        System.out.println("Listener received: " + user);
        this.listenLatch.countDown();
    }
}
