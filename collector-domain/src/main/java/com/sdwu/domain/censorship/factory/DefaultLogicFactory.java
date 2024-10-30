package com.sdwu.domain.censorship.factory;


import com.alibaba.fastjson2.util.AnnotationUtils;
import com.sdwu.domain.censorship.annotation.LogicStrategy;
import com.sdwu.domain.censorship.model.entity.RuleActionEntity;
import com.sdwu.domain.censorship.model.entity.RuleMatterEntity;
import com.sdwu.domain.censorship.model.vo.LogicCheckTypeVO;
import com.sdwu.domain.censorship.service.IRuleLogicFilter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 规则工厂
 * @create 2024-01-07 09:45
 */
@Service
public class DefaultLogicFactory {

    public Map<String, IRuleLogicFilter> logicFilterMap = new ConcurrentHashMap<>();

    public DefaultLogicFactory(List<IRuleLogicFilter> logicFilters) {
        logicFilters.forEach(logic -> {
            LogicStrategy strategy = AnnotationUtils.findAnnotation(logic.getClass(), LogicStrategy.class);
            if (null != strategy) {
                logicFilterMap.put(strategy.logicMode().getCode(), logic);
            }
        });
    }

    public RuleActionEntity<RuleMatterEntity> doCheckLogic(RuleMatterEntity ruleMatterEntity, LogicModel... logics) {
        RuleActionEntity<RuleMatterEntity> entity = null;
        for (LogicModel model : logics) {
            entity = logicFilterMap.get(model.code).filter(ruleMatterEntity);
            if (!LogicCheckTypeVO.SUCCESS.equals(entity.getType())) return entity;
            ruleMatterEntity = entity.getData();
        }
        return entity != null ? entity :
                RuleActionEntity.<RuleMatterEntity>builder()
                        .type(LogicCheckTypeVO.SUCCESS)
                        .data(ruleMatterEntity)
                        .build();
    }

    /**
     * 规则逻辑枚举
     */
    public enum LogicModel {

        SENSITIVE_WORD("SENSITIVE_WORD", "敏感词过滤"),
        CONTENT_SECURITY("CONTENT_SECURITY", "内容安全"),
        ;

        private String code;
        private String info;

        LogicModel(String code, String info) {
            this.code = code;
            this.info = info;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }
    }

}
