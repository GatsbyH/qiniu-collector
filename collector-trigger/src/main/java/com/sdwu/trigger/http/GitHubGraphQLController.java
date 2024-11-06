package com.sdwu.trigger.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sdwu.domain.github.model.valobj.DevelopeVo;
import com.sdwu.domain.github.service.ITalentRankGraphQLService;
import com.sdwu.types.annotation.Loggable;
import com.sdwu.types.enums.ResponseCode;
import com.sdwu.types.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping("/GraphQL/")
public class GitHubGraphQLController {
    @Resource
    private ITalentRankGraphQLService talentRankGraphQLService;


    //测试GraphQL
    @GetMapping("testGraphQL")
    public Response testGraphQL() throws JsonProcessingException {
        String testGraphQL = talentRankGraphQLService.testGraphQL();
        return Response.builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .data(testGraphQL)
                .build();
    }

    //GraphQL获得talentRank
    @GetMapping("getTalentRankByUserName")
    @Loggable
    public Response getTalentRankByUserName(String username) throws IOException {
        DevelopeVo talentRank = talentRankGraphQLService.getDeveloperStatsByUserName(username);
        return Response.builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .data(talentRank)
                .build();
    }

    //根据账号评估开发者页面
    @GetMapping("getDeveloperAssessment")
    public Response getDeveloperAssessment(String username) throws IOException {
        DevelopeVo developerAssessment = talentRankGraphQLService.getDeveloperAssessment(username);
        return Response.builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .data(developerAssessment)
                .build();
    }
    //测试领域搜索
    @GetMapping("testSearch")
    public Response testSearch(String topic) throws IOException {
        talentRankGraphQLService.fetchUserByRepoTopic(topic);
        return Response.builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .data(null)
                .build();
    }
    //根据账号搜索用户使用的语言
    @GetMapping("getDeveloperLanguage")
    public Response getDeveloperLanguage(String username)  {
        Map<String, Integer> developerLanguage = talentRankGraphQLService.getDeveloperLanguage(username);
        return Response.builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .data(developerLanguage)
                .build();
    }
}
