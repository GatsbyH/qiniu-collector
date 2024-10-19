package com.sdwu.infrastructure.persistent.repository;

import com.sdwu.domain.sysuser.model.entity.SysRole;
import com.sdwu.domain.sysuser.repository.ISysRoleRepository;
import com.sdwu.infrastructure.persistent.dao.ISysRoleDao;
import com.sdwu.infrastructure.persistent.dao.ISysRoleMenuDao;
import com.sdwu.infrastructure.persistent.dao.ISysUserRoleDao;
import com.sdwu.infrastructure.persistent.po.SysRoleMenuPO;
import com.sdwu.infrastructure.persistent.po.SysRolePO;
import com.sdwu.infrastructure.persistent.po.SysUserRolePO;
import com.sdwu.infrastructure.persistent.utils.LambdaQueryWrapperX;
import com.sdwu.types.model.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class SysRoleRepository implements ISysRoleRepository {

    @Resource
    private ISysRoleDao sysRoleDao;
    @Resource
    private ISysUserRoleDao userRoleDao;


    @Resource
    private ISysRoleMenuDao roleMenuDao;

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

    @Override
    public PageResult<SysRole> selectRolePage(SysRole role) {
        PageResult<SysRolePO> sysRolePageResult = sysRoleDao.selectRolePage(role);
        if (sysRolePageResult==null){
            return PageResult.empty();
        }
        List<SysRole> sysRoles = sysRolePageResult.getList().stream()
                    .map(SysRolePO::convertToDomain)
                    .collect(Collectors.toList());
        return new PageResult<>(sysRoles, sysRolePageResult.getTotal());
    }

    @Override
    public Integer insertRole(SysRole role) {
        int rows=1;
        SysRolePO sysRolePO = SysRolePO.convertToPO(role);
        sysRoleDao.insert(sysRolePO);
        List<SysRoleMenuPO> list = new ArrayList<>();
        for (Long menuId : role.getMenuIds()) {
            SysRoleMenuPO sysRoleMenuPO = new SysRoleMenuPO();
            sysRoleMenuPO.setRoleId(sysRolePO.getRoleId());
            sysRoleMenuPO.setMenuId(menuId);
            list.add(sysRoleMenuPO);
        }
        if (list.size() > 0){
           rows= roleMenuDao.batchRoleMenu(list);
        }
        return rows;
    }

    @Override
    public List<SysRole> findRoleListByRoleName(SysRole role) {
        List<SysRolePO> sysRolePOList = sysRoleDao.findRoleListByRoleName(role.getRoleName());
        List<SysRole> sysRoles = sysRolePOList.stream()
                .map(SysRolePO::convertToDomain)
                .collect(Collectors.toList());
        return sysRoles;
    }

    @Override
    public List<SysRole> findRoleListByRoleKey(SysRole role) {
        List<SysRolePO> sysRolePOList = sysRoleDao.findRoleListByRoleKey(role.getRoleKey());
        List<SysRole> sysRoles = sysRolePOList.stream()
                .map(SysRolePO::convertToDomain)
                .collect(Collectors.toList());
        return sysRoles ;
    }

    @Override
    public SysRole selectRoleById(Long roleId) {
        SysRolePO sysRolePO = sysRoleDao.selectRoleById(roleId);
        return SysRolePO.convertToDomain(sysRolePO);
    }

    @Override
    @Transactional
    public Integer updateRole(SysRole role) {
        if (role.getRoleId()==null|| role.getRoleId()==null){
            return 0;
        }
        int rows=1;
        SysRolePO sysRolePO = SysRolePO.convertToPO(role);
        int i = sysRoleDao.updateById(sysRolePO);
        roleMenuDao.deleteByRoleId(role.getRoleId());
        List<SysRoleMenuPO> list = new ArrayList<>();
        for (Long menuId : role.getMenuIds()) {
            SysRoleMenuPO sysRoleMenuPO = new SysRoleMenuPO();
            sysRoleMenuPO.setRoleId(sysRolePO.getRoleId());
            sysRoleMenuPO.setMenuId(menuId);
            list.add(sysRoleMenuPO);
        }
        if (list.size() > 0){
            rows= roleMenuDao.batchRoleMenu(list);
        }
        return i;
    }

    @Override
    public Integer deleteRoleByIds(Long[] roleIds) {
        roleMenuDao.deleteRoleMenu(roleIds);
        return sysRoleDao.deleteRoleByIds(roleIds);
    }

    @Override
    public boolean checkRoleExistUser(Long[] roleIds) {
        if (userRoleDao.selectCount(new LambdaQueryWrapperX<SysUserRolePO>().eq(SysUserRolePO::getRoleId,roleIds))>0){
            return true;
        }
        return false;
    }


}
