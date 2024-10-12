package com.sdwu.infrastructure.persistent.dao;

import com.sdwu.infrastructure.persistent.po.SystemUserPO;
import com.sdwu.infrastructure.persistent.utils.BaseMapperX;
import com.sdwu.infrastructure.persistent.utils.LambdaQueryWrapperX;

public interface ISystemUserDao extends BaseMapperX<SystemUserPO> {
    default SystemUserPO  findByUserName(String username){
        return selectOne(new LambdaQueryWrapperX<SystemUserPO>().eq(SystemUserPO::getUserName, username));
    };
}
