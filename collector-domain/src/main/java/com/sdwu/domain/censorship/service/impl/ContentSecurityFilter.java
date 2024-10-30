//package com.sdwu.domain.censorship.service.impl;
//
//import com.baidu.aip.contentcensor.AipContentCensor;
//import com.sdwu.domain.censorship.annotation.LogicStrategy;
//import com.sdwu.domain.censorship.factory.DefaultLogicFactory;
//import com.sdwu.domain.censorship.model.entity.RuleActionEntity;
//import com.sdwu.domain.censorship.model.entity.RuleMatterEntity;
//import com.sdwu.domain.censorship.model.vo.LogicCheckTypeVO;
//import com.sdwu.domain.censorship.service.IRuleLogicFilter;
//import lombok.extern.slf4j.Slf4j;
//
//import org.json.JSONObject;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//

//@Slf4j
//@Component
//@LogicStrategy(logicMode = DefaultLogicFactory.LogicModel.CONTENT_SECURITY)
//public class ContentSecurityFilter implements IRuleLogicFilter {
//
//    @Resource
//    private AipContentCensor aipContentCensor;
//
//    @Override
//    public RuleActionEntity<RuleMatterEntity> filter(RuleMatterEntity ruleMatterEntity) {
//        JSONObject jsonObject = aipContentCensor.textCensorUserDefined(ruleMatterEntity.getContent());
//        if (!jsonObject.isNull("conclusion") && "不合规".equals(jsonObject.get("conclusion"))) {
//            return RuleActionEntity.<RuleMatterEntity>builder()
//                    .type(LogicCheckTypeVO.REFUSE)
//                    .data(RuleMatterEntity.builder().content("内容不合规").build())
//                    .build();
//        }
//        // 返回结果
//        return RuleActionEntity.<RuleMatterEntity>builder()
//                .type(LogicCheckTypeVO.SUCCESS)
//                .data(ruleMatterEntity)
//                .build();
//    }
//
//}
