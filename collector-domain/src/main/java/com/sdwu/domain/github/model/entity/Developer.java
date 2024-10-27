package com.sdwu.domain.github.model.entity;

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
}
