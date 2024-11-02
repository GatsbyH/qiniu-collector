package com.sdwu.domain.github.service;

import com.sdwu.domain.github.model.valobj.DevelopeVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;

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
    public DevelopeVo getTalentRankByUserName(String username) throws IOException {
        DevelopeVo userStats = gitHubGraphQLApi.fetchUserStats(username);
        return userStats;
    }
}
