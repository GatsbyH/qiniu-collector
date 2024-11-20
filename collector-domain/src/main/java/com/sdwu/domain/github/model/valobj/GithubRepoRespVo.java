package com.sdwu.domain.github.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GithubRepoRespVo {
    // 基本信息
    private String name;
    private String description;
    private String htmlUrl;
    private String homepage;

    // 统计信息
    private Integer stars;
    private Integer forks;
    private Integer watchers;
    private Integer openIssues;

    // 技术相关
    private String language;
    private String defaultBranch;
    private List<String> topics;

    // 状态标记
    private Boolean isPrivate;
    private Boolean isFork;
    private Boolean isArchived;

    // 时间信息
    private Date updatedAt;
    private Date createdAt;

    // 额外信息
    private String size;  // 格式化后的大小
}
