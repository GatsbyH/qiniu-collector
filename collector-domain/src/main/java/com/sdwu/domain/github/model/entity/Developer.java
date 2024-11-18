package com.sdwu.domain.github.model.entity;

import com.alibaba.fastjson2.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Developer {
    private String login;     // 用户名
    private String location;  // 位置
    private String field;     // 领域
    private String nation;    // 国籍
    private String htmlUrl;   // GitHub 地址
    private double talentRank; // 人才指数
    private String name;               // 真实姓名
    private String company;            // 公司
    private String blog;               // 博客链接
    private String email;              // 电子邮件
    private Boolean hireable;          // 是否可被雇佣
    private String bio;                // 个人简介
    private String twitterUsername;     // Twitter 用户名
    private int publicRepos;           // 公共仓库数量
    private int publicGists;           // 公共 Gist 数量
    private int followers;             // 粉丝数量
    private int following;             // 关注的用户数量
    private String avatarUrl;           // 头像 URL
    private String type;                // 用户类型
    private String repositoryUrl;             // 项目URL
    private String assessment;
    private String level;

    public static Developer fromGitHubData(
            String login,
            JSONObject userInfo,
            String field,
            String developerNation,
            double talentRank,
            String level,
            String assessment,
            String repositoryUrl) {

        return Developer.builder()
                .login(login)
                .bio(userInfo.getString("bio"))
                .company(userInfo.getString("company"))
                .field(field)
                .location(userInfo.getString("location"))
                .htmlUrl(userInfo.getString("html_url"))
                .name(userInfo.getString("name"))
                .blog(userInfo.getString("blog"))
                .email(userInfo.getString("email"))
                .hireable(userInfo.getBoolean("hireable"))
                .talentRank(talentRank)
                .level(level)
                .nation(developerNation)
                .assessment(assessment)
                .twitterUsername(userInfo.getString("twitter_username"))
                .publicRepos(userInfo.getIntValue("public_repos"))
                .publicGists(userInfo.getIntValue("public_gists"))
                .followers(userInfo.getIntValue("followers"))
                .following(userInfo.getIntValue("following"))
                .type(userInfo.getString("type"))
                .repositoryUrl(repositoryUrl)
                .avatarUrl(userInfo.getString("avatar_url"))
                .build();
    }
}
