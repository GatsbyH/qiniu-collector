package com.sdwu.domain.github.service;//package com.sdwu.domain.github.service;
//
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.Response;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import java.io.IOException;
//import java.net.InetSocketAddress;
//import java.net.Proxy;
//import java.util.concurrent.ConcurrentLinkedQueue;
//import java.util.concurrent.locks.ReentrantLock;
//
//@Service
//public class GitHubClientService {
//    private final OkHttpClient okHttpClient;
//    private final ConcurrentLinkedQueue<String> tokenQueue;
//
//
//    @Autowired
//    public GitHubClientService(OkHttpClient okHttpClient) {
//        this.okHttpClient = okHttpClient.newBuilder()
//                .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("localhost", 7890)))
//                .build(); // 设置代理服务器
//        this.tokenQueue = new ConcurrentLinkedQueue<>();
//        // 硬编码 GitHub Tokens
//        this.tokenQueue.add("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");
//        this.tokenQueue.add("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");
//        this.tokenQueue.add("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");
//        this.tokenQueue.add("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");
//        this.tokenQueue.add("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");
//        this.tokenQueue.add("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");
//        this.tokenQueue.add("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");
//    }
//
//    public String fetchGitHubApi(String endpoint, Object params) throws IOException {
////        String token = tokenQueue.poll();
//        String token = "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";
////        if (token == null) { // 如果没有可用的 token，从队列末尾重新开始
////            token = tokenQueue.poll();
////        }
//        Request request = new Request.Builder()
//                .url("https://api.github.com" + endpoint)
//                .header("Authorization", token)
//                .build();
//
//        try (Response response = okHttpClient.newCall(request).execute()) {
////            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
//
//            // 检查是否需要轮换 token，例如如果遇到 403 错误
////            if (response.code() == 403) {
////                // 将 token 标记为无效，并重新尝试
////                // 这里可以添加逻辑来更新 tokenQueue，移除无效的 token
////                return fetchGitHubApi(endpoint, params); // 递归调用
////            }
//
//            return response.body().string();
//        }
//    }
//}


