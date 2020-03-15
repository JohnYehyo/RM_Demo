package com.johnyehyo.base.service;

import com.johnyehyo.base.entity.UserEntity;
import org.springframework.stereotype.Service;

/**
 * @author JohnYehyo
 * @date 2020-3-12
 */
@Service
public class BaseServiceImpl implements BaseService {

    @Override
    public void submit(UserEntity user) {
        try {
            Thread.sleep(1000);
            System.out.println(user);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("处理失败");
            throw new RuntimeException("处理业务异常");
        }
    }
}
