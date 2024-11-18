package com.sdwu.infrastructure.persistent.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.sdwu.domain.github.model.entity.FieldMapping;
import com.sdwu.infrastructure.persistent.utils.BaseDO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("field_mapping")
public class FieldMappingPO extends BaseDO {
    private Long mappingId;      
    private Long fieldId;        
    private String fieldName;    
    private String mappingType;  
    private String mappingValue; 
    private Integer mappingPriority;

    public static FieldMapping convertToDomain(FieldMappingPO po) {
        return FieldMapping.builder()
                .mappingId(po.getMappingId())
                .fieldId(po.getFieldId())
                .fieldName(po.getFieldName())
                .mappingType(po.getMappingType())
                .mappingValue(po.getMappingValue())
                .mappingPriority(po.getMappingPriority())
                .build();
    }
} 