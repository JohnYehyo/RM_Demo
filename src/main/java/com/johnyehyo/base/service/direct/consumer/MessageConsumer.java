package com.johnyehyo.base.service.direct.consumer;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.johnyehyo.base.entity.UserEntity;
import com.johnyehyo.base.service.BaseService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * 消息消费者 direct模式 自动确认
 * @author JohnYehyo
 * @date 2020-3-12
 */
@Service
public class MessageConsumer{

    @Autowired
    private BaseService baseService;

    private static final String QUEUE = "loginKey";
    @RabbitListener(queues = QUEUE)
    public void onMessage(Message message) {
        try {
            String messages = new String(message.getBody(), "UTF-8");
            ObjectMapper objectMapper = new ObjectMapper();
            UserEntity user = objectMapper.readValue(messages, UserEntity.class);
            System.out.println("监听到消息开始业务操作...");
            //业务操作
            baseService.submit(user);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (RuntimeException e){
            e.printStackTrace();
        }
    }
}
