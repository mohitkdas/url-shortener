package com.mohit.urlshortener.config;

import io.lettuce.core.RedisURI;
import io.lettuce.core.cluster.RedisClusterClient;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.cluster.api.sync.RedisAdvancedClusterCommands;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisConfig {

    private static final String REDIS_URI = "rediss://clustercfg.redis-cache.yq50wk.use1.cache.amazonaws.com:6379";

    @Bean
    public RedisAdvancedClusterCommands<String, String> redisCommands() {
        RedisURI redisURI = RedisURI.create(REDIS_URI);

        RedisClusterClient redisClient = RedisClusterClient.create(redisURI);

        StatefulRedisClusterConnection<String, String> connection = redisClient.connect();

        return connection.sync();
    }

}
