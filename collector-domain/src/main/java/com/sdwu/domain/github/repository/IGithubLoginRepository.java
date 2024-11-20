package com.sdwu.domain.github.repository;

import com.sdwu.domain.github.model.entity.GithubUser;

public interface IGithubLoginRepository {
    GithubUser findByUuid(String uuid);
    Long save(GithubUser githubUser);
    GithubUser findById(Long id);
}
