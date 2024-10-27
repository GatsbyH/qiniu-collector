package com.sdwu.config;

import cn.bugstack.openai.executor.model.chatglm.config.ChatGLMConfig;
import cn.bugstack.openai.executor.model.moonshot.config.MoonShotConfig;
import cn.bugstack.openai.session.OpenAiSession;
import cn.bugstack.openai.session.OpenAiSessionFactory;
import cn.bugstack.openai.session.defaults.DefaultOpenAiSessionFactory;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({
        OpenAISDKConfigProperties.class,
        ChatGlmConfigProperties.class
})
public class OpenAISDKConfig {

    @Bean(name = "openAiSession")
    @ConditionalOnProperty(value = "openai.sdk.config.enabled", havingValue = "true", matchIfMissing = false)
    public OpenAiSession openAiSession(OpenAISDKConfigProperties properties,ChatGlmConfigProperties chatGlmConfigProperties) {
        // 1. 配置文件
        cn.bugstack.openai.session.Configuration configuration = new cn.bugstack.openai.session.Configuration();
        // 添加配置
        MoonShotConfig moonShotConfig = new MoonShotConfig();
        moonShotConfig.setApiHost(properties.getApiHost());
        moonShotConfig.setApiKey(properties.getApiKey());


        ChatGLMConfig chatGLMConfig = new ChatGLMConfig();
        chatGLMConfig.setApiHost(chatGlmConfigProperties.getApiHost());
        chatGLMConfig.setApiKey(chatGlmConfigProperties.getApiKey());


        configuration.setLevel(HttpLoggingInterceptor.Level.BODY);
        configuration.setMoonShotConfig(moonShotConfig);
        configuration.setChatGLMConfig(chatGLMConfig);
        // 2. 会话工厂
        OpenAiSessionFactory factory = new DefaultOpenAiSessionFactory(configuration);

        // 3. 开启会话
        return factory.openSession();
    }


}
