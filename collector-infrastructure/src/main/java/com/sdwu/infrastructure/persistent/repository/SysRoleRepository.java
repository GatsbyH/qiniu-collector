package com.sdwu.infrastructure.persistent.repository;

import com.sdwu.domain.sysuser.model.entity.SysRole;
import com.sdwu.domain.sysuser.repository.ISysRoleRepository;
import com.sdwu.infrastructure.persistent.dao.ISysRoleDao;
import com.sdwu.infrastructure.persistent.dao.ISysUserRoleDao;
import com.sdwu.infrastructure.persistent.po.SysRolePO;
import com.sdwu.infrastructure.persistent.po.SysUserRolePO;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class SysRoleRepository implements ISysRoleRepository {

    @Resource
    private ISysRoleDao sysRoleDao;
    @Resource
    private ISysUserRoleDao userRoleDao;

    @Override
    public List<String> findRoleListByUserId(Long userId) {
        // 防止空指针异常
        List<SysUserRolePO> sysUserRoleList = userRoleDao.selectSysUserRoleListByUserId(userId);
        if (sysUserRoleList == null || sysUserRoleList.isEmpty()) {
            return Collections.emptyList();
        }

        // 使用HashSet避免重复角色键
        Set<String> roleSet = new HashSet<>();

        // 尝试批量获取角色信息以减少数据库访问次数
        List<Long> roleIds = sysUserRoleList.stream()
                .map(SysUserRolePO::getRoleId)
                .filter(Objects::nonNull) // 过滤掉null值
                .collect(Collectors.toList());

        List<SysRolePO> sysRoleList = sysRoleDao.findByRoleIds(roleIds); // 假设此方法存在

        // 添加角色键到集合中
        sysRoleList.forEach(sysRole -> roleSet.add(sysRole.getRoleKey()));

        // 转换为List返回
        return new ArrayList<>(roleSet);
    }

    @Override
    public List<SysRole> selectRoleAll() {
        List<SysRolePO> sysRoleList = sysRoleDao.selectList();
        List<SysRole> sysRoles = sysRoleList.stream()
                .map(SysRolePO::convertToDomain)
                .collect(Collectors.toList());
        return sysRoles;
    }
}
