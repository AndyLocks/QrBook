package com.locfox.qr_book.account_service.config.redis;

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
public class RedisJwtBlackListConfig {

    @Value("${redis.jwt_black_list.host}")
    private String host;

    @Value("${redis.jwt_black_list.port}")
    private int port;

    private final Logger LOG = LoggerFactory.getLogger(RedisJwtBlackListConfig.class);

    @Bean(name = "jedisForBlackListConnectionFactory")
    @Primary
    public RedisConnectionFactory jedisForBlackListConnectionFactory() {
        LOG.debug("Redis server: {}", host);
        LOG.debug("Redis port: {}", port);

        var configuration = new RedisStandaloneConfiguration(host, port);
        return new JedisConnectionFactory(configuration);
    }

}
