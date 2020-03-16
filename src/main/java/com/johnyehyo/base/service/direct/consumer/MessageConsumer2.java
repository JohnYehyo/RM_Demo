package com.johnyehyo.base.service.direct.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.johnyehyo.base.entity.UserEntity;
import com.johnyehyo.base.service.BaseService;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Map;


/**
 * 消息消费者 direct模式 手动确认
 *
 * @author JohnYehyo
 * @date 2020-3-12
 */
@Component
//public class MessageConsumer2 implements ChannelAwareMessageListener {
public class MessageConsumer2{

    @Autowired
    private BaseService baseService;


//    @Override
//    public void onMessage(Message message, Channel channel) throws Exception {
//        long tag = message.getMessageProperties().getDeliveryTag();
//        try {
//            String userInfo = new String(message.getBody(), "UTF-8");
//            ObjectMapper mapper = new ObjectMapper();
//            UserEntity user = mapper.readValue(userInfo, UserEntity.class);
//            System.out.println("[1]监听到消息开始业务操作...");
//            //执行业务
//            baseService.submit(user);
////            为了区分消费速度在执行业务后也停顿一下
//            Thread.sleep(1000);
//            //手动确认
//            channel.basicAck(tag, false);
//        } catch (Exception e) {
//            e.printStackTrace();
//            //拒绝接收
//            channel.basicReject(tag, false);
//        }
//    }

    @RabbitListener(queues="direct-queue")
    @RabbitHandler
    public void onMessage(@Payload UserEntity body, @Headers Map<String, Object> headers, Channel channel) throws Exception {
        long tag = (long) headers.get(AmqpHeaders.DELIVERY_TAG);
        try {
            System.out.println("[1]监听到消息开始业务操作...");
            //执行业务
            baseService.submit(body);
//            为了区分消费速度在执行业务后也停顿一下
            Thread.sleep(1000);
            //手动确认
            channel.basicAck(tag, false);
        } catch (Exception e) {
            e.printStackTrace();
            //拒绝接收
            channel.basicReject(tag, false);
        }
    }
}
