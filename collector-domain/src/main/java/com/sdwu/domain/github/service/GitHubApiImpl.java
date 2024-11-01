package com.sdwu.domain.github.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.sdwu.domain.github.model.entity.Developer;
import com.sdwu.domain.github.repository.IGithubUserRepository;
import com.sdwu.domain.github.repository.IScheduledTaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
}
