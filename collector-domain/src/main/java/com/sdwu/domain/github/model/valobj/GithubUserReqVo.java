package com.sdwu.domain.github.model.valobj;

import com.sdwu.types.model.PageParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GithubUserReqVo extends PageParam {
    private String username;
}