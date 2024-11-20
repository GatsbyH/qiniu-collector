package com.sdwu.infrastructure.persistent.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.sdwu.domain.github.model.entity.GithubUser;
import com.sdwu.infrastructure.persistent.utils.BaseDO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("github_user")
public class GithubUserPO extends BaseDO {
    private Long id;
    private String uuid;
    private String username;
    private String nickname;
    private String avatar;
    private String email;
    private String accessToken;

    public static GithubUser convertToDomain(GithubUserPO po) {
        if (po == null) {
            return null;
        }
        return GithubUser.builder()
                .id(po.getId())
                .uuid(po.getUuid())
                .username(po.getUsername())
                .nickname(po.getNickname())
                .avatar(po.getAvatar())
                .email(po.getEmail())
                .accessToken(po.getAccessToken())
                .build();
    }

    public static GithubUserPO convertFromDomain(GithubUser domain) {
        if (domain == null) {
            return null;
        }
        GithubUserPO po = GithubUserPO.builder()
                .id(domain.getId())
                .uuid(domain.getUuid())
                .username(domain.getUsername())
                .nickname(domain.getNickname())
                .avatar(domain.getAvatar())
                .email(domain.getEmail())
                .accessToken(domain.getAccessToken())
                .build();
        return po;
    }
}
