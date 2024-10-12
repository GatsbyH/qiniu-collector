package com.sdwu.domain.sysuser.repository;

import com.sdwu.domain.sysuser.model.entity.SystemUser;

public interface ISystemUserRepository {
    SystemUser findByUserName(String userName);
}
