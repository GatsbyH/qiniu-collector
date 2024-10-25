package com.sdwu.trigger.http;

import cn.dev33.satoken.stp.StpUtil;
import com.sdwu.domain.sysuser.model.entity.SysMenu;
import com.sdwu.domain.sysuser.model.entity.SystemUser;
import com.sdwu.domain.sysuser.model.valobj.LoginRequest;
import com.sdwu.domain.sysuser.repository.ISystemUserRepository;
import com.sdwu.domain.sysuser.service.IMenuService;
import com.sdwu.types.annotation.AccessInterceptor;
import com.sdwu.types.enums.ResponseCode;
import com.sdwu.types.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/")
public class LoginController {

    @Resource
    private ISystemUserRepository systemUserRepository;

    @Resource
    private IMenuService menuService;

    @PostMapping("login")
    @AccessInterceptor(key = "uuid", fallbackMethod = "loginErr", permitsPerSecond = 1.0d, blacklistCount = 10)
    public Response login(@RequestBody LoginRequest loginRequest){

        boolean b= systemUserRepository.validateCaptcha(loginRequest.getUsername(), loginRequest.getCode(), loginRequest.getUuid());
        if (!b){
            return Response.builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.CAPTCHA_ERROR.getInfo())
                    .build();
        }

        SystemUser systemUser = null;
        try {
            systemUser = systemUserRepository.findByUserName(loginRequest.getUsername());
        } catch (Exception e) {
            return Response.builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.USER_NOT_EXIST.getInfo())
                    .build();
        }
        if (systemUser == null){
            return Response.builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.USER_NOT_EXIST.getInfo())
                    .build();        }

        if (!systemUser.getPassword().equals(loginRequest.getPassword())){
            return Response.fail(ResponseCode.Password_ERROR.getInfo());
        }


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

    public Response loginErr(@RequestBody LoginRequest loginRequest) {
        return Response.builder()
                .code(ResponseCode.UN_ERROR.getCode())
                .info("请求过于频繁，请稍后再试")
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
