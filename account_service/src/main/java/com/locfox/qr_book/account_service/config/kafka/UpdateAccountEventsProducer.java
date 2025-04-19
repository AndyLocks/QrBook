package com.locfox.qr_book.account_service.config.kafka;

import com.locfox.qr_book.account_service.entity.kafka.UpdateAccountEventMessage;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;

@Configuration
public class UpdateAccountEventsProducer {

    @Value("${kafka.host}")
    private String kafkaHost;

    @Bean
    public ProducerFactory<String, UpdateAccountEventMessage> updateAccountEventMessageProducerFactory() {
        var config = new HashMap<String, Object>();

        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaHost);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean
    public KafkaTemplate<String, UpdateAccountEventMessage> updateAccountEventMessageKafkaTemplate() {
        return new KafkaTemplate<>(updateAccountEventMessageProducerFactory());
    }

}
