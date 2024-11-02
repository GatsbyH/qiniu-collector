package com.sdwu.domain.github.service;

import com.sdwu.domain.github.model.valobj.DevelopeVo;

import java.io.IOException;

public interface IGitHubGraphQLApi {
    String fetchUserByUsername(String username);

    DevelopeVo fetchUserStats(String username) throws IOException;
}
