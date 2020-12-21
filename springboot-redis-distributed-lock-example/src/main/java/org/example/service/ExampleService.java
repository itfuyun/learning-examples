package org.example.service;

import org.example.lock.Lock;
import org.springframework.stereotype.Service;

/**
 * lock业务
 *
 * @author <a href="mailto:itfuyun@gmail.com">Tanxh</a>
 * @since 1.0
 */
@Service
public class ExampleService {

    @Lock(lockKey = "testKey", expire = 10)
    public String testLock() {
        try {
            //睡眠1秒，模拟业务执行时间
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "ok";
    }


}
