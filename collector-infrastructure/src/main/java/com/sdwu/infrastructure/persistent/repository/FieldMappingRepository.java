package com.sdwu.infrastructure.persistent.repository;

import com.sdwu.domain.github.model.entity.FieldMapping;
import com.sdwu.domain.github.repository.IFieldMappingRepository;
import com.sdwu.infrastructure.persistent.dao.IFieldMappingDao;
import com.sdwu.infrastructure.persistent.po.FieldMappingPO;
import com.sdwu.infrastructure.persistent.utils.LambdaQueryWrapperX;
import com.sdwu.types.model.PageResult;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class FieldMappingRepository implements IFieldMappingRepository {

    @Resource
    private IFieldMappingDao fieldMappingDao;

    @Override
    public PageResult<FieldMapping> selectFieldMappingPage(FieldMapping fieldMapping) {
        PageResult<FieldMappingPO> pageResult = fieldMappingDao.selectFieldMappingPage(fieldMapping);
        if (pageResult == null) {
            return PageResult.empty();
        }
        List<FieldMapping> mappings = pageResult.getList().stream()
                .map(FieldMappingPO::convertToDomain)
                .collect(Collectors.toList());
        return new PageResult<>(mappings, pageResult.getTotal());
    }

    @Override
    public Boolean insertFieldMapping(FieldMapping fieldMapping) {
        FieldMappingPO po = convertToPO(fieldMapping);
        return fieldMappingDao.insert(po) > 0;
    }

    @Override
    public Boolean updateFieldMapping(FieldMapping fieldMapping) {
        FieldMappingPO po = convertToPO(fieldMapping);
        return fieldMappingDao.updateById(po) > 0;
    }

    @Override
    public Boolean deleteFieldMapping(Long mappingId) {
        return fieldMappingDao.delete(new LambdaQueryWrapperX<FieldMappingPO>().eq(FieldMappingPO::getMappingId, mappingId))>0;
    }

    @Override
    public FieldMapping getFieldMapping(Long mappingId) {
        FieldMappingPO po = fieldMappingDao.selectById(mappingId);
        return po != null ? FieldMappingPO.convertToDomain(po) : null;
    }

    @Override
    public Boolean batchSaveFieldMappings(List<FieldMapping> mappings) {
        List<FieldMappingPO> pos = mappings.stream()
                .map(this::convertToPO)
                .collect(Collectors.toList());
        return fieldMappingDao.insertBatch(pos);
    }

    @Override
    public List<FieldMapping> getFieldMappingsByFieldName(String fieldName) {
        List<FieldMappingPO> pos = fieldMappingDao.selectByFieldName(fieldName);
        return pos.stream()
                .map(FieldMappingPO::convertToDomain)
                .collect(Collectors.toList());
    }

    private FieldMappingPO convertToPO(FieldMapping fieldMapping) {
        return FieldMappingPO.builder()
                .mappingId(fieldMapping.getMappingId())
                .fieldId(fieldMapping.getFieldId())
                .fieldName(fieldMapping.getFieldName())
                .mappingType(fieldMapping.getMappingType())
                .mappingValue(fieldMapping.getMappingValue())
                .mappingPriority(fieldMapping.getMappingPriority())
                .build();
    }
}
