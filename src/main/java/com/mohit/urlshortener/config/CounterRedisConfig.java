package com.mohit.urlshortener.config;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.sync.RedisCommands;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CounterRedisConfig {

    private static final String COUNTER_REDIS_URI = "rediss://clustercfg.redis-cache.yq50wk.use1.cache.amazonaws.com:6379";

    @Bean
    public RedisCommands<String, String> counterCommands() {
        RedisClient redisClient = RedisClient.create(COUNTER_REDIS_URI);
        return redisClient.connect().sync();
    }

}
