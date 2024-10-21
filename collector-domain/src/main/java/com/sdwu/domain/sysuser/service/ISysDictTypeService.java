package com.sdwu.domain.sysuser.service;

import com.sdwu.domain.sysuser.model.entity.SysDictData;

import java.util.List;

public interface ISysDictTypeService {
    List<SysDictData> selectDictDataByType(String dictType);
}
