package com.sdwu.domain.github.service;

import com.sdwu.domain.github.model.valobj.DeveloperContributionVo;

import java.io.IOException;
import java.util.List;

public interface ITalentRankService {
    double getTalentRankByUserName(String username) throws IOException;

    DeveloperContributionVo getDeveloperAssessment(String username) throws IOException;

    String getDeveloperTechnicalAbility(String username);
}
