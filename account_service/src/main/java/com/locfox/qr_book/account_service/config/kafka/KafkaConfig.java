package com.locfox.qr_book.account_service.config.kafka;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;

@Configuration
public class KafkaConfig {

    @Value("${kafka.host}")
    private String host;

    @Bean
    public KafkaAdmin admin() {
        var configs = new HashMap<String, Object>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, host);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic deleteAccountEventTopic() {
        return TopicBuilder.name(Topics.DELETE_ACCOUNT_EVENT_MESSAGE.getName())
                .partitions(5)
                .build();
    }

    @Bean
    public NewTopic updateAccountEventTopic() {
        return TopicBuilder.name(Topics.UPDATE_ACCOUNT_EVENT_MESSAGE.getName())
                .partitions(5)
                .build();
    }

}
