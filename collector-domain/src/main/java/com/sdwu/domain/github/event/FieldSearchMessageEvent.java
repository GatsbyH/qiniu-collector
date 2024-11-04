package com.sdwu.domain.github.event;

import com.sdwu.types.event.BaseEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class FieldSearchMessageEvent extends BaseEvent<FieldSearchMessageEvent.FieldSearchMessage> {
    @Value("${spring.rabbitmq.topic.field}")
    private String topic;
    @Override
    public EventMessage<FieldSearchMessage> buildEventMessage(FieldSearchMessage data) {
        return EventMessage.<FieldSearchMessage>builder()
                .id(RandomStringUtils.randomNumeric(11))
                .timestamp(new Date())
                .data(data)
                .build();
    }

    @Override
    public String topic() {
        return topic;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FieldSearchMessage {
        private String field;
    }
}
