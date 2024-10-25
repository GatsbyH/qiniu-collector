package com.sdwu.trigger.http;

import com.google.code.kaptcha.Producer;
import com.sdwu.domain.sysuser.service.ISysUserService;
import com.sdwu.types.common.Base64;
import com.sdwu.types.enums.ResponseCode;
import com.sdwu.types.model.Response;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

@RestController
public class CaptchaController {

    @Resource(name = "captchaProducerMath")
    private Producer captchaProducerMath;
    @Resource
    private ISysUserService sysUserService;
    @GetMapping("/captchaImage/{visitorId}")
    public Response captchaImage(@PathVariable("visitorId") String visitorId){
        String capStr = null, code = null;
        BufferedImage image = null;
        String capText = captchaProducerMath.createText();
        capStr = capText.substring(0, capText.lastIndexOf("@"));
        code = capText.substring(capText.lastIndexOf("@") + 1);
        image = captchaProducerMath.createImage(capStr);
        sysUserService.setCaptchaCache(visitorId, code);
        // 转换流信息写出
        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        try
        {
            ImageIO.write(image, "jpg", os);
        }
        catch (IOException e)
        {
            return Response.builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
        return Response.builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .build()
                .put("img", Base64.encode(os.toByteArray()));
    }
}
