package com.sdwu.domain.sysuser.service;

import com.sdwu.domain.sysuser.model.entity.SystemUser;
import com.sdwu.domain.sysuser.repository.ISystemUserRepository;
import com.sdwu.types.model.PageResult;
import com.sun.org.apache.xml.internal.resolver.helpers.PublicId;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;

@Component
public class SysUserServiceImpl implements ISysUserService {
    @Resource
    private ISystemUserRepository systemUserRepository;
    @Override
    public PageResult<SystemUser> selcetUserPage(SystemUser user) {
        return systemUserRepository.selcetUserPage(user);
    }

    @Override
    public Integer insertUser(SystemUser user) {
        return systemUserRepository.insertUser(user);
    }





}
