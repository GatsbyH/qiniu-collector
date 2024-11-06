package com.sdwu.domain.github.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.sdwu.domain.github.model.valobj.DeveloperContributionVo;
import com.sdwu.types.enums.ResponseCode;
import com.sdwu.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TalentRankServiceImpl implements ITalentRankService{
    private final GitHubClientService gitHubClientService;
    @Resource
    private IGitHubApi gitHubApi;
    @Resource
    private IChatGlmApi chatGlmApi;
    private Map<String, Integer> languageCount = new HashMap<>();
    private Map<String, Integer> topicCount = new HashMap<>();
    private Integer totalStars = 0;
    private Integer totalForks = 0;
    private Integer totalIssues = 0;
    private Integer totalCommits = 0;
    private Integer totalContributions = 0;
    private Integer totalPullRequests = 0;
    private Integer totalUserIssues = 0;

    List<String> topics = new ArrayList<>();

    @Autowired
    public TalentRankServiceImpl(GitHubClientService gitHubClientService) {
        this.gitHubClientService = gitHubClientService;
    }
    //废弃
    @Override
    public double getTalentRankByUserName(String username) throws IOException {
        String reposByUserName = gitHubApi.getReposByUserName(username);
        double talentRank = this.calculateOverallTalentRank(username, reposByUserName);
        return talentRank;
    }

    //废弃
    @Override
    public DeveloperContributionVo getDeveloperAssessment(String username) throws IOException {
        String repos = gitHubApi.getReposByUserName(username);
        DeveloperContributionVo developerContributionVo = this.calculateDeveloperGithubContributions(username, repos);
        return developerContributionVo;
    }

    @Override
    public String getDeveloperTechnicalAbility(String username) {
        JSONObject userInfo=null;
        String assessment = null;
        try {
            userInfo = gitHubApi.getUserInfo(username);
        } catch (Exception e) {
            log.error("获取用户{}的信息时发生错误: {}", username, e.getMessage());
        }
        if (userInfo!=null){
            String bio = userInfo.getString("bio");
            String blog = userInfo.getString("blog");
            if (blog!=null||bio!=null){
                try {
                    assessment = chatGlmApi.doDevelopmentAssessment(blog, bio);
                } catch (Exception e) {
                    log.error("获取用户{}的所在地信息时发生错误: {}", username, e.getMessage());
                }
            }
        }
        return assessment;
    }


//    private void calculateDeveloperGithubContributions(String username, String repos) {
//        JSONArray jsonArray = JSON.parseArray(repos);
//        List<String> arrayList = new ArrayList<>();
//        for (int i=0;i<jsonArray.size(); i++){
//            JSONObject jsonObject = jsonArray.getJSONObject(i);
//            String repoName = jsonObject.getString("name");
//            JSONObject jsonObject1 = jsonObject.getJSONObject("owner");
//            String name = jsonObject1.getString("login");
//            try {
//                calculateDeveloperGithubContribution(name, repoName, username,jsonObject);
//            } catch (IOException e) {
//                log.error("计算开发人员 Github 贡献 错误:{}", e.getMessage());
//                throw new RuntimeException(e);
//            }
//        }
//        return ;
//    }

//    private void calculateDeveloperGithubContribution(String owner, String repo, String username, JSONObject jsonObject) throws IOException {
//        int stars = jsonObject.getIntValue("stargazers_count");
//        int forks = jsonObject.getIntValue("forks_count");
//        int issues = jsonObject.getIntValue("open_issues_count");
//        String language = jsonObject.getString("language");
//        JSONArray topicsArray = jsonObject.getJSONArray("topics");
//        List<String> topics = new ArrayList<>();
//        for (int i=0;i<topicsArray.size(); i++){
//            topics.add(topicsArray.getString(i));
//        }
//        int commits=0;
//        int contributions=0;
//        String userContributions = gitHubApi.getUserContributions(owner, repo, username);
//        JSONArray jsonArray = JSON.parseArray(userContributions);
//        if (jsonArray!=null){
//            for (int i=0;i<jsonArray.size(); i++){
//                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
//                if (jsonObject1.getString("login").equals(username)){
//                    contributions=jsonObject1.getIntValue("contributions");
//                }
//            }
//        }
//
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
//
//    }

    //废弃
    public DeveloperContributionVo calculateDeveloperGithubContributions(String username, String repos) {
        double talentRank=0;
        try {
            talentRank = this.calculateOverallTalentRank(username, repos);
        } catch (IOException e) {
            log.error("获取用户{}的仓库信息时发生错误: {}", username, e.getMessage());
            throw new RuntimeException(e);
        }
        JSONArray jsonArray = JSONArray.parseArray(repos);
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String repoName = jsonObject.getString("name");
            JSONObject jsonObject1 = jsonObject.getJSONObject("owner");
            String name = jsonObject1.getString("login");
            try {
                calculateDeveloperGithubContribution(name, repoName, username, jsonObject);
            } catch (IOException e) {
                System.err.println("计算开发人员 Github 贡献 错误:" + e.getMessage());
                e.printStackTrace();
            }
        }
        JSONObject userInfo = null;
        try {
            userInfo = gitHubApi.getUserInfo(username);
        } catch (IOException e) {
            log.error("获取用户{}的个人信息时发生错误: {}", username, e.getMessage());
            throw new RuntimeException(e);
        }
        String assessment = "";
        if (userInfo.getString("blog")!=null){
            log.info("用户{}的博客地址为：{}",username,userInfo.getString("blog"));
            try {
                assessment = chatGlmApi.doDevelopmentAssessment(userInfo.getString("blog"),userInfo.getString("bio"));
            } catch (JsonProcessingException e) {
                log.error("调用 ChatGLM API 错误: {}", e.getMessage());
                throw new RuntimeException(e);
            }
        }
        //把topics变成String
        StringBuilder sb = new StringBuilder();
        for (String topic : topics) {
            sb.append(topic).append(",");
        }
        String field="";
        try {
            field = chatGlmApi.guessTheFieldBasedOnTheTopic(sb.toString());
        } catch (JsonProcessingException e) {
            log.error("调用 ChatGLM API 错误: {}", e.getMessage());
            throw new RuntimeException(e);
        }
        DeveloperContributionVo developerContributionVo = new DeveloperContributionVo()
                .builder()
                .field(field)
                .assessment(assessment)
                .totalTalentRank(talentRank)
                .username(username)
                .avatarUrl(userInfo.getString("avatar_url"))
                .totalStars(totalStars)
                .totalForks(totalForks)
                .totalIssues(totalIssues)
                .totalCommits(totalCommits)
                .totalContributions(totalContributions)
                .totalPullRequests(totalPullRequests)
                .totalUserIssues(totalUserIssues)
                .languageCount(languageCount)
                .topicCount(topicCount)
                .build();
        return developerContributionVo;
    }

    private void calculateDeveloperGithubContribution(String owner, String repo, String username, JSONObject jsonObject) throws IOException {
        int stars=0;
        int forks=0;
        int issues=0;
        if (!jsonObject.getBooleanValue("fork") && jsonObject.getInteger("size") > 0) {
             stars = jsonObject.getIntValue("stargazers_count");
             forks = jsonObject.getIntValue("forks_count");
             issues = jsonObject.getIntValue("open_issues_count");
        }

        String language = jsonObject.getString("language");
        JSONArray topicsArray = jsonObject.getJSONArray("topics");

        for (int i = 0; i < topicsArray.size(); i++) {
            topics.add(topicsArray.getString(i));
        }

        totalStars += stars;
        totalForks += forks;
        totalIssues += issues;

        int commits = gitHubApi.getUserCommits(owner, repo, username);
        totalCommits += commits;

        int contributions = 0;
        String userContributions = gitHubApi.getUserContributions(owner, repo, username);
        JSONArray jsonArray = JSONArray.parseArray(userContributions);
        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                if (jsonObject1.getString("login").equals(username)) {
                    contributions = jsonObject1.getIntValue("contributions");
                    totalContributions += contributions;
                }
            }
        }
        int pullRequests = 0;
        if (!jsonObject.getBooleanValue("fork") && jsonObject.getInteger("size") > 0) {
            pullRequests = gitHubApi.getUserPullRequests(owner, repo, username);
            totalPullRequests += pullRequests;
        }

        int userIssues = 0;
        if (jsonObject.getIntValue("open_issues_count") > 0 && jsonObject.getInteger("size") > 0) {
            userIssues = gitHubApi.getUserIssues(owner, repo, username);
            totalUserIssues += userIssues;
        }
        if (language != null){
            updateLanguageCount(language);
        }
        if (topics != null){
            updateTopicCount(topics);
        }
    }

    private void updateLanguageCount(String language) {
        languageCount.put(language, languageCount.getOrDefault(language, 0) + 1);
    }

    private void updateTopicCount(List<String> topics) {
        for (String topic : topics) {
            topicCount.put(topic, topicCount.getOrDefault(topic, 0) + 1);
        }
    }


