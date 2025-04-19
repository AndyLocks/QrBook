package com.locfox.qr_book.codes_handler.config.redis.templates;

import com.locfox.qr_book.codes_handler.entity.QrCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.time.Duration;
import java.util.function.Function;

@Configuration
public class QrCodeRedisTemplateConfig {

    /// The time after which container records are deleted in redis
    public static final Duration TTL_DURATION = Duration.ofMinutes(5);

    public static final Function<String, String> USER_ID_KEY_GENERATOR =s -> String.format("user_id:%s", s);
    public static final Function<String, String> QR_CODE_KEY_GENERATOR =s -> String.format("qr_code:%s", s);

    private final Logger LOG = LoggerFactory.getLogger(QrCodeRedisTemplateConfig.class);

    @Bean
    public RedisTemplate<String, QrCode> qrCodeRedisTemplate(@Qualifier("jedisConnectionFactory") RedisConnectionFactory redisConnectionFactory) {
        LOG.debug("Redis connection for [RedisTemplate<String, QrCode>]: [{}]", redisConnectionFactory);

        var redisTemplate = new RedisTemplate<String, QrCode>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        redisTemplate.setKeySerializer(RedisSerializer.string());
        redisTemplate.setValueSerializer(RedisSerializer.json());

        return redisTemplate;
    }

}
