package org.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * redis测试业务
 *
 * @author <a href="mailto:itfuyun@gmail.com">Tanxh</a>
 * @since 1.0
 */
@Service
public class ExampleService {
    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    /**
     * set一个值
     *
     * @param key
     * @param value
     * @return
     */
    public void set(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 查询值
     *
     * @param key
     * @return
     */
    public String get(String key) {
        Object object = redisTemplate.opsForValue().get(key);
        if (object == null) {
            return "";
        }
        return object.toString();
    }

    /**
     * 删除
     *
     * @param key
     */
    public void del(String key) {
        redisTemplate.delete(key);
    }
}
