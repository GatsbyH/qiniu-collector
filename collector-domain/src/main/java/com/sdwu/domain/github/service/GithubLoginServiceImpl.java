package com.sdwu.domain.github.service;

import com.sdwu.domain.github.model.entity.GithubUser;
import com.sdwu.domain.github.repository.IGithubLoginRepository;

import me.zhyd.oauth.model.AuthUser;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@Service
public class GithubLoginServiceImpl implements IGithubLoginService {

    @Resource
    private IGithubLoginRepository githubLoginRepository;

    @Override
    public Long handleGithubLogin(AuthUser authUser) {
        // 查找或创建GitHub用户
        GithubUser githubUser = githubLoginRepository.findByUuid(authUser.getUuid());

        if (githubUser == null) {
            githubUser = new GithubUser();
            githubUser.setUuid(authUser.getUuid());
        }

        // 更新用户信息
        githubUser.setUsername(authUser.getUsername());
        githubUser.setNickname(authUser.getNickname());
        githubUser.setAvatar(authUser.getAvatar());
        githubUser.setEmail(authUser.getEmail());
        githubUser.setAccessToken(authUser.getToken().getAccessToken());

        // 保存并返回用户ID
        return githubLoginRepository.save(githubUser);
    }

    @Override
    public GithubUser getGithubUserInfo(Long userId) {
        GithubUser githubUser = githubLoginRepository.findById(userId);
        if (githubUser == null) {
            throw new RuntimeException("用户不存在");
        }
        return githubUser;
    }
}
