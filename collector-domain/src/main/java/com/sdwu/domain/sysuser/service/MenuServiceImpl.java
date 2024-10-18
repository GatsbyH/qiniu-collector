package com.sdwu.domain.sysuser.service;

import com.sdwu.domain.sysuser.model.entity.SysMenu;
import com.sdwu.domain.sysuser.model.entity.SystemUser;
import com.sdwu.domain.sysuser.model.entity.TreeSelect;
import com.sdwu.domain.sysuser.model.valobj.RouterVo;
import com.sdwu.domain.sysuser.repository.ISysMenuPermRepository;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MenuServiceImpl implements IMenuService{

    @Resource
    private ISysMenuPermRepository sysMenuPermRepository;
    @Override
    public List<SysMenu> selectMenuTreeByUserId(SystemUser user) {
        return sysMenuPermRepository.selectMenuTreeByUserId(user);
    }

    @Override
    public List<RouterVo> buildMenus(List<SysMenu> menus) {
        return sysMenuPermRepository.buildMenus(menus);
    }

    @Override
    public List<SysMenu> selectMenuListByUserId(Long userId) {
        return sysMenuPermRepository.selectMenuListByUserId(userId);
    }

    @Override
    public List<TreeSelect> buildMenuTreeSelect(List<SysMenu> menus) {
        List<SysMenu> menuTrees = sysMenuPermRepository.buildMenuTree(menus);
        return menuTrees.stream().map(TreeSelect::new).collect(Collectors.toList());
    }


}
