# Springboot整合Redis
>Redis是现在最受欢迎的NoSQL数据库之一，key-value形式数据库，基于内存运行，性能高效，应用广泛
## 添加依赖
```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```
## 添加redis配置信息
```yaml
spring:
  redis:
    database: 0
    host: 127.0.0.1
    port: 6379
    password:
    timeout: 6000ms  # 连接超时时长（毫秒）
    jedis:
      pool:
        max-active: 1000  # 连接池最大连接数（使用负值表示没有限制）
        max-wait: -1      # 连接池最大阻塞等待时间（使用负值表示没有限制）
        max-idle: 10      # 连接池中的最大空闲连接
        min-idle: 5       # 连接池中的最小空闲连接
```

## 添加redis配置类
```java
@Configuration
public class RedisConfig {

    @Bean
    @SuppressWarnings("all")
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
        ObjectMapper objectMapper = new ObjectMapper();
        template.setConnectionFactory(factory);
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        // key采用String的序列化方式
        template.setKeySerializer(stringRedisSerializer);
        // hash的key也采用String的序列化方式
        template.setHashKeySerializer(stringRedisSerializer);
        // value序列化方式采用jackson
        template.setValueSerializer(jackson2JsonRedisSerializer);
        // hash的value序列化方式采用jackson
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }
}
```

## 使用示例
```java
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
```
更多方法详见RedisTemplate类，实际项目中我们可以将RedisTemplate再做一次简易的封装，使用起来更顺手