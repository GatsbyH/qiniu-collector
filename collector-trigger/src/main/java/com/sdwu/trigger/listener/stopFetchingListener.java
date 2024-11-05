package com.sdwu.trigger.listener;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.sdwu.domain.github.service.IDeveloperFieldService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

//@Slf4j
//@Component
//public class stopFetchingListener {
//    @Resource
//    private IDeveloperFieldService developerFieldService;
//
//
//    @Subscribe
//    public void handleEvent(String topic) throws Exception {
//        log.info("preview task listener receive message: {}", topic);
//        developerFieldService.stopGetDeveloperByField(topic);
//    }
//}
