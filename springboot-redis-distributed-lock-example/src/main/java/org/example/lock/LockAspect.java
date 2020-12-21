package org.example.lock;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 分布式锁切面
 *
 * @author <a href="mailto:itfuyun@gmail.com">Tanxh</a>
 * @since 1.0
 */
@Aspect
@Component
public class LockAspect implements Ordered {
    private static final Logger log = LoggerFactory.getLogger(LockAspect.class);
    /**
     * 键名前缀
     */
    private static final String DISTRIBUTED_LOCK_PREFIX = "DistributedLock:";

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Pointcut("@annotation(org.example.lock.Lock)")
    public void distributedLock() {
    }

    @Around("distributedLock()")
    public Object distributedLockAround(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        Lock lock = method.getAnnotation(Lock.class);
        String lockKey = lock.lockKey();
        long expire = lock.expire();
        //生成一个唯一值uuid
        String uuid = uuid();
        //尝试获取锁
        boolean lockFlag = tryLock(lockKey, uuid, expire);
        if (lockFlag) {
            log.info("开始获取分布式锁，lockKey：[{}]，value：[{}]", lockKey, uuid);
            try {
                //获取到锁，继续执行业务
                return joinPoint.proceed();
            } catch (Throwable e) {
                throw e;
            } finally {
                //释放分布式锁
                log.info("释放分布式锁，lockKey：[{}]，value：[{}]", lockKey, uuid);
                unlock(lockKey, uuid);
            }
        } else {
            //获取分布式锁失败
            log.info("获取分布式锁失败，lockKey：[{}]，value：[{}]", lockKey, uuid);
            throw new Exception("Distributed Lock Error, LockKey is [" + lockKey + "]");
        }
    }

    /**
     * 优先级最低 order越小优先级越高
     *
     * @return
     */
    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    }

    /**
     * 获取锁
     *
     * @param lockKey
     * @param value
     * @param expire
     * @return
     */
    private boolean tryLock(String lockKey, String value, long expire) {
        return redisTemplate.opsForValue().setIfAbsent(getKey(lockKey), value, expire, TimeUnit.SECONDS);
    }

    /**
     * 释放锁
     *
     * @param lockKey
     * @param value
     */
    private void unlock(String lockKey, String value) {
        String key = getKey(lockKey);
        Object obj = redisTemplate.opsForValue().get(key);
        if (obj != null && value.equals(obj.toString())) {
            //删除
            redisTemplate.delete(key);
        }
    }

    /**
     * 获取键
     *
     * @param lockKey
     * @return
     */
    private String getKey(String lockKey) {
        return DISTRIBUTED_LOCK_PREFIX + lockKey;
    }

    /**
     * uuid
     *
     * @return
     */
    private String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
