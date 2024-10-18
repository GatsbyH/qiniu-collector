package com.sdwu.trigger.http;

import com.sdwu.domain.sysuser.model.entity.SysRole;
import com.sdwu.domain.sysuser.model.entity.SystemUser;
import com.sdwu.domain.sysuser.service.ISysRoleService;
import com.sdwu.domain.sysuser.service.ISysUserService;
import com.sdwu.types.enums.ResponseCode;
import com.sdwu.types.model.PageResult;
import com.sdwu.types.model.Response;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/system/user")
public class SysUserController {
    @Resource
    private ISysUserService sysUserService;


    @Resource
    private ISysRoleService sysRoleService;

    @GetMapping("/list")
    public Response list(SystemUser user) {
        PageResult<SystemUser> systemUserPageResult = sysUserService.selcetUserPage(user);
        return Response.success(systemUserPageResult);
    }


    @GetMapping("/")
    public Response getInfo() {
        List<SysRole> sysRoles = sysRoleService.selectRoleAll();
        List<SysRole> sysRoleList = sysRoles.stream().filter(sysRole -> !sysRole.isAdmin())
                .collect(Collectors.toList());
        return Response.builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .build()
                .put("roles", sysRoleList);
    }


    @PostMapping()
    public Response add(@RequestBody SystemUser user) {
        return Response.success(sysUserService.insertUser(user));
    }
}
