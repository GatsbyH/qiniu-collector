package com.sdwu.domain.github.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class GitHubApiImpl implements IGitHubApi{

    @Resource
    private GitHubClientService gitHubClientService;
    @Override
    public String getReposByUserName(String username) throws IOException {
        String fetchGitHubApi = gitHubClientService.fetchGitHubApi("/users/" + username + "/repos", null);
        return fetchGitHubApi;
    }

    @Override
    public int getUserCommits(String owner, String repo, String username) throws IOException {
        String fetchGitHubApi = gitHubClientService.fetchGitHubApi("/repos/" + owner + "/" + repo + "/commits?author=" + username, null);
        JSONArray jsonArray = JSON.parseArray(fetchGitHubApi);
        return jsonArray.size();
    }

    @Override
    public int getUserPullRequests(String owner, String repo, String username) throws IOException {
        String fetchGitHubApi = gitHubClientService.fetchGitHubApi("/repos/" + owner + "/" + repo + "/pulls?state=all&author=" + username, null);
        JSONArray jsonArray = JSON.parseArray(fetchGitHubApi);
        return jsonArray.size();
    }

    @Override
    public int getUserIssues(String owner, String repo, String username) throws IOException {
        String fetchGitHubApi = gitHubClientService.fetchGitHubApi("/repos/" + owner + "/" + repo + "/issues?creator=" + username, null);
        JSONArray jsonArray = JSON.parseArray(fetchGitHubApi);
        return jsonArray.size();
    }

    @Override
    public String getUserContributions(String owner, String repo, String username) throws IOException {
        String fetchGitHubApi = gitHubClientService.fetchGitHubApi("/repos/" + owner + "/" + repo + "/contributors", null);
        return fetchGitHubApi;
    }

    @Override
    public JSONObject getUserInfo(String username) throws IOException {
        String fetchGitHubApi = gitHubClientService.fetchGitHubApi("/users/" + username, null);
        JSONObject jsonObject = JSONObject.parseObject(fetchGitHubApi);
        return jsonObject;
    }

    @Override
    public List<String> getFollowersByUserName(String username) throws IOException {
        String fetchGitHubApi = gitHubClientService.fetchGitHubApi("/users/" + username + "/followers", null);
        JSONArray jsonArray = JSON.parseArray(fetchGitHubApi);
        List<String> locations = new ArrayList<>();
        for (Object o : jsonArray) {
            JSONObject jsonObject = (JSONObject) o;
            if (jsonObject == null&&jsonObject.getString("location")==null){
                continue;
            }
            JSONObject userInfo = this.getUserInfo(jsonObject.getString("login"));
            locations.add(userInfo.getString("location"));
        }
        return locations;
    }

    @Override
    public List<String> getFollowingByUserName(String username) throws IOException {
        String fetchGitHubApi = gitHubClientService.fetchGitHubApi("/users/" + username + "/following", null);
        JSONArray jsonArray = JSON.parseArray(fetchGitHubApi);
        List<String> locations = new ArrayList<>();
        for (Object o : jsonArray) {
            JSONObject jsonObject = (JSONObject) o;
            if (jsonObject == null&&jsonObject.getString("location")==null){
                continue;
            }
            JSONObject userInfo = this.getUserInfo(jsonObject.getString("login"));
            locations.add(userInfo.getString("location"));
        }
        return locations;
    }
}
