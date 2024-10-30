package com.sdwu.config;

import com.github.houbb.sensitive.word.api.IWordAllow;
import com.github.houbb.sensitive.word.api.IWordDeny;
import com.github.houbb.sensitive.word.bs.SensitiveWordBs;
import com.sdwu.infrastructure.persistent.dao.ISensitiveWordDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;


@Configuration
public class SensitiveWordConfig {

    @Bean
    public SensitiveWordBs sensitiveWordBs(IWordDeny wordDeny, IWordAllow wordAllow) {
        return SensitiveWordBs.newInstance()
                .wordDeny(wordDeny)
                .wordAllow(wordAllow)
                .ignoreCase(true)
                .ignoreWidth(true)
                .ignoreNumStyle(true)
                .ignoreChineseStyle(true)
                .ignoreEnglishStyle(true)
                .ignoreRepeat(false)
                .enableNumCheck(true)
                .enableEmailCheck(true)
                .enableUrlCheck(true)
                .enableWordCheck(true)
                .numCheckLen(1024)
                .init();
    }

    @Bean
    public IWordDeny wordDeny(ISensitiveWordDao sensitiveWordDao) {
        return new IWordDeny() {
            @Override
            public List<String> deny() {
                return sensitiveWordDao.queryValidSensitiveWordConfig("deny");
            }
        };
    }

    @Bean
    public IWordAllow wordAllow(ISensitiveWordDao sensitiveWordDao) {
        return new IWordAllow() {
            @Override
            public List<String> allow() {
                return sensitiveWordDao.queryValidSensitiveWordConfig("allow");
            }
        };
    }

}
