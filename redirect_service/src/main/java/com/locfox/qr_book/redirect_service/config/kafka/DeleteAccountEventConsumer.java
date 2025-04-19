package com.locfox.qr_book.redirect_service.config.kafka;

import com.locfox.qr_book.redirect_service.entity.kafka.DeleteAccountEventMessage;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;

@Configuration
public class DeleteAccountEventConsumer {

    @Value("${kafka.host}")
    private String kafkaAccountsDeleteAccountEventHost;

    @Value("${kafka.group_id}")
    private String groupId;

    @Bean
    public ConsumerFactory<String, DeleteAccountEventMessage> deleteAccountEventMessageConsumerFactory() {
        var config = new HashMap<String, Object>();

        var deserializer = new JsonDeserializer<DeleteAccountEventMessage>(DeleteAccountEventMessage.class);
        deserializer.addTrustedPackages("com.locfox.*");
        deserializer.setUseTypeHeaders(false);

        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaAccountsDeleteAccountEventHost);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer);

        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, DeleteAccountEventMessage>
    deleteAccountEventMessageKafkaListenerContainerFactory(ConsumerFactory<String, DeleteAccountEventMessage> consumerFactory) {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, DeleteAccountEventMessage>();
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }

}
