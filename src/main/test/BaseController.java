import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.johnyehyo.base.entity.UserEntity;
import com.johnyehyo.base.service.confim.producer.ConfirmProducer;
import com.johnyehyo.base.service.producer.MessageProducer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.UUID;

/**
 * @author JohnYehyo
 * @date 2020-3-15
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-config.xml"})
public class BaseController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ConfirmProducer confirmProducer;

    @Autowired
    private MessageProducer messageProducer;

    /**
     * direct类型
     */
    @Test
    public void login() {
        UserEntity user = new UserEntity();
        user.setPassword("123456");
        ObjectMapper mapper = new ObjectMapper();
        for (int i = 0; i < 10; i++) {
            user.setUsername("测试王" + UUID.randomUUID().toString().replace("-", ""));
            try {
                String s = mapper.writeValueAsString(user);
                rabbitTemplate.convertAndSend("direct-exchange", "direct-queue-key", s);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * direct类型 使用@RabbitListener监听功能
     */
    @Test
    public void submit() {
        UserEntity user = new UserEntity();
        user.setPassword("123456");
        for (int i = 0; i < 30; i++) {
            user.setUsername("测试王" + UUID.randomUUID().toString().replace("-", ""));
            rabbitTemplate.convertAndSend("direct-exchange", "direct-queue-key", user);
        }
    }

    /**
     * topic类型 使用@RabbitListener绑定和监听功能
     */
    @Test
    public void submitWithReturn() {
        CorrelationData correlationData = new CorrelationData();
        correlationData.setId(UUID.randomUUID().toString().replace("-", ""));
        UserEntity user = new UserEntity();
        user.setPassword("123456");
        for (int i = 0; i < 10; i++) {
            user.setUsername("测试张" + i);
            rabbitTemplate.convertAndSend("topic-exchange", "user.add", user, correlationData);
        }
        System.out.println("执行成功");
    }


    final RabbitTemplate.ConfirmCallback confirmCallback = new RabbitTemplate.ConfirmCallback() {
        @Override
        public void confirm(CorrelationData correlationData, boolean ack, String cause) {
            System.out.println("correlationData--->" + correlationData);
            System.out.println(ack);
            if (ack) {
                System.out.println("正常投递回复...");
                //后续执行其他业务...
            } else {
                System.out.println("投递异常....");
                //后续记录等操作...
            }
        }
    };

    /**
     * topic-corfim类型 使用@RabbitListener绑定和监听功能
     * 提下两种皆可
     */
    @Test
    public void submitConfirm() {
        CorrelationData correlationData = new CorrelationData();
        correlationData.setId(UUID.randomUUID().toString().replace("-", ""));
        UserEntity user = new UserEntity();
        user.setUsername("测试订单" + UUID.randomUUID().toString().replace("-", ""));
        user.setPassword("123456");
        rabbitTemplate.setConfirmCallback((correlation, ack, cause) -> {
            System.out.println("correlationData--->" + correlation);
            System.out.println(ack);
            if (ack) {
                System.out.println("正常投递回复...");
                //后续执行其他业务...
            } else {
                System.out.println("投递异常....");
                //后续记录等操作...
            }
        });
        rabbitTemplate.convertAndSend("test-exchange", "order.add", user, correlationData);
        System.out.println("执行成功");
    }

    @Test
    public void submitConfirm2() {
        CorrelationData correlationData = new CorrelationData();
        correlationData.setId(UUID.randomUUID().toString().replace("-", ""));
        UserEntity user = new UserEntity();
        user.setUsername("测试订单" + UUID.randomUUID().toString().replace("-", ""));
        user.setPassword("123456");
        rabbitTemplate.setConfirmCallback((correlation, ack, cause) -> {
            System.out.println("correlationData--->" + correlation);
            System.out.println(ack);
            if (ack) {
                System.out.println("正常投递回复...");
                //后续执行其他业务...
            } else {
                System.out.println("投递异常....");
                //后续记录等操作...
            }
        });
        rabbitTemplate.convertAndSend("test-exchange", "test.add", user, correlationData);
        System.out.println("执行成功");
    }

    @Test
    public void send(){
        confirmProducer.send();
    }

    /**
     * 手动回复
     */
    @Test
    public void submitConfirm3() {
        UserEntity user = new UserEntity();
        user.setUsername("测试订单" + UUID.randomUUID().toString().replace("-", ""));
        user.setPassword("123456");
        messageProducer.submitConfirm("test-exchange", "test.add", user, UUID.randomUUID().toString().replace("-", ""));
        System.out.println("执行成功");
    }
}
