package com.sdwu.trigger.http;

import com.sdwu.domain.sysuser.model.entity.SysRole;
import com.sdwu.domain.sysuser.service.ISysRoleService;
import com.sdwu.types.model.Response;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/system/role")
public class SysRoleController {

    @Resource
    private ISysRoleService roleService;

    @RequestMapping("/list")
    public Response list(SysRole role) {
        return Response.success(roleService.selectRolePage(role));
    }
}
