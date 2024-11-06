package com.sdwu.domain.github.service;

import cn.hutool.core.collection.CollectionUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
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
import com.zhipu.oapi.service.v4.tools.*;
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
    private SensitiveWordBs sensitiveWordBs;
//    private static final String API_SECRET_KEY = System.getProperty("ZHIPUAI_API_KEY");

    private static final ClientV4 client = new ClientV4.Builder("f65e53e96d908e7f9c26fb63b52b7ec7.dSI17rpHEY87vubp")
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
        if (replace.contains("Hong Kong")){
            return "中国";
        }
        if (replace.contains("香港")){
            return "中国";
        }
        ChatMessage chatMessage = new ChatMessage(ChatMessageRole.USER.value(), replace+"#\n" +
                "\n" +
                "## 上下文（Context）\n" +
                "用户询问一个地理位置（replace+\"是哪个国家\"），并特别指出台湾是中国的一部分。\n" +
                "\n" +
                "## 目标（Objective）\n" +
                "回答用户的问题，明确指出所询问的地理位置所属的国家。\n" +
                "\n" +
                "## 风格（Style）\n" +
                "使用简洁明了的陈述句。\n" +
                "\n" +
                "## 语气（Tone）\n" +
                "客观、中立。\n" +
                "\n" +
                "## 受众（Audience）\n" +
                "对地理位置有一定了解的用户。\n" +
                "\n" +
                "## 响应（Response）\n" +
                "以文本形式返回国家，格式为“国家+置信度”。\n" +
                "\n" +
                "## 工作流程（Workflow）\n" +
                "1. 确定用户询问的地理位置。\n" +
                "2. 查找该地理位置所属的国家。\n" +
                "3. 直接回答所属国家。\n" +
                "\n" +
                "## 示例（Examples）\n" +
                "- 用户输入：beijing是哪个国家？\"\n" +
                "- AI回答：\"中国\"\n" +
                "\n" +
                "## 修改示例"+
                "- 用户输入：Ireland是哪个国家？"+
                "- AI回答：爱尔兰"+
                "- 用户输入：BITS Pilani是哪个国家？"+
                "- AI回答：印度"+
                "## 注意事项\n" +
                "- 保持回答的简洁性，不涉及任何政治立场。\n" +
                "- 回复国家的中文。\n" +
                "只需回答置信度最高的国家和它置信度，置信度为1-100%的数值，置信度低的数据为 N/A 值"+
                "- 若用户询问的地理位置存在争议，按照国际普遍认知回答。");
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
        ChatMessage chatMessage = new ChatMessage(ChatMessageRole.USER.value(), replace+"根据用户关注和粉丝的国家，猜测用户的国家，只需回答置信度最高的国家和它置信度，置信度为1-100%的数值，置信度低的数据为 N/A 值，最后只返回这两个值，不需要说明,如果发生异常情况就返回N/A，如果没有提供具体的用户关注和粉丝的国家数据就返回N/A，回复国家的中文\n");

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

    @Override
    public String testChatGlmWebSearch(String blog,String bio) throws JsonProcessingException {
        String webSearchResult = null;
        try {
            webSearchResult = this.webSearch(blog);
        } catch (Exception e) {
            log.error("网络搜索结果:{}",e.getMessage());
        }
        //开发者技术能力评估信息自动整理
        String assessment = this.developmentAssessment(webSearchResult,bio);
        return assessment;
    }

    @Override
    public String doDevelopmentAssessment(String blog,String bio) throws JsonProcessingException {
        log.info("blog:{},bio:{}",blog,bio);
        String webSearchResult = null;
        try {
            webSearchResult = this.webSearch(blog);
        } catch (Exception e) {
            log.error("网络搜索结果:{}",e.getMessage());
        }
        //开发者技术能力评估信息自动整理
        String assessment = null;
        try {
            assessment = this.developmentAssessment(webSearchResult,bio);
        } catch (JsonProcessingException e) {
            log.error("开发者技术能力评估信息自动整理:{}",e.getMessage());
        }
        return assessment;
    }



    private String developmentAssessment(String webSearchResult,String bio) throws JsonProcessingException {
        List<ChatMessage> messages = new ArrayList<>();

        ChatMessage chatMessage = new ChatMessage(ChatMessageRole.USER.value(), "根据开发者的作品："+webSearchResult+"和他的个人简介"+bio+"整理出开发者评估信息，只输出对开发者的评价");
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

    private String webSearch(String blog) throws JsonProcessingException {
        String jsonString = "[\n" +
                "    {\n" +
                "        \"content\": \"读取网页并总结：" + blog + "\",\n" +
                "        \"role\": \"user\"\n" +
                "    }\n" +
                "]";
        ArrayList<SearchChatMessage> messages = new ObjectMapper().readValue(jsonString, new TypeReference<ArrayList<SearchChatMessage>>() {
        });


        String requestId = String.format(requestIdTemplate, System.currentTimeMillis());
        WebSearchParamsRequest chatCompletionRequest = WebSearchParamsRequest.builder()
                .model("web-search-pro")
                .stream(Boolean.FALSE)
                .messages(messages)
                .requestId(requestId)
                .build();
        WebSearchApiResponse webSearchApiResponse = null;
        try {
            webSearchApiResponse = client.invokeWebSearchPro(chatCompletionRequest);
        } catch (Exception e) {
            log.error("网络搜索blog错误: {}", e.getMessage());
            return null;
        }
        WebSearchMessageToolCall jsonNodes = webSearchApiResponse.getData().getChoices().get(0).getMessage().getToolCalls().get(1);
        log.info("jsonNodes: {}", jsonNodes);
        log.info("model output: {}", mapper.writeValueAsString(webSearchApiResponse));
        return jsonNodes.toString();
    }

    @Override
    public String fieldOptimization(String field) throws JsonProcessingException {
        List<ChatMessage> messages = new ArrayList<>();

        ChatMessage chatMessage = new ChatMessage(ChatMessageRole.USER.value(), "根据"+field+"，生成一些关键词，用与github api搜索仓库，只输出3个关键词之间用"+"隔开");
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
    public String guessTheFieldBasedOnTheTopic(String topics) throws JsonProcessingException {
        List<ChatMessage> messages = new ArrayList<>();

        ChatMessage chatMessage = new ChatMessage(ChatMessageRole.USER.value(), "根据"+topics+"，猜测一个领域，只输出一个领域，比如：机器学习，深度学习，计算机视觉或者goplus等");
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
    public String guessTheFieldBasedOnTheDescriptions(List<String> descriptions) throws JsonProcessingException {
        List<ChatMessage> messages = new ArrayList<>();
        //把描述列表拼接成一个字符串
        String descrip = String.join(",", descriptions);
        ChatMessage chatMessage = new ChatMessage(ChatMessageRole.USER.value(), "根据"+descrip+"，猜测一个领域，只输出一个领域，比如：机器学习，深度学习，计算机视觉等，只输出一个词语");
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


}
