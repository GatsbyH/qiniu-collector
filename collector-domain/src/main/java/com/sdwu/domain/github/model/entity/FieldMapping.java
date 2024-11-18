package com.sdwu.domain.github.model.entity;

import com.sdwu.types.model.PageParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FieldMapping extends PageParam implements Serializable {
    private Long mappingId;      // 映射ID
    private Long fieldId;        // 领域ID
    private String fieldName;    // 领域名称
    private String mappingType;  // 映射类型(TOPIC/LANGUAGE/DESCRIPTION)
    private String mappingValue; // 映射值
    private Integer mappingPriority; // 优先级(数值越大优先级越高)
} 