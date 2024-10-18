package com.sdwu.domain.sysuser.service;

import com.sdwu.domain.sysuser.model.entity.SystemUser;
import com.sdwu.types.model.PageResult;

public interface ISysUserService {
    PageResult<SystemUser> selcetUserPage(SystemUser user);

    Integer insertUser(SystemUser user);
}
