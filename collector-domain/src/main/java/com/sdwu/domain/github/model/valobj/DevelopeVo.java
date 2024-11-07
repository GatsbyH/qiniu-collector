package com.sdwu.domain.github.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DevelopeVo {
    private Integer totalCommits;
    private Integer totalPRs;
    private Integer totalIssues;
    private Integer totalReviews;
    private Integer totalStars;
    private Integer totalFollowers;
    private Integer contributeTo;
    private RankResult rankResult;
    private String assessment;
    private String field;
    private String nation;
    private String avatarUrl;
}
