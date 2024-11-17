package com.sdwu.domain.github.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GithubUserRespVo {

    private String login;     // 用户名

    private String avatarUrl;  // 头像 URL


    private String htmlUrl;
}
