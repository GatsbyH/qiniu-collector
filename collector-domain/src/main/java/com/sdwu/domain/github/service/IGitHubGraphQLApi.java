package com.sdwu.domain.github.service;

import com.sdwu.domain.github.model.valobj.DevelopeVo;
import com.sdwu.domain.github.model.valobj.LanguageCountRespVo;
import com.sdwu.domain.github.model.valobj.RankResult;

import java.io.IOException;
import java.util.List;

public interface IGitHubGraphQLApi {
    String fetchUserByUsername(String username);

    DevelopeVo fetchUserStats(String username);


    RankResult getTalentRankByUserName(String username);

//    void fetchTopLanguages(String username);

    boolean fetchUserByRepoTopic(String topic);

    boolean fetchUserByRepoDescription(String description);

    List<LanguageCountRespVo> fetchTopLanguages(String username);

    String fetchDeveloperFiled(String username);


}
