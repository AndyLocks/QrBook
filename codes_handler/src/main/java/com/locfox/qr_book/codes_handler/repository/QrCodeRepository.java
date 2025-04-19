package com.locfox.qr_book.codes_handler.repository;

import com.locfox.qr_book.codes_handler.config.kafka.Topics;
import com.locfox.qr_book.codes_handler.config.redis.templates.QrCodeRedisTemplateConfig;
import com.locfox.qr_book.codes_handler.entity.QrCode;
import com.locfox.qr_book.codes_handler.entity.kafka.DeleteQrCodeEventMessage;
import com.locfox.qr_book.codes_handler.entity.kafka.UpdateQrCodeEventMessage;
import com.locfox.qr_book.codes_handler.repository.jpa.QrCodeJpaRepository;
import io.jsonwebtoken.lang.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class QrCodeRepository {

    private RedisTemplate<String, QrCode> redisTemplate;
    private QrCodeJpaRepository jpaRepository;
    private final Logger LOG = LoggerFactory.getLogger(QrCodeRepository.class);
    private KafkaTemplate<String, DeleteQrCodeEventMessage> deleteQrCodeEventMessageKafkaTemplate;
    private KafkaTemplate<String, UpdateQrCodeEventMessage> updateQrCodeEventMessageKafkaTemplate;
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public QrCodeRepository(RedisTemplate<String, QrCode> redisTemplate, QrCodeJpaRepository jpaRepository, KafkaTemplate<String, DeleteQrCodeEventMessage> deleteQrCodeEventMessageKafkaTemplate, KafkaTemplate<String, UpdateQrCodeEventMessage> updateQrCodeEventMessageKafkaTemplate, JdbcTemplate jdbcTemplate) {
        this.redisTemplate = redisTemplate;
        this.jpaRepository = jpaRepository;
        this.deleteQrCodeEventMessageKafkaTemplate = deleteQrCodeEventMessageKafkaTemplate;
        this.updateQrCodeEventMessageKafkaTemplate = updateQrCodeEventMessageKafkaTemplate;
        this.jdbcTemplate = jdbcTemplate;
    }

    /// Creates a new QR Code in the database and returns the created account
    ///
    /// @param code cant be `null`
    /// @return the saved QR Code. Will never be `null`.
    /// @throws IllegalArgumentException if `code` is `null`
    public QrCode add(QrCode code) {
        return jpaRepository.save(Assert.notNull(code, "Qr Code cannot be null"));
    }

    /// Returns [QrCode] from the database or redis found by the uuid
    ///
    /// @param uuid cant be `null`
    /// @return `QrCode` from database or redis
    /// @throws IllegalArgumentException if uuid is null
    public Optional<QrCode> find(UUID uuid) {
        var key = QrCodeRedisTemplateConfig.QR_CODE_KEY_GENERATOR.apply(Assert.notNull(uuid, "UUID cannot be null").toString());

        var qrCodeFromRedis = redisTemplate.opsForValue().get(key);

        if (qrCodeFromRedis == null) {
            var qrCodeFromJpa = jpaRepository.findById(uuid);
            qrCodeFromJpa.ifPresent(c -> redisTemplate.opsForValue()
                    .set(key, c, QrCodeRedisTemplateConfig.TTL_DURATION));
            return qrCodeFromJpa;
        }

        return Optional.of(qrCodeFromRedis);
    }

    /// Finds all [QrCode] belonging to the user found by `userId`.
    ///
    /// @param userId cannot be `null`
    /// @return a list of all [QrCode] belonging to the user found by `userId`.
    /// @throws IllegalArgumentException if `userId` is `null`
    public List<QrCode> findAll(Long userId) {
        return jpaRepository.findAll(QrCode.exampleWithOwnerId(Assert.notNull(userId, "User id cannot be null")));
    }

    /// Deletes all [QrCode] belonging to the user found by `userId`.
    ///
    /// @param userId cannot be `null`
    /// @throws IllegalArgumentException if `userId` is `null`
    public void deleteAll(Long userId) {
        LOG.debug("Clear all cache for user [{}]", userId);
        findAll(Assert.notNull(userId, "User id cannot be null")).forEach(q -> redisTemplate.delete(QrCodeRedisTemplateConfig.QR_CODE_KEY_GENERATOR.apply(q.getId().toString())));
        LOG.debug("Delete all codes for user [{}]", userId);
        jdbcTemplate.update("DELETE FROM qr_code where owner_id=?", userId);
    }

    /// Deletes [QrCode] found by its `uuid`.
    ///
    /// @param uuid cannot be `null`
    /// @throws IllegalArgumentException if `uuid` is `null`
    public void delete(UUID uuid) {
        jpaRepository.deleteById(Assert.notNull(uuid, "UUID cannot be null"));
        redisTemplate.delete(QrCodeRedisTemplateConfig.QR_CODE_KEY_GENERATOR.apply(uuid.toString()));
        deleteQrCodeEventMessageKafkaTemplate.send(Topics.DELETE_QR_CODE_EVENT.getName(), DeleteQrCodeEventMessage.from(uuid));
    }

    /// Deletes [QrCode] found by its `uuid` ([QrCode#getId()]).
    ///
    /// @param qrCode can be `null`
    /// @throws IllegalArgumentException if `uuid` ([QrCode#getId()]) is `null`
    public void delete(QrCode qrCode) {
        this.delete(qrCode.getId());
    }

    /// Updates [QrCode] found by `qrCodeId`
    ///
    /// @param qrCodeId cannot be `null`
    /// @param newUrl cannot be `null`
    public void update(UUID qrCodeId, String newUrl) {
        jpaRepository.findById(Assert.notNull(qrCodeId, "Qr Code cannot be null")).ifPresent(qr -> {
            redisTemplate.delete(QrCodeRedisTemplateConfig.QR_CODE_KEY_GENERATOR.apply(qrCodeId.toString()));

            updateQrCodeEventMessageKafkaTemplate.send(Topics.UPDATE_QR_CODE_EVENT.getName(),
                    new UpdateQrCodeEventMessage(qr.getId(), QrCode.builder()
                            .id(qr.getId())
                            .url(newUrl)
                            .ownerId(qr.getOwnerId())
                            .build()));

            qr.setUrl(newUrl);
            jpaRepository.save(qr);
        });
    }

}
