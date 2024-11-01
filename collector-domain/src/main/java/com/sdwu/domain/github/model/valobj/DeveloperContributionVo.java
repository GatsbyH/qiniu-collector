package com.sdwu.domain.github.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public  class DeveloperContributionVo {
        private String username;
        private Integer totalStars;
        private Integer totalForks;
        private Integer totalIssues;
        private Integer totalCommits;
        private Integer totalContributions;
        private Integer totalPullRequests;
        private Integer totalUserIssues;
        private Double totalTalentRank;
        private String assessment;
        private String field;
        private Map<String, Integer> languageCount;
        private Map<String, Integer> topicCount;
}
