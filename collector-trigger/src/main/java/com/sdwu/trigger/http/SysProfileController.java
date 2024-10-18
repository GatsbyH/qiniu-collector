package com.sdwu.trigger.http;

import cn.dev33.satoken.stp.StpUtil;
import com.sdwu.domain.sysuser.model.entity.SystemUser;
import com.sdwu.domain.sysuser.service.ISysUserService;
import com.sdwu.types.enums.ResponseCode;
import com.sdwu.types.model.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/system/user/profile")
public class SysProfileController {

    @Resource
    private ISysUserService sysUserService;
    @GetMapping
    public Response profile()
    {
        SystemUser user = (SystemUser) StpUtil.getSession().get("user");
        return Response.builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .build()
                .put("user", user)
                .put("roleGroup",sysUserService.selectUserRoleGroup(user.getUserId()));
    }
}
