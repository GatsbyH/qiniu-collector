package com.sdwu.trigger.http;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.sdwu.domain.sysuser.model.entity.SystemUser;
import com.sdwu.domain.sysuser.model.valobj.LoginRequest;
import com.sdwu.domain.sysuser.repository.ISystemUserRepository;
import com.sdwu.types.enums.ResponseCode;
import com.sdwu.types.exception.AppException;
import com.sdwu.types.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("api/v1/")
public class LoginController {

    @Resource
    private ISystemUserRepository systemUserRepository;

    @PostMapping("login")
    public Response login(@RequestBody LoginRequest loginRequest){
        SystemUser systemUser = null;
        try {
            systemUser = systemUserRepository.findByUserName(loginRequest.getUsername());
        } catch (Exception e) {
            return Response.fail(ResponseCode.USER_NOT_EXIST.getInfo());
        }
        if (systemUser == null)
            return Response.fail(ResponseCode.USER_NOT_EXIST.getInfo());
        if (!systemUser.getPassword().equals(loginRequest.getPassword()))
            return Response.fail(ResponseCode.UN_ERROR.getCode());
        StpUtil.login(systemUser.getUserId());
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
        return Response.builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .data(tokenInfo)
                .build();
    }
}
