package com.sdwu.infrastructure.persistent.po;

import com.sdwu.domain.github.model.entity.Developer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeveloperPO {
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

    public static DeveloperPO toPO(Developer developer) {
        return DeveloperPO.builder()
                .login(developer.getLogin())
                .location(developer.getLocation())
                .field(developer.getField())
                .nation(developer.getNation())
                .htmlUrl(developer.getHtmlUrl())
                .talentRank(developer.getTalentRank())
                .name(developer.getName())
                .company(developer.getCompany())
                .blog(developer.getBlog())
                .email(developer.getEmail())
                .hireable(developer.getHireable())
                .bio(developer.getBio())
                .twitterUsername(developer.getTwitterUsername())
                .publicRepos(developer.getPublicRepos())
                .publicGists(developer.getPublicGists())
                .followers(developer.getFollowers())
                .following(developer.getFollowing())
                .avatarUrl(developer.getAvatarUrl())
                .type(developer.getType())
                .repositoryUrl(developer.getRepositoryUrl())
                .assessment(developer.getAssessment())
                .build();
    }

    public static Developer toDeveloper(DeveloperPO po) {
        return Developer.builder()
                .avatarUrl(po.getAvatarUrl())
                .login(po.getLogin())
                .location(po.getLocation())
                .field(po.getField())
                .nation(po.getNation())
                .htmlUrl(po.getHtmlUrl())
                .talentRank(po.getTalentRank())
                .name(po.getName())
                .company(po.getCompany())
                .blog(po.getBlog())
                .email(po.getEmail())
                .hireable(po.getHireable())
                .bio(po.getBio())
                .twitterUsername(po.getTwitterUsername())
                .publicRepos(po.getPublicRepos())
                .publicGists(po.getPublicGists())
                .followers(po.getFollowers())
                .following(po.getFollowing())
                .avatarUrl(po.getAvatarUrl())
                .type(po.getType())
                .repositoryUrl(po.getRepositoryUrl())
                .assessment(po.getAssessment())
                .build();
    }
}
