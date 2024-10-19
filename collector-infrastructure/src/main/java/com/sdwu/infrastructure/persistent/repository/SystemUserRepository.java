package com.sdwu.infrastructure.persistent.repository;

import cn.dev33.satoken.stp.StpUtil;
import com.sdwu.domain.sysuser.model.entity.SysRole;
import com.sdwu.domain.sysuser.model.entity.SystemUser;
import com.sdwu.domain.sysuser.repository.ISystemUserRepository;
import com.sdwu.infrastructure.persistent.dao.ISysRoleDao;
import com.sdwu.infrastructure.persistent.dao.ISysUserRoleDao;
import com.sdwu.infrastructure.persistent.dao.ISystemUserDao;
import com.sdwu.infrastructure.persistent.po.SysRolePO;
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


    @Resource
    private ISysRoleDao sysRoleDao;

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

        int insert = systemUserDao.insert(systemUserPO);

        user.setUserId(systemUserPO.getUserId());
        insertUserRole(user);


        return insert;
    }

    @Override
    public SystemUser selectUserById(Long userId) {
        SystemUserPO systemUserPO = systemUserDao.selectOne(SystemUserPO::getUserId, userId);
        SystemUser systemUser = SystemUserPO.convertToDomain(systemUserPO);
        systemUser.setRoleIds(sysUserRoleDao.selectList(SysUserRolePO::getUserId, userId).stream()
                .map(SysUserRolePO::getRoleId)
                .toArray(Long[]::new));
        return systemUser;
    }

    @Override
    public Integer resetUserPwd(SystemUser user) {
        SystemUserPO systemUserPO = SystemUserPO.convertToPo(user);
        return systemUserDao.updateById(systemUserPO);
    }

    @Override
    @Transactional
    public Integer updateUser(SystemUser user) {

        sysUserRoleDao.delete(SysUserRolePO::getUserId, user.getUserId());

        insertUserRole(user);

        SystemUserPO systemUserPO = SystemUserPO.convertToPo(user);

        return systemUserDao.updateById(systemUserPO);
    }

    @Override
    public Integer deleteUserByIds(Long[] userIds) {
        // 删除用户与角色关联
        sysUserRoleDao.deleteUserRole(userIds);

        return systemUserDao.deleteUserByIds(userIds);
    }

    @Override
    public Integer changeStatus(SystemUser user) {
        SystemUserPO systemUserPO = SystemUserPO.convertToPo(user);
        return systemUserDao.updateById(systemUserPO);
    }

    @Override
    public String selectUserRoleGroup(Long userId) {
        List<SysUserRolePO> sysUserRolePOList = sysUserRoleDao.selectSysUserRoleListByUserId(userId);
        List<String> rolesName=null;
        if (sysUserRolePOList != null && !sysUserRolePOList.isEmpty()) {
            List<Long> roleIds = sysUserRolePOList.stream()
                    .map(SysUserRolePO::getRoleId)
                    .collect(Collectors.toList());
            List<SysRolePO> roles = sysRoleDao.findByRoleIds(roleIds);
            rolesName = roles.stream().map(SysRolePO::getRoleName).collect(Collectors.toList());
        }
        return String.join(",", rolesName);
    }

    @Override
    public boolean checkUserNameUnique(String userName) {
        if (systemUserDao.findByUserName(userName) == null){
            return true;
        }
        return false;
    }


    public void insertUserRole(SystemUser user) {
        List<SysUserRolePO> list = new ArrayList<>(user.getRoleIds().length);
        Arrays.stream(user.getRoleIds()).forEach(roleId -> {
            SysUserRolePO sysUserRolePO = new SysUserRolePO();
            sysUserRolePO.setRoleId(roleId);
            sysUserRolePO.setUserId(user.getUserId());
            list.add(sysUserRolePO);
        });

        sysUserRoleDao.insertUserRoleBatch(list);
    }
}
