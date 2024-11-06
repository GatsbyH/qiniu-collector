package com.sdwu.domain.github.service;

import com.sdwu.domain.github.model.valobj.DevelopeVo;
import com.sdwu.domain.github.model.valobj.LanguageCountRespVo;
import com.sdwu.domain.github.model.valobj.RankResult;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ITalentRankGraphQLService {
    String testGraphQL();

    RankResult getTalentRankByUserName(String username) throws IOException;
    DevelopeVo getDeveloperStatsByUserName(String username) throws IOException;

    DevelopeVo getDeveloperAssessment(String username) throws IOException;


    boolean fetchUserByRepoTopic(String topic);

    boolean fetchUserByRepoDescription(String field);

    List<LanguageCountRespVo> getDeveloperLanguage(String username);

    String getDeveloperFiled(String username);


}
