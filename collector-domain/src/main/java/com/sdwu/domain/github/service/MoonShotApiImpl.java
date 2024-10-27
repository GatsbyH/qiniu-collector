package com.sdwu.domain.github.service;

import cn.bugstack.openai.executor.parameter.ChatCompletionNoStreamResponse;
import cn.bugstack.openai.executor.parameter.CompletionRequest;
import cn.bugstack.openai.executor.parameter.Message;
import cn.bugstack.openai.session.OpenAiSession;
import com.alibaba.fastjson2.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class MoonShotApiImpl implements IMoonShotApi{
    @Autowired(required = false)
    private OpenAiSession openAiSession;
    @Override
    public String getCountry(String location) {
        CompletionRequest request = CompletionRequest.builder()
                .messages(Collections.singletonList(Message.builder()
                        .role(CompletionRequest.Role.USER)
                        .content(location+"是哪个国家，只需回答国家")
                        .build()))
                .model(CompletionRequest.Model.MOON_SHOT_V1_32K.getCode())
                .build();

        ChatCompletionNoStreamResponse moonShotChatCompletionNoStreamResponse = openAiSession.completionsNotStream(request);
        ChatCompletionNoStreamResponse.Choice choice = moonShotChatCompletionNoStreamResponse.getChoices().get(0);
        ChatCompletionNoStreamResponse.Choice.Message message = choice.getMessage();
        return message.getContent();
    }

    @Override
    public String getCountryByUserRelations(List<String> followersLocations) {
        //把followersLocations序列化成String
        String followersLocation = JSON.toJSONString(followersLocations);
        CompletionRequest request = CompletionRequest.builder()
                .messages(Collections.singletonList(Message.builder()
                        .role(CompletionRequest.Role.USER)
                        .content(followersLocation+"根据这些用户关注和粉丝，猜测用户的国家，只需回答置信度最高的国家和它置信度，置信度为1-100%的数值，置信度低的数据为 N/A 值，不要回答多余的内容，只回答国家名和置信度\n")
                        .build()))
                .model(CompletionRequest.Model.MOON_SHOT_V1_32K.getCode())
                .build();

        ChatCompletionNoStreamResponse moonShotChatCompletionNoStreamResponse = openAiSession.completionsNotStream(request);
        ChatCompletionNoStreamResponse.Choice choice = moonShotChatCompletionNoStreamResponse.getChoices().get(0);
        ChatCompletionNoStreamResponse.Choice.Message message = choice.getMessage();
        return message.getContent();
    }
}
