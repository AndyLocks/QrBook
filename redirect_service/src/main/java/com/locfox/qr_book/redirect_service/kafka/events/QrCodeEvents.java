package com.locfox.qr_book.redirect_service.kafka.events;

import com.locfox.qr_book.redirect_service.entity.kafka.DeleteQrCodeEventMessage;
import com.locfox.qr_book.redirect_service.entity.kafka.UpdateQrCodeEventMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class QrCodeEvents {

    private final Logger LOG = LoggerFactory.getLogger(QrCodeEvents.class);

    @CacheEvict(value = "codes", key = "#message.crCodeId().toString()")
    @KafkaListener(topics = "delete_qr_code_events", groupId = "redirect-service", containerFactory = "deleteQrCodeEventMessageConcurrentKafkaListenerContainerFactory")
    public void qrCodeDeleteEvent(DeleteQrCodeEventMessage message) {
    }

    @CacheEvict(value = "codes", key = "#message.uuid().toString()")
    @KafkaListener(topics = "update_qr_code_events", groupId = "redirect-service", containerFactory = "updateQrCodeEventMessageConcurrentKafkaListenerContainerFactory")
    public void qrCodeDeleteEvent(UpdateQrCodeEventMessage message) {
    }

}
