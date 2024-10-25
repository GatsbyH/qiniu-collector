package com.sdwu.domain.sysuser.repository;

import com.sdwu.domain.sysuser.model.entity.SysMenu;
import com.sdwu.domain.sysuser.model.entity.SystemUser;
import com.sdwu.domain.sysuser.model.valobj.RouterVo;

import java.util.List;

public interface ISysMenuPermRepository {
    List<String> findMenuPermListByUserId(Long userId);

    List<SysMenu> selectMenuTreeByUserId(SystemUser user);

    List<RouterVo> buildMenus(List<SysMenu> menus);

    List<SysMenu> selectMenuListByUserId(Long userId);

    List<SysMenu>  buildMenuTree(List<SysMenu> menus);

    List<Long> selectMenuListByRoleId(Long roleId);

    boolean checkMenuNameUnique(SysMenu menu);

    int insertMenu(SysMenu menu);

    int updateMenu(SysMenu menu);

    SysMenu selectMenuById(Long menuId);

    boolean hasChildrenByMenuId(Long menuId);

    boolean checkMenuExistRole(Long menuId);

    int deleteMenuById(Long menuId);
}
