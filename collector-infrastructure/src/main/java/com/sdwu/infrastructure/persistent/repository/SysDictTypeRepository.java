package com.sdwu.infrastructure.persistent.repository;

import com.sdwu.domain.sysuser.model.entity.SysDictData;
import com.sdwu.domain.sysuser.repository.ISysDictDataRepository;
import com.sdwu.domain.sysuser.repository.ISysDictTypeRepository;
import com.sdwu.infrastructure.persistent.dao.ISysDcitDataDao;
import com.sdwu.infrastructure.persistent.po.SysDictDataPO;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class SysDictTypeRepository implements ISysDictTypeRepository {
    @Resource
    private ISysDcitDataDao sysDcitDataDao;

    @Override
    public List<SysDictData> selectDictDataByType(String dictType) {
        List<SysDictDataPO> sysDictDataPOList= sysDcitDataDao.selectDictDataByType(dictType);
        List<SysDictData> sysDictData = sysDictDataPOList.stream()
                .map(SysDictDataPO::convertToDomain)
                .collect(Collectors.toList());
        return sysDictData;
    }
}
