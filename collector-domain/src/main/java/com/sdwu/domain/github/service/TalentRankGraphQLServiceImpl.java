package com.sdwu.domain.github.service;

import com.sdwu.domain.github.model.valobj.DevelopeVo;
import com.sdwu.domain.github.model.valobj.LanguageCountRespVo;
import com.sdwu.domain.github.model.valobj.RankResult;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class TalentRankGraphQLServiceImpl implements ITalentRankGraphQLService{
    @Resource
    private IGitHubGraphQLApi gitHubGraphQLApi;
    @Override
    public String testGraphQL() {
        String yunaiV = gitHubGraphQLApi.fetchUserByUsername("YunaiV");
        return yunaiV;
    }

    @Override
    public RankResult getTalentRankByUserName(String username){
        RankResult talentRankByUserName = gitHubGraphQLApi.getTalentRankByUserName(username);
        return talentRankByUserName;
    }

    @Override
    public DevelopeVo getDeveloperStatsByUserName(String username){
        DevelopeVo userStats = gitHubGraphQLApi.fetchUserStats(username);
        return userStats;
    }

    @Override
    public DevelopeVo getDeveloperAssessment(String username) throws IOException {
//        DevelopeVo userStats = gitHubGraphQLApi.fetchUserStats(username);
//        gitHubGraphQLApi.fetchTopLanguages(username);
        return null;
    }

    @Override
    public boolean fetchUserByRepoTopic(String topic) {
      return gitHubGraphQLApi.fetchUserByRepoTopic(topic);
    }

    @Override
    public boolean fetchUserByRepoDescription(String description) {
        return gitHubGraphQLApi.fetchUserByRepoDescription(description);
    }

    @Override
    public List<LanguageCountRespVo> getDeveloperLanguage(String username) {
        return gitHubGraphQLApi.fetchTopLanguages(username);
    }

    @Override
    public String getDeveloperFiled(String username) {
        return gitHubGraphQLApi.fetchDeveloperFiled(username);
    }

    @Override
    public boolean fetchUsersByStrategy(String field,String queryTemplate, String queryParamName, String formattedTerm) {
        return gitHubGraphQLApi.fetchUsersByStrategy(field,queryTemplate,queryParamName,formattedTerm);
    }


}
