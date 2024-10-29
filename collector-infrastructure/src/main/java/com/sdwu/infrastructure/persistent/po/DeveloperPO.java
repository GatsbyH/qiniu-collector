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

    public static DeveloperPO toPO(Developer developer) {
        return DeveloperPO.builder()
                .login(developer.getLogin())
                .location(developer.getLocation())
                .field(developer.getField())
                .nation(developer.getNation())
                .htmlUrl(developer.getHtmlUrl())
                .talentRank(developer.getTalentRank())
                .build();
    }

    public static Developer toDeveloper(DeveloperPO po) {
        return Developer.builder()
                .login(po.getLogin())
                .location(po.getLocation())
                .field(po.getField())
                .nation(po.getNation())
                .htmlUrl(po.getHtmlUrl())
                .talentRank(po.getTalentRank())
                .build();
    }
}
