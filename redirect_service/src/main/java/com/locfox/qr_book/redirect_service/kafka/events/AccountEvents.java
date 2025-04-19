package com.locfox.qr_book.redirect_service.kafka.events;

import com.locfox.qr_book.redirect_service.entity.kafka.DeleteAccountEventMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class AccountEvents {

    private final Logger LOG = LoggerFactory.getLogger(AccountEvents.class);
    private RedisConnectionFactory redisConnectionFactory;

    @Autowired
    public AccountEvents(RedisConnectionFactory redisConnectionFactory) {
        this.redisConnectionFactory = redisConnectionFactory;
    }

    @KafkaListener(topics = "delete_account_events", groupId = "redirect-service", containerFactory = "deleteAccountEventMessageKafkaListenerContainerFactory")
    public void accountDeleteEvent(DeleteAccountEventMessage message) {
        redisConnectionFactory.getConnection().serverCommands().flushAll();
        LOG.debug("All redis data was deleted");
    }

}
