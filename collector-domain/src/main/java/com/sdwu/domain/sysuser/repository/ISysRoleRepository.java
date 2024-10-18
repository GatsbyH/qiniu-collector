package com.sdwu.domain.sysuser.repository;

import com.sdwu.domain.sysuser.model.entity.SysRole;
import com.sdwu.types.model.PageResult;

import java.util.List;

public interface ISysRoleRepository {

    List<String> findRoleListByUserId(Long userId);


    List<SysRole> selectRoleAll();

    PageResult<SysRole> selectRolePage(SysRole role);
}
