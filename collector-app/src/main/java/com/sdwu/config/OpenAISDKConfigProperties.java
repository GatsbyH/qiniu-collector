package com.sdwu.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "openai.sdk.config.moonshot", ignoreInvalidFields = true)
public class OpenAISDKConfigProperties {

//    private com.sdwu.config.MoonShotConfig moonShotConfig;

    private String apiHost;
    private String apiKey;
    private String enabled;
}
