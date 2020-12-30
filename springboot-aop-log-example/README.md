# 基于aop实现系统日志记录
> AOP这个词对大家并不陌生，实际项目当中他能干什么，如何使用？这篇文章就用AOP实现系统请求日志的一个功能

AOP面向切面，对于系统当中我们需要在不同地方做相同的操作时，就可以考虑是否能用AOP解决问题

## 典型的AOP应用场景

- 日志记录
- 权限验证
- @Transactional 事务

通过AOP我们把分布在各个地方的操作统一集中到一个地方去。打个比方：系统需要对某些接口实现性能分析，需要记录接口的响应时间，如何实现？  
很简单，我们只需要在Controller里面调用业务之前记录时间，然后在业务执行之后用当前时间减去前面的时间就行了，代码如下：
```java
@GetMapping("/log")
public ResultEntity doSomething() {
    //开始时间
    long startTime = System.currentTimeMillis();
    try {
        //随机睡眠模拟业务执行
        Thread.sleep(new Random().nextInt(1000));
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
    //结束时间
    long endTime = System.currentTimeMillis();
    //时间差
    long diffTime = endTime - startTime;
    //记录到数据库
    logger.debug("接口处理时间为：{}毫秒", diffTime);
    return ResultEntity.ok();
}
```
## AOP切面实现日志记录

这样的实现没有问题，但是记录一个接口这样去做还好，如果是100个甚至1000个呢？岂不是得重复去写N多次，又或者哪一天，需求改变，要求时间超过3秒的才记录日志，这时候每个地方又得一一修改

现在用AOP来改造下这个功能，其实在redis分布式锁那篇文章已经用过AOP，基本实现都是差不多，无非就是定义注解（注解也并不是必须的）、定义切面、编写逻辑
### 自定义注解
```
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Log {
    /**
     * 模块名称
     * 
     * @return
     */
    String module();

    /**
     * 接口名称
     * 
     * @return
     */
    String info() default "";
}
```
### 日志切面
```java
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
```
### 使用
最后，在需要记录的Controller接口上方打上注解
```java
@Log(module = "日志模块", info = "测试接口")
@GetMapping("/aopLog")
public ResultEntity aopDoSomething() {
    try {
        //随机睡眠模拟业务执行
        Thread.sleep(new Random().nextInt(1000));
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
    return ResultEntity.ok();
}
```
当访问有Log注解的接口，就会进入日志切面，可以看到控制台打出日志
```
记录日志，模块：日志模块，描述：测试接口，用时：346毫秒
```

## 总结
AOP的强大远远不止于此，因为AOP我们可以无侵入的增强某些功能，就像前面提到的权限验证、声明式事务管理等等