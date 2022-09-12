package com.indi.eventapi.configuration;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class KafkaTestUtils {

    @Bean
    public KafkaProducer<String, String> kafkaProducer(KafkaProperties kafkaProperties) {
        var config = kafkaProperties.buildProducerProperties();
        return new KafkaProducer<>(config);
    }
}
