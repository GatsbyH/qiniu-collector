package com.sdwu.domain.sysuser.repository;

import com.sdwu.domain.sysuser.model.entity.SysDictData;

import java.util.List;

public interface ISysDictTypeRepository {
    List<SysDictData> selectDictDataByType(String dictType);
}
