package com.sdwu.domain.github.repository;

import com.sdwu.domain.github.model.entity.Developer;

import java.util.List;

public interface IGithubUserRepository {
    void save(String field,List<Developer> developers);

    List<Developer> getDevelopersByFields(String field);

    Integer getGitHubPageByField(String field);

    void updateGitHubPageByField(String field);

    Boolean getFetchFlag(String field);

    void updateFetchFlag(String field);
}
