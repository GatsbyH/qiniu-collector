package com.sdwu.domain.github.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LanguageCountRespVo {
    private String item;
    private Integer a;
}