//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.Response;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import java.io.IOException;
//import java.net.InetSocketAddress;
//import java.net.Proxy;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.locks.ReentrantLock;
//
//@Service
//public class GitHubClientService {
//    private final OkHttpClient okHttpClient;
//    private final ConcurrentHashMap<String, Boolean> tokenMap;
//    private final ReentrantLock lock = new ReentrantLock();
//
//    @Autowired
//    public GitHubClientService(OkHttpClient okHttpClient) {
//        this.okHttpClient = okHttpClient.newBuilder()
//                .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("localhost", 7890)))
//                .build(); // 设置代理服务器
//        this.tokenMap = new ConcurrentHashMap<>();
//        // 硬编码 GitHub Tokens
//        initializeTokens();
//    }
//
//    private void initializeTokens() {
//        tokenMap.put("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", true);
//        tokenMap.put("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", true);
//        tokenMap.put("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", true);
//        tokenMap.put("ghp_VlTDQKohyxJ5HR3vPb5sDuxwBr5ZzZAuhkVh", true);
//        tokenMap.put("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", true);
//    }
//
//    public String fetchGitHubApi(String endpoint, Object params) throws IOException {
//        String token;
//        do {
//            token = getNextValidToken();
//            if (token == null) {
//                throw new IOException("No valid tokens available");
//            }
//            Request request = new Request.Builder()
//                    .url("https://api.github.com" + endpoint)
//                    .header("Authorization", token)
//                    .header("Authorization", "Bearer " + token) // 使用 Bearer 方式传递令牌
//                    .header("X-GitHub-Api-Version", "2022-11-28") // 设置 API 版本`
//                    .build();
//
//            try (Response response = okHttpClient.newCall(request).execute()) {
//                if (response.code() == 401) {
//                    // Token is invalid, mark it as invalid in the map
//                    lock.lock();
//                    try {
//                        tokenMap.put(token, false);
//                    } finally {
//                        lock.unlock();
//                    }
//                    continue;
//                }
//                if (!response.isSuccessful()) {
//                    throw new IOException("Unexpected code " + response);
//                }
//                return response.body().string();
//            }
//        } while (true);
//    }
//
//    private String getNextValidToken() {
//        for (String token : tokenMap.keySet()) {
//            if (tokenMap.get(token)) {
//                return token;
//            }
//        }
//        return null;
//    }
//}
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.Response;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.io.IOException;
//import java.net.InetSocketAddress;
//import java.net.Proxy;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.TimeUnit;
//import java.util.concurrent.locks.ReentrantLock;
//@Service
//public class GitHubClientService {
//    private final OkHttpClient okHttpClient;
//    private final ConcurrentHashMap<String, Boolean> tokenMap;
//    private final ReentrantLock lock = new ReentrantLock();
//
//    @Autowired
//    public GitHubClientService(OkHttpClient okHttpClient) {
//        this.okHttpClient = okHttpClient.newBuilder()
//                .connectTimeout(10, TimeUnit.SECONDS)
//                .readTimeout(30, TimeUnit.SECONDS)
//                .writeTimeout(30, TimeUnit.SECONDS)
//                .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("localhost", 7890))) // 确保代理设置正确
//                .build();
//        this.tokenMap = new ConcurrentHashMap<>();
//        initializeTokens();
//    }
//
//    private void initializeTokens() {
//        tokenMap.put("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", true);
//        tokenMap.put("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", true);
//        tokenMap.put("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", true);
//        tokenMap.put("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", true);
//    }
//
//    public String fetchGitHubApi(String endpoint, Object params) throws IOException {
//        String token;
//        while (true) {
//            token = getNextValidToken();
//            if (token == null) {
//                throw new IOException("No valid tokens available");
//            }
//            Request request = new Request.Builder()
//                    .url("https://api.github.com" + endpoint)
//                    .header("Authorization", "Bearer " + token) // 使用 Bearer 方式传递令牌
//                    .header("X-GitHub-Api-Version", "2022-11-28") // 设置 API 版本`
//                    .build();
//
//            try (Response response = okHttpClient.newCall(request).execute()) {
//                if (response.code() == 401 || response.code() == 403) {
//                    // Token 无效或权限不足，标记为无效并重试
//                    markTokenInvalid(token);
//                    continue;
//                }
//                if (!response.isSuccessful()) {
//                    throw new IOException("Unexpected code " + response);
//                }
//                return response.body().string();
//            }
//        }
//    }
//
//    private String getNextValidToken() {
//        return tokenMap.keySet().stream()
//                .filter(tokenMap::get) // 过滤出有效的令牌
//                .findFirst()
//                .orElse(null); // 如果没有可用的令牌，返回 null
//    }
//
//
//
//    private void markTokenInvalid(String token) {
//        lock.lock();
//        try {
////            tokenMap.put(token, false);
//            tokenMap.remove(token);
//        } finally {
//            lock.unlock();
//        }
//        try {
//            Thread.sleep(1000); // Wait 1 second before retrying
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//        }
//    }
//
//}
//import com.sdwu.domain.github.repository.IScheduledTaskRepository;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.Response;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import javax.annotation.Resource;
//import java.io.IOException;
//import java.net.InetSocketAddress;
//import java.net.Proxy;
//import java.util.concurrent.TimeUnit;
//
//@Service
//public class GitHubClientService {
//    private final OkHttpClient okHttpClient;
//    private final String token; // Store a single token
//    @Resource
//    private IScheduledTaskRepository scheduledTaskRepository;
//    @Autowired
//    public GitHubClientService(OkHttpClient okHttpClient) {
//        this.okHttpClient = okHttpClient.newBuilder()
//                .connectTimeout(10, TimeUnit.SECONDS)
//                .readTimeout(30, TimeUnit.SECONDS)
//                .writeTimeout(30, TimeUnit.SECONDS)
//                .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("localhost", 7890))) // Ensure proxy is set correctly
//                .build();
//        this.token = "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"; // Use a single token
//    }
//
//    public String fetchGitHubApi(String endpoint, Object params) throws IOException {
//        Request request = new Request.Builder()
//                .url("https://api.github.com" + endpoint)
//                .header("Authorization", "Bearer " + token) // Use Bearer token
//                .header("X-GitHub-Api-Version", "2022-11-28") // Set API version
//                .build();
//
//        try (Response response = okHttpClient.newCall(request).execute()) {
//            if (response.code() == 401 || response.code() == 403) {
//                throw new IOException("Token is invalid or permissions are insufficient");
//            }
//            if (!response.isSuccessful()) {
//                throw new IOException("Unexpected code " + response);
//            }
//            return response.body().string();
//        }
//    }
//}
//import com.google.gson.Gson;
//import okhttp3.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.io.IOException;
//import java.net.InetSocketAddress;
//import java.net.Proxy;
//import java.util.concurrent.TimeUnit;

