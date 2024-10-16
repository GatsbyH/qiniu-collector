package com.sdwu.trigger.http;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.sdwu.domain.sysuser.model.entity.SysMenu;
import com.sdwu.domain.sysuser.model.entity.SystemUser;
import com.sdwu.domain.sysuser.model.valobj.LoginRequest;
import com.sdwu.domain.sysuser.repository.ISystemUserRepository;
import com.sdwu.domain.sysuser.service.IMenuService;
import com.sdwu.types.enums.ResponseCode;
import com.sdwu.types.exception.AppException;
import com.sdwu.types.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/")
public class LoginController {

    @Resource
    private ISystemUserRepository systemUserRepository;

    @Resource
    private IMenuService menuService;

    @PostMapping("login")
    public Response login(@RequestBody LoginRequest loginRequest){
        SystemUser systemUser = null;
        try {
            systemUser = systemUserRepository.findByUserName(loginRequest.getUsername());
        } catch (Exception e) {
            return Response.fail(ResponseCode.USER_NOT_EXIST.getInfo());
        }
        if (systemUser == null)
            return Response.success(ResponseCode.USER_NOT_EXIST.getInfo());
        if (!systemUser.getPassword().equals(loginRequest.getPassword()))
            return Response.fail(ResponseCode.Password_ERROR.getCode());
        StpUtil.login(systemUser.getUserId());

        StpUtil.getSession().set("user", systemUser);

        String tokenValue = StpUtil.getTokenInfo().getTokenValue();
        List<String> roleList = StpUtil.getRoleList();
        List<String> permissionList = StpUtil.getPermissionList();

        log.info("tokenValue:{},roleList:{},permissionList:{}",tokenValue,roleList,permissionList);

        return Response.builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .data(tokenValue)
                .build();
    }


    @GetMapping("getInfo")
    public Response getInfo()
    {
        return Response.<HashMap<String, Object>>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .build()
                .put("permissions", StpUtil.getPermissionList())  // 添加用户信息
                .put("roles",  StpUtil.getRoleList())
                .put("user", StpUtil.getSession().get("user"));
    }

    @PostMapping("logout")
    public Response logout(){
        StpUtil.logout();
        return Response.success("注销");
    }


    /**
     * 获取路由信息
     *
     * @return 路由信息
     */
    @GetMapping("getRouters")
    public Response getRouters()
    {
        SystemUser user = (SystemUser) StpUtil.getSession().get("user");
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(user);
        return Response.success(menuService.buildMenus(menus));
    }

}
