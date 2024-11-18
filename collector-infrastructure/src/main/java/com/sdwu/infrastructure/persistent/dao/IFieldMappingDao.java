package com.sdwu.infrastructure.persistent.dao;

import com.sdwu.domain.github.model.entity.FieldMapping;
import com.sdwu.infrastructure.persistent.po.FieldMappingPO;
import com.sdwu.infrastructure.persistent.utils.BaseMapperX;
import com.sdwu.infrastructure.persistent.utils.LambdaQueryWrapperX;
import com.sdwu.types.model.PageResult;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface IFieldMappingDao extends BaseMapperX<FieldMappingPO> {

    /**
     * 批量插入字段映射
     * @param list 字段映射列表
     * @return 是否成功
     */
    Boolean insertBatch(@Param("list") List<FieldMappingPO> list);

    /**
     * 分页查询字段映射
     */
    default PageResult<FieldMappingPO> selectFieldMappingPage(FieldMapping fieldMapping) {
        return selectPage(fieldMapping, new LambdaQueryWrapperX<FieldMappingPO>()
                .eqIfPresent(FieldMappingPO::getFieldId, fieldMapping.getFieldId())
                .likeIfPresent(FieldMappingPO::getFieldName, fieldMapping.getFieldName())
                .eqIfPresent(FieldMappingPO::getMappingType, fieldMapping.getMappingType())
                .likeIfPresent(FieldMappingPO::getMappingValue, fieldMapping.getMappingValue())
                .orderByDesc(FieldMappingPO::getMappingPriority)
        );
    }

    /**
     * 根据字段名称查询映射列表
     */
    default List<FieldMappingPO> selectByFieldName(String fieldName) {
        return selectList(new LambdaQueryWrapperX<FieldMappingPO>()
                .eq(FieldMappingPO::getFieldName, fieldName)
                .orderByDesc(FieldMappingPO::getMappingPriority));
    }
}
