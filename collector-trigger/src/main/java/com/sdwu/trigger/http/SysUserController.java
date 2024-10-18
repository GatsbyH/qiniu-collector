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


    @GetMapping(value = { "/", "/{userId}" })
    public Response getInfo(@PathVariable(value = "userId", required = false) Long userId) {

        List<SysRole> sysRoles = sysRoleService.selectRoleAll();
        List<SysRole> sysRoleList = sysRoles.stream().filter(sysRole -> !sysRole.isAdmin())
                .collect(Collectors.toList());
        if (userId != null){
            SystemUser systemUser = sysUserService.selectUserById(userId);
            return Response.builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .build()
                    .put("roles",sysRoleList)
                    .put("user",systemUser)
                    .put("roleIds",systemUser.getRoleIds());
        }
        return Response.builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .build()
                .put("roles", sysRoleList);
    }


    @PostMapping
    public Response add(@RequestBody SystemUser user) {
        if (!sysUserService.checkUserNameUnique(user.getUserName())){
            return Response.builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.USER_NAME_EXIST.getInfo())
                    .build();
        }
        return Response.success(sysUserService.insertUser(user));
    }


    @PutMapping
    public Response edit(@RequestBody SystemUser user) {
        return Response.success(sysUserService.updateUser(user));
    }


    @DeleteMapping("/{userIds}")
    public Response remove(@PathVariable Long[] userIds) {
        return Response.success(sysUserService.deleteUserByIds(userIds));
    }


    @PutMapping("/resetPwd")
    public Response resetPwd(@RequestBody SystemUser user) {
        return Response.success(sysUserService.resetUserPwd(user));
    }


    @PutMapping("/changeStatus")
    public Response changeStatus(@RequestBody SystemUser user) {
        return Response.success(sysUserService.changeStatus(user));
    }
}
