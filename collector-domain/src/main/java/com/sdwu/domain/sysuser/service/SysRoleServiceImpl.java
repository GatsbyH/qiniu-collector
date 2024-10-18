package com.sdwu.domain.sysuser.service;

import com.sdwu.domain.sysuser.model.entity.SysRole;
import com.sdwu.domain.sysuser.repository.ISysRoleRepository;
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
}
