package com.sdwu.domain.github.service;

import com.alibaba.fastjson2.JSONObject;
import com.sdwu.domain.github.model.entity.Developer;

import java.io.IOException;
import java.util.List;

public interface IGitHubApi {
    String getReposByUserName(String username) throws IOException;

    int getUserCommits(String owner, String repo, String username) throws IOException;

    int getUserPullRequests(String owner, String repo, String username) throws IOException;

    int getUserIssues(String owner, String repo, String username) throws IOException;

    String getUserContributions(String owner, String repo, String username) throws IOException;

    JSONObject getUserInfo(String username) throws IOException;

    List<String> getFollowersByUserName(String username) throws IOException;

    List<String> getFollowingByUserName(String username) throws IOException;

    String getDeveloperByFieldAndNation(String field, String nation) throws IOException;

    String getDevelopersByFields(String field) throws IOException;
}
