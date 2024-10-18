package com.sdwu.domain.sysuser.repository;

import com.sdwu.domain.sysuser.model.entity.SysRole;
import com.sdwu.types.model.PageResult;

import java.util.Collection;
import java.util.List;

public interface ISysRoleRepository {

    List<String> findRoleListByUserId(Long userId);


    List<SysRole> selectRoleAll();

    PageResult<SysRole> selectRolePage(SysRole role);

    Integer insertRole(SysRole role);

    List<SysRole> findRoleListByRoleName(SysRole role);

    List<SysRole> findRoleListByRoleKey(SysRole role);
}
