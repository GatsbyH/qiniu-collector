package com.sdwu.domain.sysuser.service;

import com.sdwu.domain.sysuser.model.entity.SysDictData;
import com.sdwu.domain.sysuser.repository.ISysDictDataRepository;
import com.sdwu.domain.sysuser.repository.ISysDictTypeRepository;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
@Component
public class SysDictTypeServiceImpl implements ISysDictTypeService{

    @Resource
    private ISysDictDataRepository sysDictDataRepository;

    @Resource
    private ISysDictTypeRepository sysDictTypeRepository;

    @Override
    public List<SysDictData> selectDictDataByType(String dictType) {
        return sysDictTypeRepository.selectDictDataByType(dictType);
    }
}
