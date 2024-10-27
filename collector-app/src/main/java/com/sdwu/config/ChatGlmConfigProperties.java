package com.sdwu.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "openai.sdk.config.chatglm", ignoreInvalidFields = true)
public class ChatGlmConfigProperties {
    private String apiHost;
    private String apiKey;
    private String enabled;
}
