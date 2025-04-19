package com.locfox.qr_book.account_service.config.redis.templates;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.Duration;

@Configuration
public class JwtBlackListTemplate {

    @Value("${redis.jwt_black_list.ttl}")
    private static long milliseconds;
    private final Logger LOG = LoggerFactory.getLogger(JwtBlackListTemplate.class);

    public static Duration ttl() {
        return Duration.ofMillis(milliseconds);
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate(@Qualifier("jedisForBlackListConnectionFactory") RedisConnectionFactory connectionFactory) {
        LOG.debug("Redis connection for [StringRedisTemplate]: [{}]", connectionFactory);

        return new StringRedisTemplate(connectionFactory);
    }

}
