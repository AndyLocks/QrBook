package com.locfox.qr_book.codes_handler.config.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

@Configuration
public class RedisConfig {

    @Value("${redis.host}")
    private String redisServer;

    @Value("${redis.port}")
    private int redisPort;

    private final Logger LOG = LoggerFactory.getLogger(RedisConfig.class);

    @Bean(name = "jedisConnectionFactory")
    @Primary
    public RedisConnectionFactory jedisConnectionFactory() {
        LOG.debug("Redis server: {}", redisServer);
        LOG.debug("Redis port: {}", redisPort);

        var configuration = new RedisStandaloneConfiguration(redisServer, redisPort);
        return new JedisConnectionFactory(configuration);
    }

}
