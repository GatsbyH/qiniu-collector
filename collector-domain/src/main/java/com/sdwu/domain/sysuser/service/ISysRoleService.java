package com.sdwu.domain.sysuser.service;


import com.sdwu.domain.sysuser.model.entity.SysRole;
import com.sdwu.types.model.PageResult;

import java.util.List;

public interface ISysRoleService {

    List<SysRole> selectRoleAll();

    PageResult<SysRole> selectRolePage(SysRole role);

    Integer insertRole(SysRole role);

    boolean checkRoleNameUnique(SysRole role);

    boolean checkRoleKeyUnique(SysRole role);
}
