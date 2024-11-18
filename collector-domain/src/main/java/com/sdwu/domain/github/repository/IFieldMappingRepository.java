package com.sdwu.domain.github.repository;

import java.util.List;

import com.sdwu.domain.github.model.entity.FieldMapping;
import com.sdwu.types.model.PageResult;

public interface IFieldMappingRepository {
    PageResult<FieldMapping> selectFieldMappingPage(FieldMapping fieldMapping);
    
    Boolean insertFieldMapping(FieldMapping fieldMapping);
    
    Boolean updateFieldMapping(FieldMapping fieldMapping);
    
    Boolean deleteFieldMapping(Long mappingId);
    
    FieldMapping getFieldMapping(Long mappingId);
    
    Boolean batchSaveFieldMappings(List<FieldMapping> mappings);
    
    List<FieldMapping> getFieldMappingsByFieldName(String fieldName);
} 