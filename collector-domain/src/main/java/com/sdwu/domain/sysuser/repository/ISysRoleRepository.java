package com.sdwu.domain.sysuser.repository;

import com.sdwu.domain.sysuser.model.entity.SysRole;

import java.util.List;

public interface ISysRoleRepository {

    List<String> findRoleListByUserId(Long userId);


    List<SysRole> selectRoleAll();
}
