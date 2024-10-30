package com.sdwu.domain.github.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.sdwu.domain.github.model.entity.Developer;
import com.sdwu.domain.github.repository.IGithubUserRepository;
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

    @Override
    public String getDeveloperByFieldAndNation(String field,String nation) throws IOException {
        String fetchGitHubApi = gitHubClientService.fetchGitHubApi("/search/repositories?q=language:" + field +"&sort=stars&order=desc", null);
       return fetchGitHubApi;
    }

    @Override
    public String getDevelopersByFields(String field) throws IOException {
        Integer page = githubUserRepository.getGitHubPageByField(field);
//        Integer page = 1;
        Integer per_page = 4;
        try {
            String fetchGitHubApi = gitHubClientService.fetchGitHubApi("/search/repositories?q=" + field + "+in:description,topics&sort=stars" + "&page=" + page + "&per_page=" + per_page, null);

            // Check if the response is valid (you may want to implement a response validation method)
            if (fetchGitHubApi == null || fetchGitHubApi.isEmpty()) {
                log.error("Received an empty response for field: {}", field);
                throw new IOException("Received an empty response from GitHub API");
            }

            // Optionally, parse the response and check for errors (if applicable)
            JSONObject responseObject = JSON.parseObject(fetchGitHubApi);
            if (responseObject.getInteger("total_count") == 0) {
                log.warn("No repositories found for field: {}", field);
            }

            // Update the page number in the repository
            githubUserRepository.updateGitHubPageByField(field);

            return fetchGitHubApi;

        } catch (IOException e) {
            log.error("Error fetching developers from GitHub API for field: {} - {}", field, e.getMessage());
            throw e; // Rethrow to notify the caller about the error
        } catch (Exception e) {
            log.error("Unexpected error occurred while fetching developers for field: {} - {}", field, e.getMessage());
            throw new IOException("An unexpected error occurred", e);
        }
    }
}
