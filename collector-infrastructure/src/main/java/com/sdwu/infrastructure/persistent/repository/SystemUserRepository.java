package com.sdwu.infrastructure.persistent.repository;

import cn.dev33.satoken.stp.StpUtil;
import com.sdwu.domain.sysuser.model.entity.SysRole;
import com.sdwu.domain.sysuser.model.entity.SystemUser;
import com.sdwu.domain.sysuser.repository.ISystemUserRepository;
import com.sdwu.infrastructure.persistent.dao.ISysUserRoleDao;
import com.sdwu.infrastructure.persistent.dao.ISystemUserDao;
import com.sdwu.infrastructure.persistent.po.SysUserRolePO;
import com.sdwu.infrastructure.persistent.po.SystemUserPO;
import com.sdwu.types.enums.ResponseCode;
import com.sdwu.types.exception.AppException;
import com.sdwu.types.model.PageResult;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class SystemUserRepository implements ISystemUserRepository {

    @Resource
    private ISystemUserDao systemUserDao;


    @Resource
    private ISysUserRoleDao sysUserRoleDao;

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

    @Override
    public PageResult<SystemUser> selcetUserPage(SystemUser user) {
        PageResult<SystemUserPO> sysUserPage = systemUserDao.selcetUserPage(user);
        if (sysUserPage == null){
            return PageResult.empty();
        }
        List<SystemUser> systemUsers = sysUserPage.getList().stream()
                .map(SystemUserPO::convertToDomain)
                .collect(Collectors.toList());
        return  new PageResult<>(systemUsers, sysUserPage.getTotal());
    }

    @Override
    @Transactional
    public Integer insertUser(SystemUser user) {
        SystemUserPO systemUserPO = SystemUserPO.convertToPo(user);
        List<SysUserRolePO> list = new ArrayList<>(user.getRoleIds().length);
        int insert = systemUserDao.insert(systemUserPO);
        Arrays.stream(user.getRoleIds()).forEach(roleId -> {
            SysUserRolePO sysUserRolePO = new SysUserRolePO();
            sysUserRolePO.setRoleId(roleId);
            sysUserRolePO.setUserId(systemUserPO.getUserId());
            list.add(sysUserRolePO);
        });
        sysUserRoleDao.insertUserRoleBatch(list);
        return insert;
    }
}
