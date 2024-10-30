package com.sdwu.domain.censorship.model.entity;


import com.sdwu.domain.censorship.model.vo.LogicCheckTypeVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RuleActionEntity<T> {

    private LogicCheckTypeVO type;
    private T data;

}
