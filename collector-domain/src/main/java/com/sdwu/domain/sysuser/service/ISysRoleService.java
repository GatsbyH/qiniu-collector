package com.sdwu.domain.sysuser.service;


import com.sdwu.domain.sysuser.model.entity.SysRole;

import java.util.List;

public interface ISysRoleService {

    List<SysRole> selectRoleAll();
}
