package com.sdwu.domain.github.service;

import cn.hutool.core.util.URLUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.sdwu.domain.github.model.entity.Developer;
import com.sdwu.domain.github.model.valobj.GithubRepoReqVo;
import com.sdwu.domain.github.model.valobj.GithubRepoRespVo;
import com.sdwu.domain.github.model.valobj.GithubUserReqVo;
import com.sdwu.domain.github.model.valobj.GithubUserRespVo;
import com.sdwu.domain.github.repository.IGithubUserRepository;
import com.sdwu.domain.github.repository.IScheduledTaskRepository;
import com.sdwu.types.enums.ResponseCode;
import com.sdwu.types.exception.AppException;
import com.sdwu.types.model.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class GitHubApiImpl implements IGitHubApi{

    @Resource
    private GitHubClientService gitHubClientService;
    @Resource
    private IGithubUserRepository githubUserRepository;
    @Resource
    private IScheduledTaskRepository scheduledTaskRepository;
//    @Override
//    public String getReposByUserName(String username) throws IOException {
//        String fetchGitHubApi = gitHubClientService.fetchGitHubApi("/users/" + username + "/repos", null);
//        return fetchGitHubApi;
//    }
//
//    @Override
//    public int getUserCommits(String owner, String repo, String username) throws IOException {
//        String fetchGitHubApi = gitHubClientService.fetchGitHubApi("/repos/" + owner + "/" + repo + "/commits?author=" + username, null);
//        JSONArray jsonArray = JSON.parseArray(fetchGitHubApi);
//        return jsonArray.size();
//    }
//
//    @Override
//    public int getUserPullRequests(String owner, String repo, String username) throws IOException {
//        String fetchGitHubApi = gitHubClientService.fetchGitHubApi("/repos/" + owner + "/" + repo + "/pulls?state=all&author=" + username, null);
//        JSONArray jsonArray = JSON.parseArray(fetchGitHubApi);
//        return jsonArray.size();
//    }
//
//    @Override
//    public int getUserIssues(String owner, String repo, String username) throws IOException {
//        String fetchGitHubApi = gitHubClientService.fetchGitHubApi("/repos/" + owner + "/" + repo + "/issues?creator=" + username, null);
//        JSONArray jsonArray = JSON.parseArray(fetchGitHubApi);
//        return jsonArray.size();
//    }
//
//    @Override
//    public String getUserContributions(String owner, String repo, String username) throws IOException {
//        String fetchGitHubApi = gitHubClientService.fetchGitHubApi("/repos/" + owner + "/" + repo + "/contributors", null);
//        return fetchGitHubApi;
//    }
//
//    @Override
//    public JSONObject getUserInfo(String username) throws IOException {
//        String fetchGitHubApi = gitHubClientService.fetchGitHubApi("/users/" + username, null);
//        JSONObject jsonObject = JSONObject.parseObject(fetchGitHubApi);
//        return jsonObject;
//    }
//
//    @Override
//    public List<String> getFollowersByUserName(String username) throws IOException {
//        String fetchGitHubApi = gitHubClientService.fetchGitHubApi("/users/" + username + "/followers", null);
//        JSONArray jsonArray = JSON.parseArray(fetchGitHubApi);
//        List<String> locations = new ArrayList<>();
//        for (Object o : jsonArray) {
//            JSONObject jsonObject = (JSONObject) o;
//            if (jsonObject == null&&jsonObject.getString("location")==null){
//                continue;
//            }
//            JSONObject userInfo = this.getUserInfo(jsonObject.getString("login"));
//            locations.add(userInfo.getString("location"));
//        }
//        return locations;
//    }
//
//    @Override
//    public List<String> getFollowingByUserName(String username) throws IOException {
//        String fetchGitHubApi = gitHubClientService.fetchGitHubApi("/users/" + username + "/following", null);
//        JSONArray jsonArray = JSON.parseArray(fetchGitHubApi);
//        List<String> locations = new ArrayList<>();
//        for (Object o : jsonArray) {
//            JSONObject jsonObject = (JSONObject) o;
//            if (jsonObject == null&&jsonObject.getString("location")==null){
//                continue;
//            }
//            JSONObject userInfo = this.getUserInfo(jsonObject.getString("login"));
//            locations.add(userInfo.getString("location"));
//        }
//        return locations;
//    }

    @Override
    public String getReposByUserName(String username) throws IOException {
        try {
            String fetchGitHubApi = gitHubClientService.fetchGitHubApi("/users/" + username + "/repos", null);
            return fetchGitHubApi;
        } catch (IOException e) {
            log.error("获取用户{}的仓库信息时发生错误: {}", username, e.getMessage());
            throw e;
        }
    }

    @Override
    public int getUserCommits(String owner, String repo, String username) throws IOException {
        try {
            String fetchGitHubApi = gitHubClientService.fetchGitHubApi("/repos/" + owner + "/" + repo + "/commits?author=" + username, null);
            JSONArray jsonArray = JSON.parseArray(fetchGitHubApi);
            return jsonArray.size();
        } catch (IOException e) {
            log.error("获取用户{}的Commits数时发生错误: {}", username, e.getMessage());
            throw e;
        }
    }

    @Override
    public int getUserPullRequests(String owner, String repo, String username) throws IOException {
        try {
            String fetchGitHubApi = gitHubClientService.fetchGitHubApi("/repos/" + owner + "/" + repo + "/pulls?state=all&author=" + username, null);
            JSONArray jsonArray = JSON.parseArray(fetchGitHubApi);
            return jsonArray.size();
        } catch (IOException e) {
            log.error("获取用户{}的拉取请求数时发生错误: {}", username, e.getMessage());
            throw e;
        }
    }

    @Override
    public int getUserIssues(String owner, String repo, String username) throws IOException {
        try {
            String fetchGitHubApi = gitHubClientService.fetchGitHubApi("/repos/" + owner + "/" + repo + "/issues?creator=" + username, null);
            JSONArray jsonArray = JSON.parseArray(fetchGitHubApi);
            return jsonArray.size();
        } catch (IOException e) {
            log.error("获取用户{}的Issues报告数时发生错误: {}", username, e.getMessage());
            throw e;
        }
    }

    @Override
    public String getUserContributions(String owner, String repo, String username) throws IOException {
        try {
            String fetchGitHubApi = gitHubClientService.fetchGitHubApi("/repos/" + owner + "/" + repo + "/contributors", null);
            return fetchGitHubApi;
        } catch (IOException e) {
            log.error("获取用户{}的贡献数值时发生错误: {}", username, e.getMessage());
            throw e;
        }
    }

    @Override
    public JSONObject getUserInfo(String username) throws IOException {
        try {
            String fetchGitHubApi = gitHubClientService.fetchGitHubApi("/users/" + username, null);
            JSONObject jsonObject = JSONObject.parseObject(fetchGitHubApi);
            return jsonObject;
        } catch (IOException e) {
            log.error("获取用户{}的信息时发生错误: {}", username, e.getMessage());
            throw e;
        }
    }

    @Override
    public List<String> getFollowersByUserName(String username) throws IOException {
        try {
            String fetchGitHubApi = gitHubClientService.fetchGitHubApi("/users/" + username + "/followers", null);
            JSONArray jsonArray = JSON.parseArray(fetchGitHubApi);
            List<String> locations = new ArrayList<>();
            for (Object o : jsonArray) {
                JSONObject jsonObject = (JSONObject) o;
                if (jsonObject != null && jsonObject.getString("location") != null) {
                    JSONObject userInfo = this.getUserInfo(jsonObject.getString("login"));
                    locations.add(userInfo.getString("location"));
                }
            }
            return locations;
        } catch (IOException e) {
            log.error("获取用户{}的关注者时发生错误: {}", username, e.getMessage());
            throw e;
        }
    }

    @Override
    public List<String> getFollowingByUserName(String username) throws IOException {
        try {
            String fetchGitHubApi = gitHubClientService.fetchGitHubApi("/users/" + username + "/following", null);
            JSONArray jsonArray = JSON.parseArray(fetchGitHubApi);
            List<String> locations = new ArrayList<>();
            for (Object o : jsonArray) {
                JSONObject jsonObject = (JSONObject) o;
                if (jsonObject != null && jsonObject.getString("location") != null) {
                    JSONObject userInfo = this.getUserInfo(jsonObject.getString("login"));
                    locations.add(userInfo.getString("location"));
                }
            }
            return locations;
        } catch (IOException e) {
            log.error("获取用户{}正在关注的用户时发生错误: {}", username, e.getMessage());
            throw e;
        }
    }

    @Override
    public String getDeveloperByFieldAndNation(String field,String nation) throws IOException {
        String fetchGitHubApi = null;
        try {
            fetchGitHubApi = gitHubClientService.fetchGitHubApi("/search/repositories?q=language:" + field +"&sort=stars&order=desc", null);
        } catch (IOException e) {
            scheduledTaskRepository.updateScheduledTask(field,"FAILED",e.getMessage());
            throw new RuntimeException(e);
        }
        return fetchGitHubApi;
    }

    @Override
    public String getDevelopersByFields(String field) throws IOException {
        Integer page = githubUserRepository.getGitHubPageByField(field);
//        Integer page = 1;
        Integer per_page = 4;
        try {
            String fetchGitHubApi = gitHubClientService.fetchGitHubApi("/search/repositories?q=" + field + "+in:description,topics&sort=stars" + "&page=" + page + "&per_page=" + per_page, null);


            if (fetchGitHubApi == null || fetchGitHubApi.isEmpty()) {
                log.error("收到 field 的空响应: {}", field);
                throw new IOException("收到来自 GitHub API 的空响应");
            }


            JSONObject responseObject = JSON.parseObject(fetchGitHubApi);
            if (responseObject.getInteger("total_count") == 0) {
                log.warn("未找到 field 的存储库: {}", field);
            }


            githubUserRepository.updateGitHubPageByField(field);

            return fetchGitHubApi;

        } catch (IOException e) {
            scheduledTaskRepository.updateScheduledTask(field,"FAILED",e.getMessage());
            log.error("从 GitHub API 获取字段的开发人员时出错: {} - {}", field, e.getMessage());
            throw e;
        } catch (Exception e) {
            scheduledTaskRepository.updateScheduledTask(field,"FAILED",e.getMessage());
            log.error("为字段获取开发人员时发生意外错误: {} - {}", field, e.getMessage());
            throw new IOException("发生意外错误", e);
        }
    }

    @Override
    public PageResult<GithubUserRespVo> getGithubDevelopers(GithubUserReqVo githubUserReqVo) {
        String response = null;
        // 对用户名进行 URL 编码
        String encodedUsername = URLUtil.encode(githubUserReqVo.getUsername());
        String endpoint = String.format("/search/users?q=%s in:login&page=%d&per_page=%d",
            encodedUsername, githubUserReqVo.getPageNum(), githubUserReqVo.getPageSize());

        try {
            // 1. API调用
            response = gitHubClientService.fetchGitHubApi(endpoint, null);
            if (response == null || response.isEmpty()) {
                log.warn("API返回空响应，查询参数: {}", githubUserReqVo);
                return PageResult.empty();
            }

            // 2. 解析响应
            JSONObject jsonObject = JSON.parseObject(response);
            Integer totalCount = jsonObject.getIntValue("total_count");
            JSONArray items = jsonObject.getJSONArray("items");
            if (items == null || items.isEmpty()) {
                log.warn("未找到匹配的用户，查询参数: {}", githubUserReqVo);
                return PageResult.empty();
            }
            PageResult<GithubUserRespVo> githubUserRespVoPageResult = new PageResult<>();
            githubUserRespVoPageResult.setTotal(Long.valueOf(totalCount));
            List<GithubUserRespVo> githubUserRespVos = items.stream()
                    .map(item -> {
                        JSONObject userJson = (JSONObject) item;
                        return GithubUserRespVo.builder()
                                .login(userJson.getString("login"))
                                .avatarUrl(userJson.getString("avatar_url"))
                                .htmlUrl(userJson.getString("html_url"))
                                .build();
                    })
                    .collect(Collectors.toList());
            githubUserRespVoPageResult.setList(githubUserRespVos);
            return githubUserRespVoPageResult;

        } catch (IOException e) {
            log.error("获取用户列表时发生错误: {}", e.getMessage());
            throw new AppException(ResponseCode.GITHUB_API_ERROR.getCode(), ResponseCode.GITHUB_API_ERROR.getInfo());
        } catch (Exception e) {
            log.error("处理用户数据时发生错误: {}", e.getMessage());
            throw new AppException(ResponseCode.UN_ERROR.getCode(), ResponseCode.UN_ERROR.getInfo());
        }
    }

    @Override
    public List<GithubRepoRespVo> getGithubUserRepos(String username) {
        String response = null;
        String encodedUsername = URLUtil.encode(username);

        String endpoint = String.format("/users/%s/repos?per_page=100&sort=stars&direction=desc", encodedUsername);

        try {
            // 1. API调用
            response = gitHubClientService.fetchGitHubApi(endpoint, null);
            if (response == null || response.isEmpty()) {
                log.warn("API返回空响应，用户: {}", username);
                return Collections.emptyList();
            }

            // 2. 解析响应
            JSONArray repositories = JSON.parseArray(response);
            if (repositories == null || repositories.isEmpty()) {
                log.warn("未找到仓库，用户: {}", username);
                return Collections.emptyList();
            }

            // 3. 直接处理数据，不需要额外的API调用
            return repositories.stream()
                    .map(item -> {
                        JSONObject repo = (JSONObject) item;
                        return GithubRepoRespVo.builder()
                                // 基本信息
                                .name(repo.getString("name"))
                                .description(StringUtils.defaultIfBlank(repo.getString("description"), "No description"))
                                .htmlUrl(repo.getString("html_url"))
                                .homepage(repo.getString("homepage"))
                                // 统计信息
                                .stars(repo.getInteger("stargazers_count"))
                                .forks(repo.getInteger("forks_count"))
                                .watchers(repo.getInteger("watchers_count"))
                                .openIssues(repo.getInteger("open_issues_count"))
                                // 技术相关
                                .language(repo.getString("language"))
                                .defaultBranch(repo.getString("default_branch"))
                                // 状态标记
                                .isPrivate(repo.getBoolean("private"))
                                .isFork(repo.getBoolean("fork"))
                                .isArchived(repo.getBoolean("archived"))
                                // 时间信息
                                .updatedAt(repo.getDate("updated_at"))
                                .createdAt(repo.getDate("created_at"))
                                // topics直接从响应中获取
                                .topics(repo.getJSONArray("topics") != null ?
                                      repo.getJSONArray("topics").toJavaList(String.class) :
                                      Collections.emptyList())
                                .size(formatRepoSize(repo.getInteger("size")))
                                .build();
                    })
                    .sorted(Comparator.comparing(GithubRepoRespVo::getStars).reversed())
                    .collect(Collectors.toList());

        } catch (Exception e) {
            log.error("处理仓库数据时发生错误: {}", e.getMessage());
            throw new AppException(ResponseCode.UN_ERROR.getCode(), ResponseCode.UN_ERROR.getInfo());
        }
    }
    /**
     * 格式化仓库大小
     * @param sizeInKB 仓库大小（KB）
     * @return 格式化后的大小字符串
     */
    private String formatRepoSize(Integer sizeInKB) {
        if (sizeInKB == null || sizeInKB == 0) {
            return "0 KB";
        }

        final long KB = 1024;
        final long MB = KB * 1024;
        final long GB = MB * 1024;

        if (sizeInKB < KB) {
            return sizeInKB + " KB";
        } else if (sizeInKB < MB) {
            double size = sizeInKB / (double) KB;
            return String.format("%.1f MB", size);
        } else {
            double size = sizeInKB / (double) MB;
            return String.format("%.1f GB", size);
        }
    }

}
