package com.indi.eventapi.consumers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.indi.eventapi.dto.UserUpdateDto;
import com.indi.eventapi.services.UserUpdateIndexer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;

@Service
@AllArgsConstructor
@Slf4j
public class EventApi {
    private final UserUpdateIndexer indexer;

    @KafkaListener(
            groupId = "user-update",
            topics = "user.Updates",
            containerFactory = "containerFactory"
    )
    public void consume(@Payload String message, Acknowledgment ack) {
            indexer.indexUserUpdate(message, ack);
            ack.acknowledge();
            RequestContextHolder.resetRequestAttributes();
    }
}