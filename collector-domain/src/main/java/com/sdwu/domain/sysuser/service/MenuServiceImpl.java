package com.sdwu.domain.sysuser.service;

import com.sdwu.domain.sysuser.model.entity.SysMenu;
import com.sdwu.domain.sysuser.model.entity.SystemUser;
import com.sdwu.domain.sysuser.model.valobj.RouterVo;
import com.sdwu.domain.sysuser.repository.ISysMenuPermRepository;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
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
}
