package com.sdwu.infrastructure.persistent.dao;

import com.sdwu.infrastructure.persistent.po.SysRoleMenuPO;
import com.sdwu.infrastructure.persistent.utils.BaseMapperX;
import com.sdwu.infrastructure.persistent.utils.LambdaQueryWrapperX;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public interface ISysRoleMenuDao extends BaseMapperX<SysRoleMenuPO> {
    default List<SysRoleMenuPO> selectRoleMenuByRoleIds(Set<Long> roleIds){
        return selectList(new LambdaQueryWrapperX<SysRoleMenuPO>().inIfPresent(SysRoleMenuPO::getRoleId, roleIds));
    };

    int batchRoleMenu(List<SysRoleMenuPO> list);


    default int deleteByRoleId(Long roleId){
        return delete(SysRoleMenuPO::getRoleId, roleId);
    };


    int deleteRoleMenu(Long[] roleIds);

    default Long checkMenuExistRole(Long menuId){
        return selectCount(SysRoleMenuPO::getMenuId, menuId);
    };
}
