package com.sdwu.domain.github.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GithubUser {
    private Long id;               // 主键
    private String uuid;           // GitHub用户唯一标识
    private String username;       // GitHub用户名
    private String nickname;       // 昵称
    private String avatar;         // 头像URL
    private String email;          // 邮箱
    private String accessToken;    // 访问令牌
}