//    public double calculateOverallTalentRank(String username, String repos) throws IOException {
//        double totalTalentRank = 0;
//        JSONArray jsonArray = JSON.parseArray(repos);
//        List<String> arrayList = new ArrayList<>();
//        for (int i=0;i<jsonArray.size(); i++){
//            JSONObject jsonObject = jsonArray.getJSONObject(i);
//            String repoName = jsonObject.getString("name");
//            JSONObject jsonObject1 = jsonObject.getJSONObject("owner");
//            String name = jsonObject1.getString("login");
//            totalTalentRank += calculateTalentRank(name, repoName, username,jsonObject);
//        }
//        return totalTalentRank;
//    }
//
//    public double calculateTalentRank(String owner, String repo, String username, JSONObject jsonObject) throws IOException {
//        int stars = jsonObject.getIntValue("stargazers_count");
//        int forks = jsonObject.getIntValue("forks_count");
//        int issues = jsonObject.getIntValue("open_issues_count");
////        int commits=0;
//        int contributions=0;
//        String userContributions = gitHubApi.getUserContributions(owner, repo, username);
//        JSONArray jsonArray = JSON.parseArray(userContributions);
//        if (jsonArray!=null){
//            for (int i=0;i<jsonArray.size(); i++){
//                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
//                if (jsonObject1.getString("login").equals(username)){
//                    contributions=jsonObject1.getIntValue("contributions");
//                }
//            }
//        }
//
////        if (jsonObject.getDate("pushed_at")!= null && jsonObject.getInteger("size") > 0){
////            commits = gitHubApi.getUserCommits(owner, repo, username);
////        }
////        int pullRequests=0;
////        if (!jsonObject.getBooleanValue("fork") && jsonObject.getInteger("size") > 0){
////            pullRequests = gitHubApi.getUserPullRequests(owner, repo, username);
////        }
////        int userIssues=0;
////        if (jsonObject.getIntValue("open_issues_count")>0 && jsonObject.getInteger("size") > 0){
////            userIssues = gitHubApi.getUserIssues(owner, repo, username);
////        }
//
//
//        // 项目重要程度的权重
//        double projectImportance = stars * 0.5 + forks * 0.3 + issues * 0.2;
//
//        // 开发者贡献度的权重
////        double userContribution = commits * 0.5 + pullRequests * 0.3 + userIssues * 0.2;
//
//        double userContribution = contributions;
//
//        // 计算 TalentRank
//        return projectImportance * 0.6 + userContribution * 0.4;
//    }


    //废弃
    public double calculateOverallTalentRank(String username, String repos) throws IOException {
        List<CompletableFuture<Double>> futureList = new ArrayList<>();

        JSONArray jsonArray = JSON.parseArray(repos);
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String repoName = jsonObject.getString("name");
            JSONObject jsonObject1 = jsonObject.getJSONObject("owner");
            String name = jsonObject1.getString("login");

            CompletableFuture<Double> future = CompletableFuture.supplyAsync(() -> {
                try {
                    return calculateTalentRank(name, repoName, username, jsonObject);
                } catch (IOException e) {
                    throw new CompletionException(e);
                }
            });

            futureList.add(future);
        }

        return futureList.stream()
                .map(CompletableFuture::join)
                .mapToDouble(Double::doubleValue)
                .sum();
    }

    public double calculateTalentRank(String owner, String repo, String username, JSONObject jsonObject) throws IOException {
        int stars = jsonObject.getIntValue("stargazers_count");
        int forks = jsonObject.getIntValue("forks_count");
        int issues = jsonObject.getIntValue("open_issues_count");
        // 获取用户在该仓库的贡献
        int contributions = getUserContributions(owner, repo, username);
        int pullRequests = getUserPullRequests(owner, repo, username, jsonObject);
        int issuesReported = getUserIssuesReported(owner, repo, username, jsonObject);
        // 项目重要程度的权重
        double projectImportance = stars * 0.5 + forks * 0.3 + issues * 0.2;
        // 综合开发者贡献度的权重
        double userContribution = contributions * 0.5 + pullRequests * 0.3 + issuesReported * 0.2;
        // 计算 TalentRank
        return projectImportance * 0.6 + userContribution * 0.4;
    }

    private int getUserContributions(String owner, String repo, String username) throws IOException {
        String userContributionsJson = gitHubApi.getUserContributions(owner, repo, username);
        JSONArray contributionsArray = JSON.parseArray(userContributionsJson);

        if (contributionsArray == null) {
            return 0;
        }
        for (int i = 0; i < contributionsArray.size(); i++) {
            JSONObject contribution = contributionsArray.getJSONObject(i);
            if (contribution.getString("login").equals(username)) {
                return contribution.getIntValue("contributions");
            }
        }
        return 0; // 如果没有找到贡献，返回 0
    }

    private int getUserPullRequests(String owner, String repo, String username, JSONObject jsonObject) throws IOException {
        if (!jsonObject.getBooleanValue("fork") && jsonObject.getIntValue("size", 0) > 0) {
            return gitHubApi.getUserPullRequests(owner, repo, username);
        }
        return 0; // 条件不满足时返回 0
    }

    private int getUserIssuesReported(String owner, String repo, String username, JSONObject jsonObject) throws IOException {
        if (jsonObject.getIntValue("open_issues_count", 0) > 0 && jsonObject.getIntValue("size", 0) > 0) {
            return gitHubApi.getUserIssues(owner, repo, username);
        }
        return 0; // 条件不满足时返回 0
    }
}
