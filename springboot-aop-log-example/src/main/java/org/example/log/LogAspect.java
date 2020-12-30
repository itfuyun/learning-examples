package org.example.log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 操作日志切面
 * try{
 * try{
 * //@Before
 * method.invoke(..);
 * }finally{
 * //@After
 * }
 * //@AfterReturning
 * }catch(){
 * //@AfterThrowing
 * }
 *
 * @author <a href="mailto:itfuyun@gmail.com">Tanxh</a>
 * @since 1.0
 */
@Aspect
@Component
public class LogAspect {

    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Pointcut("@annotation(org.example.log.Log)")
    public void log() {
    }

    /**
     * 这里我们用的Around 环绕切面，还有Before After等不同场景，从字面意思就能看出之间的区别了
     *
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around(value = "log()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        Log log = method.getAnnotation(Log.class);
        long startTime = System.currentTimeMillis();
        try {
            //获取到锁，继续执行业务
            return joinPoint.proceed();
        } catch (Throwable e) {
            throw e;
        } finally {
            long endTime = System.currentTimeMillis();
            long diffTime = endTime - startTime;
            logger.info("记录日志，模块：{}，描述：{}，用时：{}毫秒", log.module(), log.info(), diffTime);
            //TODO
            //插入数据库操作
        }
    }
}
