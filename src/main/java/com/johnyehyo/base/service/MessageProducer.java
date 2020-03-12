package com.johnyehyo.base.service;

import java.io.IOException;

/**
 * 消息生产者
 * @author JohnYehyo
 * @date 2020-3-12
 */
public interface MessageProducer {

    /**
     * 发送消息到指定队列
     * @param message
     * @param mesageKey
     */
    void sendMessage(Object message, String mesageKey)throws IOException;

}
