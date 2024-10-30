package com.sdwu.domain.censorship.service;


import com.sdwu.domain.censorship.model.entity.RuleActionEntity;
import com.sdwu.domain.censorship.model.entity.RuleMatterEntity;


public interface IRuleLogicFilter {

    RuleActionEntity<RuleMatterEntity> filter(RuleMatterEntity ruleMatterEntity);

}
