package com.locfox.qr_book.codes_handler.config.kafka;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;

@Configuration
@EnableKafka
public class KafkaConfig {

    @Value("${kafka.host}")
    private String kafkaDeleteQrCodeEventsHost;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        var configs = new HashMap<String, Object>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaDeleteQrCodeEventsHost);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic deleteQrCodeEventsTopic() {
        return TopicBuilder.name(Topics.DELETE_QR_CODE_EVENT.getName())
                .partitions(5)
                .build();
    }

    @Bean
    public NewTopic updateQrCodeEventsTopic() {
        return TopicBuilder.name(Topics.UPDATE_QR_CODE_EVENT.getName())
                .partitions(5)
                .build();
    }

}
