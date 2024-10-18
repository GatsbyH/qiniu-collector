package com.sdwu.domain.sysuser.repository;

import com.sdwu.domain.sysuser.model.entity.SystemUser;
import com.sdwu.types.model.PageResult;

public interface ISystemUserRepository {
    SystemUser findByUserName(String userName);

    PageResult<SystemUser> selcetUserPage(SystemUser user);

    Integer insertUser(SystemUser user);
}
