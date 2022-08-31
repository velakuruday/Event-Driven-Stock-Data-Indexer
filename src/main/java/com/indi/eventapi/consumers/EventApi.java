package com.indi.eventapi.consumers;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import com.indi.eventapi.services.UserUpdateIndexer;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;

@Component
@AllArgsConstructor
public class EventApi {
    private final UserUpdateIndexer indexer;

    private final ElasticsearchClient esClient;

    @KafkaListener(groupId = "user-update", topics = "user.Updates", containerFactory = "containerFactory")
    public void consume(@Payload String message, Acknowledgment ack) {
        indexer.indexUserUpdate(message, ack);
        ack.acknowledge();
        RequestContextHolder.resetRequestAttributes();
    }
}