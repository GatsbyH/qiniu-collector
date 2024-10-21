package com.sdwu.trigger.http;

import com.sdwu.domain.sysuser.model.entity.SysRole;
import com.sdwu.domain.sysuser.service.ISysRoleService;
import com.sdwu.types.enums.ResponseCode;
import com.sdwu.types.model.Response;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping(value = "/{roleId}")
    public Response getInfo(@PathVariable Long roleId) {
        return Response.success(roleService.selectRoleById(roleId));
    }

    @PutMapping
    public Response edit(@RequestBody SysRole role) {
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
        return Response.success(roleService.updateRole(role));
    }
    @DeleteMapping("/{roleIds}")
    public Response remove(@PathVariable Long[] roleIds) {
        if (!roleService.checkRoleExistUser(roleIds)){
            return Response.builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.ROLE_EXIST_USER.getInfo())
                    .build();
        }
        return Response.success(roleService.deleteRoleByIds(roleIds));
    }

    @PutMapping("/changeStatus")
    public Response changeStatus(@RequestBody SysRole role) {
        return Response.success(roleService.updateRoleStatus(role));
    }


}
