package com.sdwu.trigger.http;

import com.sdwu.domain.github.service.IGithubLoginService;
import com.sdwu.types.common.StpUserUtil;
import com.sdwu.types.enums.ResponseCode;
import com.sdwu.types.model.Response;
import com.xkcoding.http.config.HttpConfig;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/oauth")
@Slf4j
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
    public void renderAuth(HttpServletResponse response) {
        try {
            AuthRequest authRequest = getAuthRequest();
            // 生成授权URL并直接重定向
            String authorizeUrl = authRequest.authorize(AuthStateUtils.createState());
            response.sendRedirect(authorizeUrl);
        } catch (Exception e) {
            log.error("GitHub授权重定向失败: {}", e.getMessage(), e);
            try {
                // 发生错误时重定向到错误页面
                response.sendRedirect("/error?message=" + URLEncoder.encode("GitHub授权失败: " + e.getMessage(), "UTF-8"));
            } catch (IOException ex) {
                log.error("重定向到错误页面失败: {}", ex.getMessage(), ex);
            }
        }
    }

    @GetMapping("/callback/github")
    public Response login(@RequestParam("code") String code,
                         @RequestParam(value = "state", required = false) String state) {
        try {
            // 创建回调对象
            AuthCallback callback = AuthCallback.builder()
                    .code(code)
                    .state(state)
                    .build();

            AuthRequest authRequest = getAuthRequest();
            AuthResponse<AuthUser> authResponse = authRequest.login(callback);

            if (authResponse.ok()) {
                AuthUser githubUser = authResponse.getData();
                // 处理GitHub登录，获取用户ID
                Long userId = githubLoginService.handleGithubLogin(githubUser);

                // 使用 StpUserUtil 登录
                StpUserUtil.login(userId);

                // 构建返回数据，增加用户信息
                Map<String, Object> resultData = new HashMap<>();
                resultData.put("token", StpUserUtil.getTokenValue());
                resultData.put("userInfo", githubUser);

                return Response.builder()
                        .code(ResponseCode.SUCCESS.getCode())
                        .info("GitHub登录成功")
                        .data(resultData)
                        .build();
            }

            return Response.builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info("GitHub登录失败：" + authResponse.getMsg())
                    .build();
        } catch (Exception e) {
            log.error("GitHub登录异常", e);
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

    @GetMapping("/github/logout")
    public Response logout() {
        try {
            StpUserUtil.logout();
            return Response.builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info("退出登录成功")
                    .build();
        } catch (Exception e) {
            return Response.builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info("退出登录失败：" + e.getMessage())
                    .build();
        }
    }
}
