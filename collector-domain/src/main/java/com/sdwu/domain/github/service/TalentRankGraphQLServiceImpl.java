package com.sdwu.domain.github.service;

import com.sdwu.domain.github.model.valobj.DevelopeVo;
import com.sdwu.domain.github.model.valobj.RankResult;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
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
    public RankResult getTalentRankByUserName(String username) throws IOException {
        RankResult talentRankByUserName = gitHubGraphQLApi.getTalentRankByUserName(username);
        return talentRankByUserName;
    }

    @Override
    public DevelopeVo getDeveloperStatsByUserName(String username) throws IOException {
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
    public Map<String, Integer> getDeveloperLanguage(String username) {
        return gitHubGraphQLApi.fetchTopLanguages(username);
    }
}
