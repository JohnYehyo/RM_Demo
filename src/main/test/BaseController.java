import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.johnyehyo.base.entity.UserEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
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

    @Test
    public void login() {
        UserEntity user = new UserEntity();
        user.setPassword("123456");
        ObjectMapper mapper = new ObjectMapper();
        for (int i = 0; i < 10; i++) {
            user.setUsername("测试王" + UUID.randomUUID().toString().replace("_", ""));
            try {
                String s = mapper.writeValueAsString(user);
                rabbitTemplate.convertAndSend("direct-exchange", "direct-queue-key", s);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

        }
    }

    @Test
    public void submit() {
        UserEntity user = new UserEntity();
        user.setPassword("123456");
        for (int i = 0; i < 30; i++) {
            user.setUsername("测试王" + UUID.randomUUID().toString().replace("_", ""));
            rabbitTemplate.convertAndSend("direct-exchange", "direct-queue-key", user);
        }
    }

    @Test
    public void submitWithReturn() {
        CorrelationData correlationData = new CorrelationData();
        correlationData.setId(UUID.randomUUID().toString().replace("_", ""));
        UserEntity user = new UserEntity();
        user.setUsername("测试王" + UUID.randomUUID().toString().replace("_", ""));
        user.setPassword("123456");
        rabbitTemplate.convertAndSend("topic-exchange", "user.add", user, correlationData);
        System.out.println("执行成功");
    }
}
