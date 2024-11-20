package com.sdwu.trigger.http;

import com.sdwu.domain.github.service.IGithubLoginService;
import com.sdwu.types.common.StpUserUtil;
import com.sdwu.types.enums.ResponseCode;
import com.sdwu.types.model.Response;
import com.xkcoding.http.config.HttpConfig;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthGithubRequest;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;

@RestController
@RequestMapping("/oauth")
public class MemLoginController {

    @Value("${github.oauth.client-id}")
    private String clientId;

    @Value("${github.oauth.client-secret}")
    private String clientSecret;

    @Value("${github.oauth.redirect-uri}")
    private String redirectUri;

    @Resource
    private IGithubLoginService githubLoginService;

    @GetMapping("/github/render")
    public void renderAuth(HttpServletResponse response) throws IOException {
        AuthRequest authRequest = getAuthRequest();
        response.sendRedirect(authRequest.authorize(AuthStateUtils.createState()));
    }

    @GetMapping("/callback/github")
    public Response login(AuthCallback callback) {
        try {
            AuthRequest authRequest = getAuthRequest();
            AuthResponse<AuthUser> authResponse = authRequest.login(callback);

            if (authResponse.ok()) {
                AuthUser githubUser = authResponse.getData();
                // 处理GitHub登录，获取用户ID
                Long userId = githubLoginService.handleGithubLogin(githubUser);

                // 使用 StpUserUtil 登录（区别于系统用户的 StpUtil）
                StpUserUtil.login(userId);

                return Response.builder()
                        .code(ResponseCode.SUCCESS.getCode())
                        .info("GitHub登录成功")
                        .data(StpUserUtil.getTokenValue())
                        .build();
            }

            return Response.builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info("GitHub登录失败：" + authResponse.getMsg())
                    .build();
        } catch (Exception e) {
            return Response.builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info("GitHub登录异常：" + e.getMessage())
                    .build();
        }
    }

    private AuthRequest getAuthRequest() {
        return new AuthGithubRequest(AuthConfig.builder()
                .clientId(clientId)
                .clientSecret(clientSecret)
                .redirectUri(redirectUri)
                .httpConfig(HttpConfig.builder()
                    .timeout(30000)  // 30秒超时
                    .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 7890)))  // 本地代理配置
                    .build())
                .build());
    }


    @GetMapping("/github/info")
    public Response getInfo() {
        try {
            Long userId = StpUserUtil.getLoginIdAsLong();
            return Response.success(githubLoginService.getGithubUserInfo(userId));
        } catch (Exception e) {
            return Response.builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info("获取用户信息失败：" + e.getMessage())
                    .build();
        }
    }
}
