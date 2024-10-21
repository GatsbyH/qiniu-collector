package com.sdwu.domain.sysuser.repository;

import com.sdwu.domain.sysuser.model.entity.SystemUser;
import com.sdwu.types.model.PageResult;

public interface ISystemUserRepository {
    SystemUser findByUserName(String userName);

    PageResult<SystemUser> selcetUserPage(SystemUser user);

    Integer insertUser(SystemUser user);

    SystemUser selectUserById(Long userId);

    Integer updateUser(SystemUser user);
    Integer resetUserPwd(SystemUser user);

    Integer deleteUserByIds(Long[] userIds);

    Integer changeStatus(SystemUser user);

    String selectUserRoleGroup(Long userId);

    boolean checkUserNameUnique(String userName);

    void setCaptchaCache(String visitorId, String code);

    boolean validateCaptcha(String username, String code, String uuid);
}
