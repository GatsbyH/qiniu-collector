package com.sdwu.domain.github.service;

import com.sdwu.domain.github.model.entity.FieldMapping;
import com.sdwu.domain.github.repository.IFieldMappingRepository;
import com.sdwu.types.model.PageResult;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class FieldMappingServiceImpl implements IFieldMappingService {
    
    @Resource
    private IFieldMappingRepository fieldMappingRepository;

    @Override
    public PageResult<FieldMapping> selectFieldMappingPage(FieldMapping fieldMapping) {
        return fieldMappingRepository.selectFieldMappingPage(fieldMapping);
    }

    @Override
    public Boolean insertFieldMapping(FieldMapping fieldMapping) {
        return fieldMappingRepository.insertFieldMapping(fieldMapping);
    }

    @Override
    public Boolean updateFieldMapping(FieldMapping fieldMapping) {
        return fieldMappingRepository.updateFieldMapping(fieldMapping);
    }

    @Override
    public Boolean deleteFieldMapping(Long mappingId) {
        return fieldMappingRepository.deleteFieldMapping(mappingId);
    }

    @Override
    public FieldMapping getFieldMapping(Long mappingId) {
        return fieldMappingRepository.getFieldMapping(mappingId);
    }

    @Override
    public Boolean batchSaveFieldMappings(List<FieldMapping> mappings) {
        return fieldMappingRepository.batchSaveFieldMappings(mappings);
    }

    @Override
    public List<FieldMapping> getFieldMappingsByFieldName(String fieldName) {
        return fieldMappingRepository.getFieldMappingsByFieldName(fieldName);
    }
} 