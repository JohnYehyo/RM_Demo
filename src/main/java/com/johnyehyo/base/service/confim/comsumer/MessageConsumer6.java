package com.johnyehyo.base.service.confim.comsumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.johnyehyo.base.entity.UserEntity;
import com.johnyehyo.base.enums.RabbitResult;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
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
            value = @Queue(value = "rj2-queue", durable = "true"),
            exchange = @Exchange(value = "test-exchange", durable = "true", type = ExchangeTypes.TOPIC),
            key = "order.*"
    ))
    @RabbitHandler
    public void onMessage(@Payload UserEntity body, @Headers Map<String, Object> headers, Channel channel) throws IOException {

//        tips:
//        生产环境应该区分本身有误的消息体,这类消息体应该直接丢弃并记录日志而不应该再执行重入队列
//        的过程,对于其它允许重试的消息,通过记录尝试次数当尝试次数达到设定值时同样丢弃并记录日志

        Long tag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
        String RECEIVED_EXCHANGE = (String) headers.get(AmqpHeaders.RECEIVED_EXCHANGE);
        String RECEIVED_ROUTING_KEY = (String) headers.get(AmqpHeaders.RECEIVED_ROUTING_KEY);
        ObjectMapper mapper = new ObjectMapper();
        byte[] bytes = mapper.writeValueAsBytes(body);
        RabbitResult rr = RabbitResult.RETRY;
        try {
            System.out.println("[corfim222]接收到消息: " + body);
            //后续业务处理...
            if(true){
                //业务处理成功
                rr = RabbitResult.SUCCESS;
            }
            Thread.sleep(3000);
        } catch (Exception e) {
            rr = RabbitResult.DISCARDED;
            System.out.println("异常...");
            e.printStackTrace();
        }finally {
            if(rr == RabbitResult.SUCCESS){
                channel.basicAck(tag, false);
            }else if(rr == RabbitResult.RETRY){
//                tips:
//                第一种方法当消息回滚到消息队列时，这条消息不会回到队列尾部，而是仍是在队列头部会造成循
//                环阻塞消息堆积,可以采用依然手动应答这时消息队列会删除该消息,同时再将消息发送到队列消息
//                便回归尾部
                //1
                channel.basicNack(tag, false, true);
                //2
//                channel.basicAck(tag, false);
//                channel.basicPublish(RECEIVED_EXCHANGE,
//                        RECEIVED_ROUTING_KEY, MessageProperties.PERSISTENT_TEXT_PLAIN,
//                        bytes);
            }else{
                channel.basicNack(tag, false, false);
            }
        }
    }
}
