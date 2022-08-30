package com.indi.eventapi.services;

import com.indi.eventapi.dto.UserUpdateDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class UserUpdateIndexer {

    public void indexUserUpdate(UserUpdateDto userUpdate, Acknowledgment ack) {

        log.info("Processed subscription update of user: {}" + userUpdate.getName());

        ack.acknowledge();
    }

}
