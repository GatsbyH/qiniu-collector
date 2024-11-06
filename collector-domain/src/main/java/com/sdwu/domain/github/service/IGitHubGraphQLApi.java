package com.sdwu.domain.github.service;

import com.sdwu.domain.github.model.valobj.DevelopeVo;
import com.sdwu.domain.github.model.valobj.RankResult;

import java.io.IOException;
import java.util.Map;

public interface IGitHubGraphQLApi {
    String fetchUserByUsername(String username);

    DevelopeVo fetchUserStats(String username) throws IOException;


    RankResult getTalentRankByUserName(String username) throws IOException;

//    void fetchTopLanguages(String username);

    boolean fetchUserByRepoTopic(String topic);

    boolean fetchUserByRepoDescription(String description);

    Map<String, Integer> fetchTopLanguages(String username);
}
