package com.sdwu.domain.github.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zhipu.oapi.service.v4.model.ModelApiResponse;

import java.util.List;

public interface IChatGlmApi {
    ModelApiResponse testCharGlmInvoke() throws JsonProcessingException;

    String getCountry(String location) throws JsonProcessingException;

    String getCountryByUserRelations(List<String> followersLocations) throws JsonProcessingException;

    ModelApiResponse testChatGlmWebSearch();
}