//@Service
//public class GitHubClientService {
//    private final OkHttpClient okHttpClient;
//    private final String token;
//    private static final int MAX_RETRIES = 3;
//    private static final long RETRY_DELAY_MS = 2000;
//
//    @Autowired
//    public GitHubClientService(OkHttpClient okHttpClient) {
//        this.okHttpClient = okHttpClient.newBuilder()
//                .connectTimeout(10, TimeUnit.SECONDS)
//                .readTimeout(30, TimeUnit.SECONDS)
//                .writeTimeout(30, TimeUnit.SECONDS)
//                .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("localhost", 7890))) // Ensure proxy is set correctly
//                .build();
//        this.token = "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";
////        this.token = "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";
//    }
//
//    public String fetchGitHubApi(String endpoint, Object params) throws IOException {
//        int retries = 0;
//        while (retries < MAX_RETRIES) {
//            Request request = new Request.Builder()
//                    .url("https://api.github.com" + endpoint)
//                    .header("Authorization", "Bearer " + token)
//                    .header("X-GitHub-Api-Version", "2022-11-28")
//                    .build();
//
//            try (Response response = okHttpClient.newCall(request).execute()) {
//                if (response.code() == 401 || response.code() == 403) {
//                    throw new IOException("Token失效，已超过Github规定请求次数");
//                }
//                if (!response.isSuccessful()) {
//                    if (retries < MAX_RETRIES - 1) {
//                        // Wait before retrying
//                        try {
//                            Thread.sleep(RETRY_DELAY_MS);
//                        } catch (InterruptedException e) {
//                            Thread.currentThread().interrupt();
//                            throw new IOException("重试已中断", e);
//                        }
//                        retries++;
//                        continue;
//                    }
//                    throw new IOException("Unexpected code " + response);
//                }
//                return response.body().string();
//            } catch (IOException e) {
//                if (retries < MAX_RETRIES - 1) {
//                    try {
//                        Thread.sleep(RETRY_DELAY_MS);
//                    } catch (InterruptedException e1) {
//                        Thread.currentThread().interrupt();
//                        throw new IOException("重试已中断", e1);
//                    }
//                    retries++;
//                    continue;
//                }
//                throw e;
//            }
//        }
//        throw new IOException("GitHub API 请求达到的最大重试次数");
//    }
//
//
//}
import com.google.gson.Gson;
import com.sdwu.types.enums.ResponseCode;
import com.sdwu.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.concurrent.TimeUnit;
@Service
@Slf4j
public class GitHubClientService {
    private final OkHttpClient okHttpClient;
    private final String token;
    private static final int MAX_RETRIES = 10;
    private static final long RETRY_DELAY_MS = 2000;

    @Autowired
    public GitHubClientService(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient.newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("localhost", 7890))) // Ensure proxy is set correctly
                .build();

//        this.token = "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";
        this.token = "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";
//        this.token = "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";
        // this.token = "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";
    }


    public String fetchGitHubApi(String endpoint, Object params) throws IOException {
        if (endpoint.startsWith("/graphql")) {
            return fetchGraphQLApi(endpoint, params);
        } else {
            return fetchRestApi(endpoint, params);
        }
    }

    private String fetchRestApi(String endpoint, Object params) throws IOException {
        int retries = 0;
        while (retries < MAX_RETRIES) {
            Request request = new Request.Builder()
                    .url("https://api.github.com" + endpoint)
                    .header("Authorization", "Bearer " + token)
                    .header("X-GitHub-Api-Version", "2022-11-28")
                    .build();

            try (Response response = okHttpClient.newCall(request).execute()) {
                if (response.code() == 401 || response.code() == 403) {
                    throw new AppException(ResponseCode.GITHUB_API_ERROR.getCode(),ResponseCode.GITHUB_API_ERROR.getInfo());
                }
                if (!response.isSuccessful()) {
                    handleRetries(retries);
                    retries++;
                    continue;
                }
                return response.body().string();
            } catch (IOException e) {
                handleRetries(retries);
                retries++;
            }
        }
        throw new AppException(ResponseCode.GITHUB_API_ERROR.getCode(),ResponseCode.GITHUB_API_ERROR.getInfo());
    }

    private String fetchGraphQLApi(String endpoint, Object params) throws IOException {
        int retries = 0;
        while (retries < MAX_RETRIES) {
            RequestBody body = RequestBody.create(
                    MediaType.get("application/json"),
                    new Gson().toJson(params)
            );

            Request request = new Request.Builder()
                    .url("https://api.github.com" + endpoint)
                    .header("Authorization", "Bearer " + token)
                    .header("Content-Type", "application/json")
                    .header("X-GitHub-Api-Version", "2022-11-28")
                    .post(body)
                    .build();

            try (Response response = okHttpClient.newCall(request).execute()) {
                if (response.code() == 401 || response.code() == 403) {
                    throw new AppException(ResponseCode.GITHUB_API_ERROR.getCode(),ResponseCode.GITHUB_API_ERROR.getInfo());
                }
                if (!response.isSuccessful()) {
                    handleRetries(retries);
                    retries++;
                    continue;
                }
                return response.body().string();
            } catch (IOException e) {
                handleRetries(retries);
                retries++;
            }
        }
        throw new AppException(ResponseCode.GITHUB_API_ERROR.getCode(),ResponseCode.GITHUB_API_ERROR.getInfo());
    }

//    private void handleRetries(int retries) throws IOException {
//        if (retries < MAX_RETRIES - 1) {
//            try {
//                Thread.sleep(RETRY_DELAY_MS);
//            } catch (InterruptedException e1) {
//                Thread.currentThread().interrupt();
//                throw new IOException("重试已中断", e1);
//            }
//        }
//    }
    private void handleRetries(int retries){
        if (retries < MAX_RETRIES - 1) {
            try {
                // 使用指数退避策略
                long delay = RETRY_DELAY_MS * (long) Math.pow(2, retries);
                log.info("第{}次重试，等待时间：{}ms", retries + 1, delay);
                Thread.sleep(delay);
            } catch (InterruptedException e1) {
                Thread.currentThread().interrupt();
                log.warn("重试等待被中断，当前重试次数: {}", retries);
            }
        }
    }
}
