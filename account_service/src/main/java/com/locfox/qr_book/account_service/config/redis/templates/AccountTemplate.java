package com.locfox.qr_book.account_service.config.redis.templates;

import com.locfox.qr_book.account_service.entity.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.time.Duration;

@Configuration
public class AccountTemplate {

    /// The time after which container records are deleted in redis
    public static final Duration TTL_DURATION = Duration.ofMinutes(5);
    private final Logger LOG = LoggerFactory.getLogger(AccountTemplate.class);

    @Bean
    public RedisTemplate<String, Account> accountRedisTemplate(@Qualifier("jedisConnectionFactory") RedisConnectionFactory redisConnectionFactory) {
        LOG.debug("Redis connection for [RedisTemplate<String, Account>]: [{}]", redisConnectionFactory);

        var redisTemplate = new RedisTemplate<String, Account>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        redisTemplate.setKeySerializer(RedisSerializer.string());
        redisTemplate.setValueSerializer(RedisSerializer.json());

        return redisTemplate;
    }

}
