package com.sdwu.domain.sysuser.service;

import com.sdwu.domain.sysuser.model.entity.SystemUser;
import com.sdwu.types.model.PageResult;

public interface ISysUserService {
    PageResult<SystemUser> selcetUserPage(SystemUser user);

    Integer insertUser(SystemUser user);

    SystemUser  selectUserById(Long userId);

    Integer updateUser(SystemUser user);

    Integer deleteUserByIds(Long[] userIds);

    Integer resetUserPwd(SystemUser user);

    Integer changeStatus(SystemUser user);

    String selectUserRoleGroup(Long userId);

    boolean checkUserNameUnique(String userName);

    void setCaptchaCache(String visitorId, String code);
}
