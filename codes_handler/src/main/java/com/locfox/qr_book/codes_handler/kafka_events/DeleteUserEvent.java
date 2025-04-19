package com.locfox.qr_book.codes_handler.kafka_events;

import com.locfox.qr_book.codes_handler.entity.kafka.DeleteAccountEventMessage;
import com.locfox.qr_book.codes_handler.repository.QrCodeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class DeleteUserEvent {

    private QrCodeRepository repository;
    private final Logger LOG = LoggerFactory.getLogger(DeleteUserEvent.class);

    @Autowired
    public DeleteUserEvent(QrCodeRepository repository) {
        this.repository = repository;
    }

    @KafkaListener(topics = "delete_account_events", groupId = "codes-handler", containerFactory = "deleteAccountEventMessageKafkaListenerContainerFactory")
    public void deleteUserEvent(DeleteAccountEventMessage deleteAccountEventMessage) {
        LOG.debug("Kafka delete account event: [{}]", deleteAccountEventMessage);
        repository.deleteAll(deleteAccountEventMessage.id());
    }

}
