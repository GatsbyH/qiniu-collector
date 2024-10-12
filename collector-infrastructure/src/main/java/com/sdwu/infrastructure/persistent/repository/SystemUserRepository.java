package com.sdwu.infrastructure.persistent.repository;

import com.sdwu.domain.sysuser.model.entity.SystemUser;
import com.sdwu.domain.sysuser.repository.ISystemUserRepository;
import com.sdwu.infrastructure.persistent.dao.ISystemUserDao;
import com.sdwu.infrastructure.persistent.po.SystemUserPO;
import com.sdwu.types.enums.ResponseCode;
import com.sdwu.types.exception.AppException;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public class SystemUserRepository implements ISystemUserRepository {

    @Resource
    private ISystemUserDao systemUserDao;

    @Override
    public SystemUser findByUserName(String username) {
        SystemUserPO systemUserPO = null;
        try {
            systemUserPO = systemUserDao.findByUserName(username);
        } catch (Exception e) {
            throw new AppException(ResponseCode.UN_ERROR.getCode());
        }
        if (systemUserPO == null)
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode());
        SystemUser systemUser = new SystemUser();
        return  systemUser.builder()
                .userId(systemUserPO.getUserId())
                .nickName(systemUserPO.getNickName())
                .userName(systemUserPO.getUserName())
                .password(systemUserPO.getPassword())
                .build();
    }
}
