package com.sdwu.domain.github.service;

import com.sdwu.domain.github.model.valobj.DevelopeVo;

import java.io.IOException;

public interface ITalentRankGraphQLService {
    String testGraphQL();

    DevelopeVo getTalentRankByUserName(String username) throws IOException;
}
