package com.sdwu.infrastructure.persistent.dao;

import com.sdwu.infrastructure.persistent.po.SysDictDataPO;
import com.sdwu.infrastructure.persistent.utils.BaseMapperX;

import java.util.List;

public interface ISysDcitDataDao extends BaseMapperX<SysDictDataPO> {
    default List<SysDictDataPO> selectDictDataByType(String dictType){
        return selectList(SysDictDataPO::getDictType, dictType);
    };
}
