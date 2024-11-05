package com.sdwu.domain.github.service;

import com.sdwu.domain.github.model.entity.Developer;
import com.sdwu.domain.github.model.valobj.DevelopeVo;
import com.sdwu.domain.github.model.valobj.RankResult;

import java.io.IOException;

public interface IGitHubGraphQLApi {
    String fetchUserByUsername(String username);

    DevelopeVo fetchUserStats(String username) throws IOException;


    RankResult getTalentRankByUserName(String username) throws IOException;

    void fetchTopLanguages(String username);

    boolean fetchUserByRepoTopic(String topic);
}
