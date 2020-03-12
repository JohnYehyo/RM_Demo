package com.johnyehyo.base.service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.johnyehyo.base.entity.UserEntity;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * 消息消费者
 * @author JohnYehyo
 * @date 2020-3-12
 */
@Service
public class MessageConsumer implements MessageListener {

    @Autowired
    private BaseService baseService;

    @Override
    public void onMessage(Message message) {
        try {
            String messages = new String(message.getBody(), "UTF-8");
            ObjectMapper objectMapper = new ObjectMapper();
            UserEntity user = objectMapper.readValue(messages, UserEntity.class);
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
