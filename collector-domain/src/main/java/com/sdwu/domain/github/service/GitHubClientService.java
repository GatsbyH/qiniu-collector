package com.sdwu.domain.github.service;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
public class GitHubClientService {
    private final OkHttpClient okHttpClient;
    private final ConcurrentLinkedQueue<String> tokenQueue;


    @Autowired
    public GitHubClientService(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient.newBuilder()
                .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("localhost", 7890)))
                .build(); // 设置代理服务器
        this.tokenQueue = new ConcurrentLinkedQueue<>();
        // 硬编码 GitHub Tokens
        this.tokenQueue.add("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");
        this.tokenQueue.add("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");
        this.tokenQueue.add("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");
    }

    public String fetchGitHubApi(String endpoint, Object params) throws IOException {
        String token = tokenQueue.poll();
        if (token == null) { // 如果没有可用的 token，从队列末尾重新开始
            token = tokenQueue.poll();
        }
        Request request = new Request.Builder()
                .url("https://api.github.com" + endpoint)
                .header("Authorization", token)
                .build();

        try (Response response = okHttpClient.newCall(request).execute()) {
//            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            // 检查是否需要轮换 token，例如如果遇到 403 错误
            if (response.code() == 403) {
                // 将 token 标记为无效，并重新尝试
                // 这里可以添加逻辑来更新 tokenQueue，移除无效的 token
                return fetchGitHubApi(endpoint, params); // 递归调用
            }

            return response.body().string();
        }
    }
}
