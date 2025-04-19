package com.locfox.qr_book.account_service.config.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

import java.time.Duration;

@Configuration
public class RedisConfig {

    @Value("${redis.host}")
    private String host;

    @Value("${redis.port}")
    private int port;

    private final Logger LOG = LoggerFactory.getLogger(RedisConfig.class);

    public final static Duration TTL = Duration.ofHours(1);

    @Bean(name = "jedisConnectionFactory")
    public RedisConnectionFactory jedisConnectionFactory() {
        LOG.info("Redis server: {}", host);
        LOG.info("Redis port: {}", port);

        var configuration = new RedisStandaloneConfiguration(host, port);
        return new JedisConnectionFactory(configuration);
    }

}
