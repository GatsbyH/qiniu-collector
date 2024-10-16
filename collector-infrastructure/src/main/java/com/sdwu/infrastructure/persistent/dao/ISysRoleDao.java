package com.sdwu.infrastructure.persistent.dao;

import com.sdwu.infrastructure.persistent.po.SysRolePO;
import com.sdwu.infrastructure.persistent.utils.BaseMapperX;
import com.sdwu.infrastructure.persistent.utils.LambdaQueryWrapperX;

import java.util.List;

public interface ISysRoleDao extends BaseMapperX<SysRolePO> {
    default SysRolePO findByRoleId(Long roleId)
    {
        return selectOne(new LambdaQueryWrapperX<SysRolePO>().eq(SysRolePO::getRoleId,roleId));
    }

    default List<SysRolePO> findByRoleIds(List<Long> roleIds){
        return selectList(new LambdaQueryWrapperX<SysRolePO>().in(SysRolePO::getRoleId,roleIds));
    };
}
