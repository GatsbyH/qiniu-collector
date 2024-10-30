package com.sdwu.domain.github.repository;

import com.sdwu.domain.github.model.entity.Developer;
import com.sdwu.domain.github.model.valobj.DevelopersByFieldReqVo;
import com.sdwu.types.model.PageResult;

import java.util.List;

public interface IGithubUserRepository {
    void save(String field,List<Developer> developers);

    List<Developer> getDevelopersByFields(String field);

    Integer getGitHubPageByField(String field);

    void updateGitHubPageByField(String field);

    Boolean getFetchFlag(String field);

    void updateFetchFlag(String field);

    PageResult<Developer> getDevelopersByFieldsPage(DevelopersByFieldReqVo developersByFieldReqVo);
}
