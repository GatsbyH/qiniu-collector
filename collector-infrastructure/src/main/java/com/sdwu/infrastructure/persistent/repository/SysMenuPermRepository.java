package com.sdwu.infrastructure.persistent.repository;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.sdwu.domain.sysuser.repository.ISysMenuPermRepository;
import com.sdwu.infrastructure.persistent.dao.*;
import com.sdwu.infrastructure.persistent.po.SysMenuPO;
import com.sdwu.infrastructure.persistent.po.SysRoleMenuPO;
import com.sdwu.infrastructure.persistent.po.SysUserRolePO;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;
@Repository
public class SysMenuPermRepository implements ISysMenuPermRepository {


    @Resource
    private ISysRoleMenuDao sysRoleMenuDao;

    @Resource
    private ISysUserRoleDao sysUserRoleDao;

    @Resource
    private ISysMenuDao sysMenuDao;
    @Override
    public List<String> findMenuPermListByUserId(Long userId) {
        List<SysUserRolePO> sysUserRolePOList = sysUserRoleDao.selectSysUserRoleListByUserId(userId);
        if (sysUserRolePOList == null || sysUserRolePOList.isEmpty()) {
            return Collections.emptyList();
        }
        Set<Long> roleIds = sysUserRolePOList.stream()
                .map(SysUserRolePO::getRoleId)
                .filter(Objects::nonNull) // 过滤掉null值
                .collect(Collectors.toSet());
        List<SysRoleMenuPO> sysRoleMenuList = sysRoleMenuDao.selectRoleMenuByRoleIds(roleIds);
        if (sysRoleMenuList == null || sysRoleMenuList.isEmpty()) {
            return Collections.emptyList();
        }
        Set<Long> menuIds = sysRoleMenuList.stream()
                .map(SysRoleMenuPO::getMenuId)
                .filter(Objects::nonNull) // 过滤掉null值
                .collect(Collectors.toSet());
        List<SysMenuPO> sysMenuPOList = sysMenuDao.selectMenuByMenuIds(menuIds);
        Set<String> menuPerm = sysMenuPOList.stream()
                .map(SysMenuPO::getPerms)
                .filter(perms -> perms != null && !perms.isEmpty())
                .collect(Collectors.toSet());
        return new ArrayList<>(menuPerm);
    }
}
