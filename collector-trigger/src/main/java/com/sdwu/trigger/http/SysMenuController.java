package com.sdwu.trigger.http;

import cn.dev33.satoken.stp.StpUtil;
import com.sdwu.domain.sysuser.model.entity.SysMenu;
import com.sdwu.domain.sysuser.model.entity.SystemUser;
import com.sdwu.domain.sysuser.service.IMenuService;
import com.sdwu.types.model.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/system/menu")
public class SysMenuController {

    @Resource
    private IMenuService menuService;

    @GetMapping("/treeselect")
    public Response treeselect(SysMenu menu)
    {
        SystemUser user = (SystemUser) StpUtil.getSession().get("user");
        List<SysMenu> menus = menuService.selectMenuListByUserId(user.getUserId());
        return Response.success(menuService.buildMenuTreeSelect(menus));
    }



}
