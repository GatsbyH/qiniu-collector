package com.sdwu.domain.github.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zhipu.oapi.service.v4.model.ModelApiResponse;

import java.util.List;

public interface IChatGlmApi {
    ModelApiResponse testCharGlmInvoke() throws JsonProcessingException;

    String getCountry(String location) throws JsonProcessingException;

    String getCountryByUserRelations(List<String> followersLocations) throws JsonProcessingException;

    String testChatGlmWebSearch(String blog,String bio) throws JsonProcessingException;

    String doDevelopmentAssessment(String blog,String bio) throws JsonProcessingException;

    String fieldOptimization(String field) throws JsonProcessingException;

    String guessTheFieldBasedOnTheTopic(String string) throws JsonProcessingException;

    String guessTheFieldBasedOnTheDescriptions(List<String> descriptions) throws JsonProcessingException;
}
