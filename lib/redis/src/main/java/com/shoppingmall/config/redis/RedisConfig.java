package com.shoppingmall.config.redis;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@EnableCaching
@Configuration
public class RedisConfig extends CachingConfigurerSupport {
    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.password}")
    private String password;

    @Value("${spring.redis.port}")
    private int redisPort;

//    @Bean
//    public RedisCommands<String, Object> redisSyncCommands() {
//        RedisClient redisClient = RedisClient.create("redis://" + password + "@" + redisHost + ":" + redisPort);
//        StatefulRedisConnection<String, Object> connection = redisClient.connect(new SerializedObjectCodec());
//
//        return connection.sync();
//    }

    @Bean
    @ConditionalOnMissingBean(RedisConnectionFactory.class)
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(redisHost);
        redisStandaloneConfiguration.setPassword(password);
        redisStandaloneConfiguration.setPort(redisPort);

        return new LettuceConnectionFactory(redisStandaloneConfiguration);
    }

    @Bean(name = "redisCustomTemplate")
    public RedisTemplate<String, Object> redisTemplate() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT);
        objectMapper.registerModule(new JavaTimeModule());

        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer(objectMapper));
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer(objectMapper));

        return redisTemplate;
    }

    @Bean
    public ZSetOperations<String, Object> zSetOperations() {
        return redisTemplate().opsForZSet();
    }

    @Bean
    public ValueOperations<String, Object> valueOperations() { return redisTemplate().opsForValue(); }

//    @Bean
//    @Override
//    public CacheManager cacheManager() {
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
//        objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT);
//        objectMapper.registerModule(new JavaTimeModule());
//
//        RedisCacheManager.RedisCacheManagerBuilder builder = RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(redisConnectionFactory());
//        RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig()
//                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer(objectMapper)))
//                .prefixKeysWith("redis-cache:");
//
//        builder.cacheDefaults(configuration);
//        return builder.build();
//    }
}
