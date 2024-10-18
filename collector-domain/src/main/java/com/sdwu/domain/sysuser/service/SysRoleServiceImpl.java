package com.sdwu.domain.sysuser.service;

import com.sdwu.domain.sysuser.model.entity.SysRole;
import com.sdwu.domain.sysuser.repository.ISysRoleRepository;
import com.sdwu.types.common.UserConstants;
import com.sdwu.types.model.PageResult;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class SysRoleServiceImpl implements ISysRoleService{

    @Resource
    private ISysRoleRepository systemRoleRepository;


    @Override
    public List<SysRole> selectRoleAll() {
        return systemRoleRepository.selectRoleAll();
    }

    @Override
    public PageResult<SysRole> selectRolePage(SysRole role) {
        return systemRoleRepository.selectRolePage(role);
    }

    @Override
    public Integer insertRole(SysRole role) {
        return systemRoleRepository.insertRole(role);
    }

    @Override
    public boolean checkRoleNameUnique(SysRole role) {
        List<SysRole> sysRoleList = systemRoleRepository.findRoleListByRoleName(role);
        if (sysRoleList.size()>0){
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    @Override
    public boolean checkRoleKeyUnique(SysRole role) {
        if (systemRoleRepository.findRoleListByRoleKey(role).size()>0){
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }


}