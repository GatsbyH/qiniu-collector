package com.sdwu.trigger.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sdwu.domain.github.model.valobj.DeveloperContributionVo;
import com.sdwu.domain.github.model.valobj.DevelopersByFieldReqVo;
import com.sdwu.domain.github.model.valobj.GithubRepoReqVo;
import com.sdwu.domain.github.model.valobj.GithubUserReqVo;
import com.sdwu.domain.github.service.*;
import com.sdwu.types.annotation.Loggable;
import com.sdwu.types.enums.ResponseCode;
import com.sdwu.types.model.Response;
import com.zhipu.oapi.service.v4.model.ModelApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

@RestController
@Slf4j
public class GitHubController {
//    private final GitHubClientService gitHubClientService;

    @Resource
    private ITalentRankService talentRankService;

    @Resource
    private IDeveloperNationService developerNationService;

    @Resource
    private IDeveloperFieldService developerFieldService;


    @Resource
    private IChatGlmApi chatGlmApi;


    @Resource
    private IGitHubApi gitHubApi;

//    @Autowired
//    public GitHubController(GitHubClientService gitHubClientService) {
//        this.gitHubClientService = gitHubClientService;
//    }



    //开启定时任务，根据领域搜索匹配开发者
    @GetMapping("startGetDeveloperByField")
    public Response startGetDeveloperByField(String field){
        return Response.builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .data(developerFieldService.startGetDeveloperByField(field))
                .build();
    }

    //模糊搜索Github用户
    @GetMapping("getGithubDevelopers")
    @Loggable
    public Response getGithubDevelopers(GithubUserReqVo githubUserReqVo){
        return Response.builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .data(gitHubApi.getGithubDevelopers(githubUserReqVo))
                .build();
    }

    //获取开发者仓库
    @GetMapping("getGithubUserRepos")
    @Loggable
    public Response getGithubUserRepos(String username) {
        return Response.builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .data(gitHubApi.getGithubUserRepos(username))
                .build();
    }

    //关闭定时任务，根据领域搜索匹配开发者
    @GetMapping("stopGetDeveloperByField")
    public Response stopGetDeveloperByField(String field){
        return Response.builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .data(developerFieldService.stopGetDeveloperByField(field))
                .build();
    }
    //分页查询开发者
    @GetMapping("getDevelopersByFields")
    public Response getDevelopersByFieldsPage(DevelopersByFieldReqVo developersByFieldReqVo){
        return Response.builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .data(developerFieldService.getDevelopersByFieldsPage(developersByFieldReqVo))
                .build();
    }

    //获取国家筛选项
    @GetMapping("getDeveloperNationOptionsByField")
    public Response getDeveloperNationOptionsByField(String field){
        return Response.builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .data(developerFieldService.getDeveloperNationOptionsByField(field))
                .build();
    }



    //大模型技术能力评估
    @GetMapping("getDeveloperTechnicalAbility")
    public Response getDeveloperTechnicalAbility(String username){
        String developerTechnicalAbility = talentRankService.getDeveloperTechnicalAbility(username);
        return Response.builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .data(developerTechnicalAbility)
                .build();
    }







    //根据账号评估开发者页面
    @GetMapping("getDeveloperAssessment")
    public Response getDeveloperAssessment(String username){
        DeveloperContributionVo developerAssessment= null;
        try {
            developerAssessment = talentRankService.getDeveloperAssessment(username);
        } catch (IOException e) {
            log.error("获取开发者{}的评估信息时发生错误: {}", username, e.getMessage());
            return Response.fail(null);
        }
        return Response.builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .data(developerAssessment)
                .build();
    }





//    @GetMapping("/user")
//    public String getUser() throws IOException {
//        return gitHubClientService.fetchGitHubApi("/users/gatsbyh", null);
//    }

    @GetMapping("getTalentRankByUserName")
    @Loggable
    public Response getTalentRankByUserName(String username) throws IOException, ExecutionException, InterruptedException {
        double talentRankByUserName = talentRankService.getTalentRankByUserName(username);
        return Response.builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .data(talentRankByUserName)
                .build();
    }
    //开发者的 Nation
    @GetMapping("getDeveloperNation")
    @Loggable
    public Response getDeveloperNation(String username) throws IOException, ExecutionException, InterruptedException {
        return Response.builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .data(developerNationService.getDeveloperNation(username))
                .build();
    }


    //开发者的领域。可根据领域搜索匹配，并按 TalentRank 排序。Nation 作为可选的筛选项，比如只需要显示所有位于中国的开发者。
    @GetMapping("getDeveloperByFieldAndNation")
    @Loggable
    public Response getDeveloperByFieldAndNation(String field,String nation) throws IOException {
        return Response.builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .data(developerFieldService.getDeveloperByFieldAndNation(field,nation))
                .build();
    }

    //获得领域
    @GetMapping("getDeveloperFields")
    public Response getDeveloperFields(){
        return Response.builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .data(developerFieldService.getDeveloperFields())
                .build();
    }
    //测试ChatGlm
    @GetMapping("testChatGlm")
    public Response testChatGlm() throws JsonProcessingException {
        ModelApiResponse modelApiResponse = chatGlmApi.testCharGlmInvoke();
        return Response.builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .data(modelApiResponse)
                .build();
    }

    //测试ChatGlm联网搜索
    @GetMapping("testChatGlmWebSearch")
    public Response testChatGlmWebSearch(String blog,String bio) throws JsonProcessingException {
        String assessment = chatGlmApi.testChatGlmWebSearch(blog,bio);
        return Response.builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .data(assessment)
                .build();
    }



}


