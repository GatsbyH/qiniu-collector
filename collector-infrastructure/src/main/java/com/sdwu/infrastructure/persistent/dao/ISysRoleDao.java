package com.sdwu.infrastructure.persistent.dao;

import com.sdwu.domain.sysuser.model.entity.SysRole;
import com.sdwu.infrastructure.persistent.po.SysRolePO;
import com.sdwu.infrastructure.persistent.utils.BaseMapperX;
import com.sdwu.infrastructure.persistent.utils.LambdaQueryWrapperX;
import com.sdwu.types.model.PageResult;

import java.util.List;
import java.util.Objects;

public interface ISysRoleDao extends BaseMapperX<SysRolePO> {
    default SysRolePO findByRoleId(Long roleId)
    {
        return selectOne(new LambdaQueryWrapperX<SysRolePO>().eq(SysRolePO::getRoleId,roleId));
    }

    default List<SysRolePO> findByRoleIds(List<Long> roleIds){
        return selectList(new LambdaQueryWrapperX<SysRolePO>().in(SysRolePO::getRoleId,roleIds));
    };

    default PageResult<SysRolePO> selectRolePage(SysRole role){
        // 从params中获取beginTime和endTime，如果不存在则返回空字符串
        String beginTime = Objects.toString(role.getParams().get("beginTime"), null);
        String endTime = Objects.toString(role.getParams().get("endTime"), null);

        return selectPage(role,new LambdaQueryWrapperX<SysRolePO>()
                .eqIfPresent(SysRolePO::getRoleName,role.getRoleName())
                .eqIfPresent(SysRolePO::getStatus,role.getStatus())
                .eqIfPresent(SysRolePO::getRoleKey,role.getRoleKey())
                .betweenIfPresent(SysRolePO::getCreateTime,beginTime,endTime));
    }



}
