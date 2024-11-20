package com.sdwu.domain.github.service;

import com.sdwu.domain.github.model.entity.GithubUser;
import me.zhyd.oauth.model.AuthUser;

public interface IGithubLoginService {
    /**
     * 处理GitHub登录
     * @param authUser GitHub授权用户信息
     * @return 用户ID
     */
    Long handleGithubLogin(AuthUser authUser);

    /**
     * 获取GitHub用户信息
     * @param userId 用户ID
     * @return GitHub用户信息
     */
    GithubUser getGithubUserInfo(Long userId);
} 