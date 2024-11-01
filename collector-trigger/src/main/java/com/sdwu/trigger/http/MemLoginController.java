package com.sdwu.trigger.http;

import com.sdwu.domain.github.service.IMemLoginService;
import com.sdwu.types.enums.ResponseCode;
import com.sdwu.types.model.Response;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController("/cms")
public class MemLoginController {

    @Resource
    private IMemLoginService memLoginService;
    //测试发送邮箱验证码
    public Response sendEmail()
    {
        memLoginService.sendEmail();
        return Response.builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .data("")
                .build();
    }
}
