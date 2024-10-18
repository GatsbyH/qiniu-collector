package com.sdwu.infrastructure.persistent.dao;

import com.sdwu.infrastructure.persistent.po.SysUserRolePO;
import com.sdwu.infrastructure.persistent.utils.BaseMapperX;
import com.sdwu.infrastructure.persistent.utils.LambdaQueryWrapperX;

import java.util.List;

public interface ISysUserRoleDao extends BaseMapperX<SysUserRolePO> {

    default List<SysUserRolePO> selectSysUserRoleListByUserId(Long userId)
    {
        return selectList(new LambdaQueryWrapperX<SysUserRolePO>().eq(SysUserRolePO::getUserId,userId));
    }

    int insertUserRoleBatch(List<SysUserRolePO> list);
}
