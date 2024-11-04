package com.sdwu.trigger.listener;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.sdwu.domain.github.event.FieldSearchMessageEvent;
import com.sdwu.domain.github.service.ITalentRankGraphQLService;
import com.sdwu.types.event.BaseEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class FieldSearchCustomer {
    @Value("${spring.rabbitmq.topic.field}")
    private String topic;
    @Resource
    private ITalentRankGraphQLService talentRankGraphQLService;
    @RabbitListener(queuesToDeclare = @Queue(value = "${spring.rabbitmq.topic.field}"))
    private void listener(String message) throws Exception {
        log.info("主题：{} 监听到消息：{}", topic, message);
        BaseEvent.EventMessage<FieldSearchMessageEvent.FieldSearchMessage> eventMessage = JSON.parseObject(message, new TypeReference<BaseEvent.EventMessage<FieldSearchMessageEvent.FieldSearchMessage>>() {
        }.getType());
        FieldSearchMessageEvent.FieldSearchMessage fieldSearchMessage = eventMessage.getData();
        log.info("Field:{}", fieldSearchMessage.getField());
        talentRankGraphQLService.fetchUserByRepoTopic(fieldSearchMessage.getField());
    }
}
