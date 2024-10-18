package com.sdwu.trigger.http;

import com.sdwu.domain.sysuser.model.entity.SysRole;
import com.sdwu.domain.sysuser.service.ISysRoleService;
import com.sdwu.types.enums.ResponseCode;
import com.sdwu.types.model.Response;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @PostMapping
    public Response add(@RequestBody SysRole role) {
        if (!roleService.checkRoleNameUnique(role)){
            return Response.builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.ROLE_NAME_EXIST.getInfo())
                    .build();
        }
        if (!roleService.checkRoleKeyUnique(role)){
            return Response.builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.ROLE_KEY_EXIST.getInfo())
                    .build();
        }
        return Response.success(roleService.insertRole(role));
    }
}
