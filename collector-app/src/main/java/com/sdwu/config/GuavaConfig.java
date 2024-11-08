package com.sdwu.config;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.eventbus.EventBus;
//import com.sdwu.trigger.listener.stopFetchingListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class GuavaConfig {

    @Bean(name = "cache")
    public Cache<String, String> cache() {
        return CacheBuilder.newBuilder()
                .expireAfterWrite(3, TimeUnit.SECONDS)
                .build();
    }

//    @Bean()
//    public EventBus eventBusListener(stopFetchingListener listener){
//        EventBus eventBus = new EventBus();
//        eventBus.register(listener);
//        return eventBus;
//    }


}
