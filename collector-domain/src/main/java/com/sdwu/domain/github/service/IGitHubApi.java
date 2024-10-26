package com.sdwu.domain.github.service;

import java.io.IOException;

public interface IGitHubApi {
    String getReposByUserName(String username) throws IOException;

    int getUserCommits(String owner, String repo, String username) throws IOException;

    int getUserPullRequests(String owner, String repo, String username) throws IOException;

    int getUserIssues(String owner, String repo, String username) throws IOException;

    String getUserContributions(String owner, String repo, String username) throws IOException;
}
