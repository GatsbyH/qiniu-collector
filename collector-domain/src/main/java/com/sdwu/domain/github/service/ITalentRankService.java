package com.sdwu.domain.github.service;

import com.sdwu.domain.github.model.valobj.DeveloperContributionVo;

import java.io.IOException;
import java.util.List;

public interface ITalentRankService {
    //废弃
    double getTalentRankByUserName(String username) throws IOException;

    //废弃
    DeveloperContributionVo getDeveloperAssessment(String username) throws IOException;

    String getDeveloperTechnicalAbility(String username);
}
