package com.sdwu.domain.github.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class TalentRankServiceImpl implements ITalentRankService{
    private final GitHubClientService gitHubClientService;
    @Resource
    private IGitHubApi gitHubApi;
    @Autowired
    public TalentRankServiceImpl(GitHubClientService gitHubClientService) {
        this.gitHubClientService = gitHubClientService;
    }

    @Override
    public double getTalentRankByUserName(String username) throws IOException {
        String reposByUserName = gitHubApi.getReposByUserName(username);
        double talentRank = this.calculateOverallTalentRank(username, reposByUserName);
        return talentRank;
    }
    public double calculateOverallTalentRank(String username, String repos) throws IOException {
        double totalTalentRank = 0;
        JSONArray jsonArray = JSON.parseArray(repos);
        List<String> arrayList = new ArrayList<>();
        for (int i=0;i<jsonArray.size(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String repoName = jsonObject.getString("name");
            JSONObject jsonObject1 = jsonObject.getJSONObject("owner");
            String name = jsonObject1.getString("login");
            totalTalentRank += calculateTalentRank(name, repoName, username,jsonObject);
        }
        return totalTalentRank;
    }

    public double calculateTalentRank(String owner, String repo, String username, JSONObject jsonObject) throws IOException {
        int stars = jsonObject.getIntValue("stargazers_count");
        int forks = jsonObject.getIntValue("forks_count");
        int issues = jsonObject.getIntValue("open_issues_count");
        int commits=0;
        int contributions=0;
        String userContributions = gitHubApi.getUserContributions(owner, repo, username);
        JSONArray jsonArray = JSON.parseArray(userContributions);
        for (int i=0;i<jsonArray.size(); i++){
            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
            if (jsonObject1.getString("login").equals(username)){
                contributions=jsonObject1.getIntValue("contributions");
            }
        }
//        if (jsonObject.getDate("pushed_at")!= null && jsonObject.getInteger("size") > 0){
//            commits = gitHubApi.getUserCommits(owner, repo, username);
//        }
//        int pullRequests=0;
//        if (!jsonObject.getBooleanValue("fork") && jsonObject.getInteger("size") > 0){
//            pullRequests = gitHubApi.getUserPullRequests(owner, repo, username);
//        }
//        int userIssues=0;
//        if (jsonObject.getIntValue("open_issues_count")>0 && jsonObject.getInteger("size") > 0){
//            userIssues = gitHubApi.getUserIssues(owner, repo, username);
//        }


        // 项目重要程度的权重
        double projectImportance = stars * 0.5 + forks * 0.3 + issues * 0.2;

        // 开发者贡献度的权重
//        double userContribution = commits * 0.5 + pullRequests * 0.3 + userIssues * 0.2;
        double userContribution = contributions;

        // 计算 TalentRank
        return projectImportance * 0.6 + userContribution * 0.4;
    }

}
