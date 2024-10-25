package com.sdwu.domain.sysuser.service;

import com.sdwu.domain.sysuser.model.entity.SystemUser;
import com.sdwu.domain.sysuser.repository.ISystemUserRepository;
import com.sdwu.types.model.PageResult;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

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

    @Override
    public SystemUser selectUserById(Long userId) {
        return systemUserRepository.selectUserById(userId);
    }

    @Override
    public Integer updateUser(SystemUser user) {
        return systemUserRepository.updateUser(user);
    }

    @Override
    public Integer deleteUserByIds(Long[] userIds) {
        return systemUserRepository.deleteUserByIds(userIds);
    }

    @Override
    public Integer resetUserPwd(SystemUser user) {
        return systemUserRepository.resetUserPwd(user);
    }

    @Override
    public Integer changeStatus(SystemUser user) {
        return systemUserRepository.changeStatus(user);
    }

    @Override
    public String selectUserRoleGroup(Long userId) {
        return systemUserRepository.selectUserRoleGroup(userId);
    }

    @Override
    public boolean checkUserNameUnique(String userName) {
        return systemUserRepository.checkUserNameUnique(userName);
    }

    @Override
    public void setCaptchaCache(String visitorId, String code) {
         systemUserRepository.setCaptchaCache(visitorId, code);
    }


}
