package com.sdwu.trigger.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sdwu.domain.github.model.valobj.DevelopeVo;
import com.sdwu.domain.github.model.valobj.LanguageCountRespVo;
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
import java.util.List;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping("/GraphQL/")
public class GitHubGraphQLController {
    @Resource
    private ITalentRankGraphQLService talentRankGraphQLService;


    //GraphQL获得talentRank
    @GetMapping("getTalentRankByUserName")
    @Loggable
    public Response getTalentRankByUserName(String username){
        DevelopeVo talentRank = talentRankGraphQLService.getDeveloperStatsByUserName(username);
        return Response.builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .data(talentRank)
                .build();
    }


    //根据账号搜索用户使用的语言
    @GetMapping("getDeveloperLanguage")
    @Loggable
    public Response getDeveloperLanguage(String username)  {
        List<LanguageCountRespVo> developerLanguage = talentRankGraphQLService.getDeveloperLanguage(username);
        return Response.builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .data(developerLanguage)
                .build();
    }


    //根据账号搜索用户的领域
    @GetMapping("getDeveloperFiled")
    @Loggable
    public Response getDeveloperFiled(String username)  {
        String developerFiled = talentRankGraphQLService.getDeveloperFiled(username);
        return Response.builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .data(developerFiled)
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
    //测试GraphQL
    @GetMapping("testGraphQL")
    public Response testGraphQL() {
        String testGraphQL = talentRankGraphQLService.testGraphQL();
        return Response.builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .data(testGraphQL)
                .build();
    }
}
