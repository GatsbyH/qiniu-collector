package com.sdwu.domain.github.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GithubRepoReqVo {
    private String username;
    private Integer pageNum = 1;
    private Integer pageSize = 10;
}
