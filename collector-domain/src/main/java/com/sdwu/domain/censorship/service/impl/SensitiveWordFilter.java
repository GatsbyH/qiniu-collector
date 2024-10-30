package com.sdwu.domain.censorship.service.impl;


import com.github.houbb.sensitive.word.bs.SensitiveWordBs;
import com.sdwu.domain.censorship.annotation.LogicStrategy;
import com.sdwu.domain.censorship.factory.DefaultLogicFactory;
import com.sdwu.domain.censorship.model.entity.RuleActionEntity;
import com.sdwu.domain.censorship.model.entity.RuleMatterEntity;
import com.sdwu.domain.censorship.model.vo.LogicCheckTypeVO;
import com.sdwu.domain.censorship.service.IRuleLogicFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


@Slf4j
@Component
@LogicStrategy(logicMode = DefaultLogicFactory.LogicModel.SENSITIVE_WORD)
public class SensitiveWordFilter implements IRuleLogicFilter {

    @Resource
    private SensitiveWordBs words;

    @Override
    public RuleActionEntity<RuleMatterEntity> filter(RuleMatterEntity ruleMatterEntity) {
        // 敏感词过滤
        String content = ruleMatterEntity.getContent();
        String replace = words.replace(content);
        // 返回结果
        return RuleActionEntity.<RuleMatterEntity>builder()
                .type(LogicCheckTypeVO.SUCCESS)
                .data(RuleMatterEntity.builder().content(replace).build())
                .build();
    }

}
