package com.sdwu.domain.github.service;

import cn.hutool.core.collection.CollectionUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.houbb.sensitive.word.bs.SensitiveWordBs;
import com.sdwu.domain.censorship.factory.DefaultLogicFactory;
import com.sdwu.domain.censorship.model.entity.RuleActionEntity;
import com.sdwu.domain.censorship.model.entity.RuleMatterEntity;
import com.sdwu.types.enums.ResponseCode;
import com.sdwu.types.exception.AppException;
import com.zhipu.oapi.ClientV4;
import com.zhipu.oapi.Constants;
import com.zhipu.oapi.service.v4.deserialize.MessageDeserializeFactory;
import com.zhipu.oapi.service.v4.model.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class ChatGlmApiImpl implements IChatGlmApi{
    @Resource
    private DefaultLogicFactory defaultLogicFactory;
    @Resource
    private SensitiveWordBs sensitiveWordBs;
    private static final String API_SECRET_KEY = System.getProperty("ZHIPUAI_API_KEY");

    private static final ClientV4 client = new ClientV4.Builder("2589ec5698aa22ce677a261bb7bf5221.nCX8m9P7dfLKUBdp")
            .networkConfig(300, 100, 100, 100, TimeUnit.SECONDS)
            .connectionPool(new okhttp3.ConnectionPool(8, 1, TimeUnit.SECONDS))
            .build();
    private static final ObjectMapper mapper = MessageDeserializeFactory.defaultObjectMapper();

    private static final String requestIdTemplate = "mycompany-%d";
    public ModelApiResponse testCharGlmInvoke() throws JsonProcessingException {
        List<ChatMessage> messages = new ArrayList<>();
        ChatMessage chatMessage = new ChatMessage(ChatMessageRole.USER.value(), "ChatGLM和你哪个更强大");
        messages.add(chatMessage);
        String requestId = String.format(requestIdTemplate, System.currentTimeMillis());

        HashMap<String, Object> extraJson = new HashMap<>();
        extraJson.put("temperature", 0.5);

        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .model(Constants.ModelCharGLM3)
                .stream(Boolean.FALSE)
                .invokeMethod(Constants.invokeMethod)
                .messages(messages)
                .requestId(requestId)
                .extraJson(extraJson)
                .build();
        log.info("request: {}", mapper.writeValueAsString(chatCompletionRequest));
        ModelApiResponse invokeModelApiResp = client.invokeModelApi(chatCompletionRequest);
        log.info("model output: {}", mapper.writeValueAsString(invokeModelApiResp));
        return invokeModelApiResp;
    }

    @Override
    public String getCountry(String location) throws JsonProcessingException {
        List<ChatMessage> messages = new ArrayList<>();

        String replace = sensitiveWordBs.replace(location);

        ChatMessage chatMessage = new ChatMessage(ChatMessageRole.USER.value(), replace+"是哪个国家（台湾是中国的），只需回答国家");
        messages.add(chatMessage);
        String requestId = String.format(requestIdTemplate, System.currentTimeMillis());

        HashMap<String, Object> extraJson = new HashMap<>();
        extraJson.put("temperature", 0.5);

        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .model("GLM-4-AirX")
                .stream(Boolean.FALSE)
                .invokeMethod(Constants.invokeMethod)
                .messages(messages)
                .requestId(requestId)
                .extraJson(extraJson)
                .build();
        ModelApiResponse invokeModelApiResp = null;
        log.info("request: {}", mapper.writeValueAsString(chatCompletionRequest));

        invokeModelApiResp = client.invokeModelApi(chatCompletionRequest);

        String message = invokeModelApiResp.getData().getChoices().get(0).getMessage().getContent().toString();
        log.info("model output: {}", mapper.writeValueAsString(invokeModelApiResp));
        return message;
    }

    @Override
    public String getCountryByUserRelations(List<String> followersLocations) throws JsonProcessingException {
        List<ChatMessage> messages = new ArrayList<>();

        //把followersLocations集合转换成字符串
        StringBuilder stringBuilder = new StringBuilder();
        if (CollectionUtil.isNotEmpty(followersLocations)) {
            for (String location : followersLocations) {
                stringBuilder.append(location).append(",");
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }
        String replace = sensitiveWordBs.replace(stringBuilder.toString());
        ChatMessage chatMessage = new ChatMessage(ChatMessageRole.USER.value(), replace+"根据用户关注和粉丝的国家，猜测用户的国家，只需回答置信度最高的国家和它置信度，置信度为1-100%的数值，置信度低的数据为 N/A 值，最后只返回这两个值，不需要说明\n");
        messages.add(chatMessage);
        String requestId = String.format(requestIdTemplate, System.currentTimeMillis());

        HashMap<String, Object> extraJson = new HashMap<>();
        extraJson.put("temperature", 0.5);

        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .model("GLM-4-AirX")
                .stream(Boolean.FALSE)
                .invokeMethod(Constants.invokeMethod)
                .messages(messages)
                .requestId(requestId)
                .extraJson(extraJson)
                .build();
        ModelApiResponse invokeModelApiResp = client.invokeModelApi(chatCompletionRequest);
        String message = invokeModelApiResp.getData().getChoices().get(0).getMessage().getContent().toString();
        log.info("model output: {}", mapper.writeValueAsString(invokeModelApiResp));
        return message;
    }


}
