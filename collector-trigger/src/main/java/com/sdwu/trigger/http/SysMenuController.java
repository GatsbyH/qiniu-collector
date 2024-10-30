package com.sdwu.trigger.http;

import cn.dev33.satoken.stp.StpUtil;
import com.sdwu.domain.sysuser.model.entity.SysMenu;
import com.sdwu.domain.sysuser.model.entity.SystemUser;
import com.sdwu.domain.sysuser.service.IMenuService;
import com.sdwu.types.enums.ResponseCode;
import com.sdwu.types.model.Response;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/system/menu")
public class SysMenuController {

    @Resource
    private IMenuService menuService;

    @GetMapping("/list")
    public Response list(SysMenu menu)
    {
        List<SysMenu> menus = menuService.selectMenuListByUserId(StpUtil.getLoginIdAsLong());
        return Response.success(menus);
    }

    @PostMapping
    public Response add(@RequestBody SysMenu menu)
    {
        if (menuService.checkMenuNameUnique(menu)){
            return Response.builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.MENU_NAME_EXIST.getInfo())
                    .build();
        }
        return Response.success(menuService.insertMenu(menu));
    }

    @GetMapping("/{menuId}")
    public Response getInfo(@PathVariable("menuId") Long menuId)
    {
        return Response.success(menuService.selectMenuById(menuId));
    }


    @DeleteMapping("/{menuId}")
    public Response remove(@PathVariable("menuId") Long menuId)
    {
        if (menuService.hasChildrenByMenuId(menuId)){
            return Response.builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.MENU_HAS_CHILD.getInfo())
                    .build();
        }

        if (menuService.checkMenuExistRole(menuId)){
            return Response.builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.Menu_EXIST_USER.getInfo())
                    .build();
        }
        return Response.success(menuService.deleteMenuById(menuId));
    }

    @PutMapping
    public Response edit(@RequestBody SysMenu menu)
    {
//        if (menuService.checkMenuNameUnique(menu)){
//            return Response.builder()
//                    .code(ResponseCode.UN_ERROR.getCode())
//                    .info(ResponseCode.MENU_NAME_EXIST.getInfo())
//                    .build();
//        }
        if (menu.getParentId().equals(menu.getMenuId())){
            return Response.builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.PARENT_VIEW.getInfo())
                    .build();
        }
        return Response.success(menuService.updateMenu(menu));
    }
    @GetMapping("/treeselect")
    public Response treeselect(SysMenu menu)
    {
        SystemUser user = (SystemUser) StpUtil.getSession().get("user");
        List<SysMenu> menus = menuService.selectMenuListByUserId(user.getUserId());
        return Response.success(menuService.buildMenuTreeSelect(menus));
    }

    @GetMapping(value = "/roleMenuTreeselect/{roleId}")
    public Response roleMenuTreeselect(@PathVariable("roleId") Long roleId)
    {
        List<SysMenu> menus = menuService.selectMenuListByUserId(StpUtil.getLoginIdAsLong());
        return Response.builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .build()
                .put("checkedKeys", menuService.selectMenuListByRoleId(roleId))
                .put("menus", menuService.buildMenuTreeSelect(menus));
    }




}
