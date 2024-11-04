package com.sdwu.domain.github.service;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.sdwu.domain.github.model.entity.Developer;
import com.sdwu.domain.github.model.valobj.DevelopeVo;
import com.sdwu.domain.github.model.valobj.RankResult;
import com.sdwu.domain.github.repository.IGithubUserRepository;
import com.sdwu.domain.github.repository.IScheduledTaskRepository;
import com.sdwu.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

@Service
@Slf4j
public class GitHubGraphQLApiImpl implements IGitHubGraphQLApi{
    @Resource
    private GitHubClientService gitHubClientService;
    @Resource
    private IGithubUserRepository githubUserRepository;
    @Resource
    private IScheduledTaskRepository scheduledTaskRepository;
    @Resource
    private IGitHubApi gitHubApi;

    @Resource
    private IChatGlmApi chatGlmApi;

    @Resource
    private IDeveloperNationService developerNationService;
    final String GRAPHQL_REPOS_FIELD = "repositories(first: 100, ownerAffiliations: OWNER, orderBy: {direction: DESC, field: STARGAZERS}, after: $after) {\n" +
            "    totalCount\n" +
            "    nodes {\n" +
            "      name\n" +
//            "      repositoryTopics(first: 10) {\n" +
//            "        nodes {\n" +
//            "          topic {\n" +
//            "            name\n" +
//            "          }\n" +
//            "        }\n" +
//            "      }\n" +
            "      stargazers {\n" +
            "        totalCount\n" +
            "      }\n" +
            "    }\n" +
            "    pageInfo {\n" +
            "      hasNextPage\n" +
            "      endCursor\n" +
            "    }\n" +
            "  }";

    final String GRAPHQL_REPOS_QUERY = "query userInfo($login: String!, $after: String) {\n" +
            "    user(login: $login) {\n" +
            "      " + GRAPHQL_REPOS_FIELD + "\n" +
            "    }\n" +
            "  }";







//    String TOP_LANGUAGES_QUERY = "query userInfo($login: String!) { "
//            + "user(login: $login) { "
//            + "repositories(ownerAffiliations: OWNER, isFork: false, first: 100) { "
//            + "nodes { "
//            + "name "
//            + "languages(first: 10, orderBy: {field: SIZE, direction: DESC}) { "
//            + "edges { "
//            + "size "
//            + "node { "
//            + "color "
//            + "name "
//            + "} "
//            + "} "
//            + "} "
//            + "} "
//            + "} "
//            + "} ";

    String TOP_LANGUAGES_QUERY = "  user(login: \"用户名\") {"
            + "    repositories(first: 100) {"
            + "      nodes {"
            + "        primaryLanguage {"
            + "          name"
            + "        }"
            + "      }"
            + "    }"
            + "  }";

    String DOMAIN_QUERY = "query($topic: String!) {\n" +
            "  search(query: $topic, type: USER, first: 100) {\n" +
            "    edges {\n" +
            "      node {\n" +
            "        ... on User {\n" +
            "          login\n" +
            "          name\n" +
            "          bio\n" +
            "          avatarUrl\n" +
            "          repositories(first: 1) {\n" +
            "            edges {\n" +
            "              node {\n" +
            "                name\n" +
            "                url\n" +
            "              }\n" +
            "            }\n" +
            "          }\n" +
            "        }\n" +
            "      }\n" +
            "    }\n" +
            "  }\n" +
            "}";



    String searchRepositoriesByTopicAndDescriptionQuery = "query SearchRepositoriesByTopicAndDescription($topic: String!, $description: String!, $after: String) {\n" +
                                                          "    search(type: REPOSITORY, query: \\\"topic:\\\" + $topic + \\\" description:\\\" + $description, first: 10, after: $after) {\n" +
                                                          "      edges {\n" +
                                                          "        node {\n" +
                                                          "          ... on Repository {\n" +
                                                          "            name\n" +
                                                          "            description\n" +
                                                          "            owner {\n" +
                                                          "              login\n" +
                                                          "              avatarUrl\n" +
                                                          "            }\n" +
                                                          "          }\n" +
                                                          "        }\n" +
                                                          "      }\n" +
                                                          "      pageInfo {\n" +
                                                          "        hasNextPage\n" +
                                                          "        endCursor\n" +
                                                          "      }\n" +
                                                          "    }\n" +
                                                          "  }\n";




    String getRepoQuery = "fragment RepoInfo on Repository {\n" +
            "    name\n" +
            "    nameWithOwner\n" +
            "    isPrivate\n" +
            "    isArchived\n" +
            "    isTemplate\n" +
            "    stargazers {\n" +
            "      totalCount\n" +
            "    }\n" +
            "    description\n" +
            "    primaryLanguage {\n" +
            "      color\n" +
            "      id\n" +
            "      name\n" +
            "    }\n" +
            "    forkCount\n" +
            "  }\n" +
            "  query getRepo($login: String!, $repo: String!) {\n" +
            "    user(login: $login) {\n" +
            "      repository(name: $repo) {\n" +
            "        ...RepoInfo\n" +
            "      }\n" +
            "    }\n" +
            "    organization(login: $login) {\n" +
            "      repository(name: $repo) {\n" +
            "        ...RepoInfo\n" +
            "      }\n" +
            "    }\n" +
            "  }";
    final String GRAPHQL_STATS_QUERY = "query userInfo($login: String!, $after: String, $includeMergedPullRequests: Boolean!, $includeDiscussions: Boolean!, $includeDiscussionsAnswers: Boolean!) {\n" +
            "    user(login: $login) {\n" +
            "      name\n" +
            "      login\n" +
//            "      websiteUrl\n" +
//            "      bio\n" +
            "      contributionsCollection {\n" +
            "        totalCommitContributions,\n" +
            "        totalPullRequestReviewContributions\n" +
            "      }\n" +
            "      repositoriesContributedTo(first: 1, contributionTypes: [COMMIT, ISSUE, PULL_REQUEST, REPOSITORY]) {\n" +
            "        totalCount\n" +
            "      }\n" +
            "      pullRequests(first: 1) {\n" +
            "        totalCount\n" +
            "      }\n" +
            "      mergedPullRequests: pullRequests(states: MERGED) @include(if: $includeMergedPullRequests) {\n" +
            "        totalCount\n" +
            "      }\n" +
            "      openIssues: issues(states: OPEN) {\n" +
            "        totalCount\n" +
            "      }\n" +
            "      closedIssues: issues(states: CLOSED) {\n" +
            "        totalCount\n" +
            "      }\n" +
            "      followers {\n" +
            "        totalCount\n" +
            "      }\n" +
            "      repositoryDiscussions @include(if: $includeDiscussions) {\n" +
            "        totalCount\n" +
            "      }\n" +
            "      repositoryDiscussionComments(onlyAnswers: true) @include(if: $includeDiscussionsAnswers) {\n" +
            "        totalCount\n" +
            "      }\n" +
            "      " + GRAPHQL_REPOS_FIELD + "\n" +
            "    }\n" +
            "  }";


    @Override
    public String fetchUserByUsername(String username) {
        String query = String.format("{ user(login: \"%s\") { id, login, name, bio } }", username);
        String fetchGitHubApi= null;
        try {
            fetchGitHubApi = gitHubClientService.fetchGitHubApi("/graphql", Collections.singletonMap("query", query));
        } catch (IOException e) {
            log.error("获取用户{}信息时发生错误: {}", username, e.getMessage());
            throw new RuntimeException(e);
        }
        return fetchGitHubApi;
    }
    //        String query = String.format("{ user(login: \"%s\") { name, login, contributionsCollection { totalCommitContributions, totalPullRequestReviewContributions }, repositoriesContributedTo(first: 1, contributionTypes: [COMMIT, ISSUE, PULL_REQUEST, REPOSITORY]) { totalCount }, pullRequests(first: 1) { totalCount }, mergedPullRequests: pullRequests(states: MERGED) { totalCount }, openIssues: issues(states: OPEN) { totalCount }, closedIssues: issues(states: CLOSED) { totalCount }, followers { totalCount }, repositoryDiscussions { totalCount }, repositoryDiscussionComments(onlyAnswers: true) { totalCount }, repositories(first: 100, orderBy: {direction: DESC, field: STARGAZERS}) { totalCount, nodes { name, stargazers { totalCount } }, pageInfo { hasNextPage, endCursor } } } }", username);
//        try {
//            HashMap<String, Object> map = new HashMap<>();
//            map.put("query", query);
//            fetchGitHubApi = gitHubClientService.fetchGitHubApi("/graphql", map);
//        } catch (IOException e) {
//            log.error("获取用户{}信息时发生错误: {}", username, e.getMessage());
//            throw new RuntimeException(e);
//        }
//    @Override
//    public String fetchUserStats(String username) throws IOException {
//        String fetchGitHubApi= null;
//        Boolean includeMergedPullRequests = true;
//        Boolean includeDiscussions = true;
//        Boolean includeDiscussionsAnswers = true;
//        boolean hasNextPage= false;
//        Integer totalCommits = 0;
//        Integer totalPRs = 0;
//        String after = null;
//        JSONArray stats = null;
//        Integer totalIssues = 0;
//        Integer totalStars= 0;
//        Integer contributedTo = 0;
//        Integer totalFollowers = 0;
//        Integer totalReviews = 0;
//        while (!hasNextPage){
//            HashMap<String, Object> map = new HashMap<>();
//            map.put("query", GRAPHQL_STATS_QUERY);
//            Map<String, Object> variables = new HashMap<>();
//            variables.put("login", username);
//            variables.put("after", after);
//            variables.put("includeMergedPullRequests", includeMergedPullRequests);
//            variables.put("includeDiscussions", includeDiscussions);
//            variables.put("includeDiscussionsAnswers", includeDiscussionsAnswers);
//            map.put("variables", variables);
//            fetchGitHubApi = gitHubClientService.fetchGitHubApi("/graphql", map);
//            JSONObject jsonObject = JSONObject.parseObject(fetchGitHubApi);
//            JSONObject data = jsonObject.getJSONObject("data");
//            JSONArray errorsArray = jsonObject.getJSONArray("errors");
//            if (errorsArray != null && errorsArray.size() > 0) {
//                // 获取第一个错误对象
//                JSONObject errorObject = errorsArray.getJSONObject(0);
//
//                // 从错误对象中获取type字段
//                String type = errorObject.getString("type");
//
//                // 判断type是否为NOT_FOUND
//                if ("NOT_FOUND".equals(type)) {
//                    throw new AppException(404, "用户不存在,你输入的可能是组织");
//                }
//            }
//            JSONObject user = data.getJSONObject("user");
//            JSONObject followers = user.getJSONObject("followers");
//            totalFollowers = followers.getIntValue("totalCount");
//            JSONObject contributionsCollection = user.getJSONObject("contributionsCollection");
//            totalReviews = contributionsCollection.getIntValue("totalPullRequestReviewContributions");
//            totalCommits=contributionsCollection.getIntValue("totalCommitContributions");
//            JSONObject pullRequests = user.getJSONObject("pullRequests");
//            totalPRs = pullRequests.getIntValue("totalCount");
//            JSONObject openIssues = user.getJSONObject("openIssues");
//            openIssues.getIntValue("totalCount");
//            JSONObject closedIssues = user.getJSONObject("closedIssues");
//            closedIssues.getIntValue("totalCount");
//            totalIssues = openIssues.getIntValue("totalCount")+closedIssues.getIntValue("totalCount");
//            JSONObject repositoriesContributedTo = user.getJSONObject("repositoriesContributedTo");
//            contributedTo = repositoriesContributedTo.getIntValue("totalCount");
//            JSONObject repositories = user.getJSONObject("repositories");
//            JSONObject pageInfo = repositories.getJSONObject("pageInfo");
//            hasNextPage = pageInfo.getBoolean("hasNextPage");
//            after = pageInfo.getString("endCursor");
//            JSONArray nodes = repositories.getJSONArray("nodes");
//            if (stats!=null){
//                stats.addAll(nodes);
//            }else {
//                stats = nodes;
//            }
//        }
//        for (Object item : stats) {
//            JSONObject jsonObject = (JSONObject) item;
//            String name = jsonObject.getString("name");
//            JSONObject stargazers = jsonObject.getJSONObject("stargazers");
//            int totalCount = stargazers.getIntValue("totalCount");
//            totalStars += totalCount;
//        }
//
//        log.info("用户{}的仓库总star数为：{}", username, totalStars);
//
//        RankResult talentRank = this.calculateRank(false, totalCommits, totalPRs, totalIssues, totalReviews, totalStars, totalFollowers);
//
//        log.info("用户{}的等级为：{}", username, talentRank.getLevel());
//        return fetchGitHubApi;
//
//    }
    @Override
    public DevelopeVo fetchUserStats(String username) throws IOException {
        String fetchGitHubApi= null;
        Boolean includeMergedPullRequests = true;
        Boolean includeDiscussions = true;
        Boolean includeDiscussionsAnswers = true;

        int totalCommits = 0;
        int totalPRs = 0;
        int totalIssues = 0;
        int totalStars = 0;
        int totalFollowers = 0;
        int totalReviews = 0;
        int contributedTo = 0;

        JSONArray stats = new JSONArray();
        String after = null;
        boolean hasNextPage = true;

        String blog=null;
        String bio=null;

        JSONArray topics=new JSONArray();
        while (hasNextPage) {
            Map<String, Object> variables = new HashMap<>();
            variables.put("login", username);
            variables.put("after", after);
            variables.put("includeMergedPullRequests", includeMergedPullRequests);
            variables.put("includeDiscussions", includeDiscussions);
            variables.put("includeDiscussionsAnswers", includeDiscussionsAnswers);

            Map<String, Object> requestMap = new HashMap<>();
            requestMap.put("query", GRAPHQL_STATS_QUERY);
            requestMap.put("variables", variables);

            try {
                fetchGitHubApi = gitHubClientService.fetchGitHubApi("/graphql", requestMap);
            } catch (IOException e) {
                log.error("获取用户{}的贡献数值时发生错误: {}", username, e.getMessage());
                throw new RuntimeException(e);
            }
            JSONObject jsonObject = JSONObject.parseObject(fetchGitHubApi);
            JSONObject data = jsonObject.getJSONObject("data");

            // 处理错误
            JSONArray errorsArray = jsonObject.getJSONArray("errors");
            if (errorsArray != null && !errorsArray.isEmpty()) {
                JSONObject errorObject = errorsArray.getJSONObject(0);
                if ("NOT_FOUND".equals(errorObject.getString("type"))) {
                    throw new AppException(404, "用户不存在, 你输入的可能是组织");
                }
            }

            // 获取用户数据
            JSONObject user = data.getJSONObject("user");
//            blog=user.getString("websiteUrl");
//            bio=user.getString("bio");
            totalFollowers = user.getJSONObject("followers").getIntValue("totalCount");
            totalReviews = user.getJSONObject("contributionsCollection").getIntValue("totalPullRequestReviewContributions");
            totalCommits = user.getJSONObject("contributionsCollection").getIntValue("totalCommitContributions");
            totalPRs = user.getJSONObject("pullRequests").getIntValue("totalCount");

            JSONObject openIssues = user.getJSONObject("openIssues");
            JSONObject closedIssues = user.getJSONObject("closedIssues");
            totalIssues = openIssues.getIntValue("totalCount") + closedIssues.getIntValue("totalCount");
            contributedTo = user.getJSONObject("repositoriesContributedTo").getIntValue("totalCount");

            JSONObject repositories = user.getJSONObject("repositories");
            hasNextPage = repositories.getJSONObject("pageInfo").getBoolean("hasNextPage");
            after = repositories.getJSONObject("pageInfo").getString("endCursor");


            // 合并当前页的仓库数据
            JSONArray nodes = repositories.getJSONArray("nodes");

            stats.addAll(nodes);
        }


        // 计算总星标数
//        totalStars = stats.stream()
//                .mapToInt(node -> ((JSONObject) node).getJSONObject("stargazers").getIntValue("totalCount"))
//                .sum();
        JSONArray repositoryTopics = new JSONArray();
        for (Object item : stats) {
            JSONObject jsonObject = (JSONObject) item;
            JSONObject stargazers = jsonObject.getJSONObject("stargazers");
//            repositoryTopics.add(jsonObject.getJSONObject("repositoryTopics"));
            int totalCount = stargazers.getIntValue("totalCount");
            totalStars += totalCount;
        }

        log.info("用户 {} 的仓库总 star 数为：{}", username, totalStars);
        log.info("用户 {} 的仓库topics为：{}", username,repositoryTopics);

//        String assessment = "";
//        if (blog!=null){
//            log.info("用户{}的博客地址为：{}",username,blog);
//            try {
//                assessment = chatGlmApi.doDevelopmentAssessment(blog,bio);
//            } catch (JsonProcessingException e) {
//                log.error("调用 ChatGLM API 错误: {}", e.getMessage());
//                throw new RuntimeException(e);
//            }
//        }

//        String field="";
//        try {
//            field = chatGlmApi.guessTheFieldBasedOnTheTopic(repositoryTopics.toString());
//        } catch (JsonProcessingException e) {
//            log.error("调用 ChatGLM API 错误: {}", e.getMessage());
//            throw new RuntimeException(e);
//        }



        RankResult talentRank = calculateRank(false, totalCommits, totalPRs, totalIssues, totalReviews, totalStars, totalFollowers);
        log.info("用户 {} 的等级为：{}", username, talentRank.getLevel());

        DevelopeVo developeVo = new DevelopeVo().builder()
                .totalCommits(totalCommits)
                .totalPRs(totalPRs)
//                .field(field)
//                .assessment(assessment)
                .totalIssues(totalIssues)
                .totalReviews(totalReviews)
                .totalStars(totalStars)
                .totalFollowers(totalFollowers)
                .rankResult(talentRank)
                .contributeTo(contributedTo)
                .build();

        return developeVo;
    }



    public RankResult getTalentRankByUserName(String username) throws IOException {
        String fetchGitHubApi= null;
        Boolean includeMergedPullRequests = true;
        Boolean includeDiscussions = true;
        Boolean includeDiscussionsAnswers = true;

        int totalCommits = 0;
        int totalPRs = 0;
        int totalIssues = 0;
        int totalStars = 0;
        int totalFollowers = 0;
        int totalReviews = 0;
        int contributedTo = 0;

        JSONArray stats = new JSONArray();
        String after = null;
        boolean hasNextPage = true;

        while (hasNextPage) {
            Map<String, Object> variables = new HashMap<>();
            variables.put("login", username);
            variables.put("after", after);
            variables.put("includeMergedPullRequests", includeMergedPullRequests);
            variables.put("includeDiscussions", includeDiscussions);
            variables.put("includeDiscussionsAnswers", includeDiscussionsAnswers);

            Map<String, Object> requestMap = new HashMap<>();
            requestMap.put("query", GRAPHQL_STATS_QUERY);
            requestMap.put("variables", variables);

            try {
                fetchGitHubApi = gitHubClientService.fetchGitHubApi("/graphql", requestMap);
            } catch (IOException e) {
                log.error("获取用户{}的贡献数值时发生错误: {}", username, e.getMessage());
                throw new RuntimeException(e);
            }
            JSONObject jsonObject = JSONObject.parseObject(fetchGitHubApi);
            JSONObject data = jsonObject.getJSONObject("data");

            // 处理错误
            JSONArray errorsArray = jsonObject.getJSONArray("errors");
            if (errorsArray != null && !errorsArray.isEmpty()) {
                JSONObject errorObject = errorsArray.getJSONObject(0);
                if ("NOT_FOUND".equals(errorObject.getString("type"))) {
                    throw new AppException(404, "用户不存在, 你输入的可能是组织");
                }
            }

            // 获取用户数据
            JSONObject user = data.getJSONObject("user");
            totalFollowers = user.getJSONObject("followers").getIntValue("totalCount");
            totalReviews = user.getJSONObject("contributionsCollection").getIntValue("totalPullRequestReviewContributions");
            totalCommits = user.getJSONObject("contributionsCollection").getIntValue("totalCommitContributions");
            totalPRs = user.getJSONObject("pullRequests").getIntValue("totalCount");

            JSONObject openIssues = user.getJSONObject("openIssues");
            JSONObject closedIssues = user.getJSONObject("closedIssues");
            totalIssues = openIssues.getIntValue("totalCount") + closedIssues.getIntValue("totalCount");
            contributedTo = user.getJSONObject("repositoriesContributedTo").getIntValue("totalCount");

            JSONObject repositories = user.getJSONObject("repositories");
            hasNextPage = repositories.getJSONObject("pageInfo").getBoolean("hasNextPage");
            after = repositories.getJSONObject("pageInfo").getString("endCursor");

            // 合并当前页的仓库数据
            JSONArray nodes = repositories.getJSONArray("nodes");
            stats.addAll(nodes);
        }


        // 计算总星标数
//        totalStars = stats.stream()
//                .mapToInt(node -> ((JSONObject) node).getJSONObject("stargazers").getIntValue("totalCount"))
//                .sum();
        for (Object item : stats) {
            JSONObject jsonObject = (JSONObject) item;
            JSONObject stargazers = jsonObject.getJSONObject("stargazers");
            int totalCount = stargazers.getIntValue("totalCount");
            totalStars += totalCount;
        }
        log.info("用户 {} 的仓库总 star 数为：{}", username, totalStars);

        RankResult talentRank = calculateRank(false, totalCommits, totalPRs, totalIssues, totalReviews, totalStars, totalFollowers);
        log.info("用户 {} 的等级为：{}", username, talentRank.getLevel());

        return talentRank;
    }
    public static String buildSearchQuery(String topic) {
        return "query{" +
                "search(query:\"topic:" + topic + "\", type: REPOSITORY, first: 10) {" +
                "edges {" +
                "node {" +
                "... on Repository {" +
                "id," + // 添加逗号
                "name," + // 添加逗号
                "owner {" +
                "login" +
                "}," + // 添加逗号
                "languages(first: 10){" +
                "nodes{" +
                "name" +
                "}" +
                "}," + // 添加逗号
                "repositoryTopics(first: 10) {" +
                "nodes {" +
                "topic {" +
                "name" +
                "}" +
                "}" +
                "}" +
                "}" +
                "}" +
                "}" +
                "}" +
                "}";
    }

    @Override
    public void fetchTopLanguages(String username) {
        Map<String, Object> variables = new HashMap<>();
//        variables.put("login", username);
//        variables.put("repo", "CuppaCorner");
        variables.put("topic", "javaweb");
        variables.put("$description", "java");
        String query = buildSearchQuery("java");
        String searchRepositoriesByTopicAndDescriptionQuery = readFile("searchRepositoriesByTopicAndDescriptionQuery.graphql");
        String modifiedQuery = searchRepositoriesByTopicAndDescriptionQuery.replace("$topic", "java");
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("query", searchRepositoriesByTopicAndDescriptionQuery);
        requestMap.put("variables", variables);
        String fetchGitHubApi = null;

        try {
            fetchGitHubApi = gitHubClientService.fetchGitHubApi("/graphql", Collections.singletonMap("query", modifiedQuery));
        } catch (IOException e) {
            log.error("获取用户{}的语言分布时发生错误: {}", username, e.getMessage());
            throw new RuntimeException(e);
        }
        log.info("用户{}的语言分布信息为: {}", username, fetchGitHubApi);
    }

    @Override
    public void fetchUserByRepoTopic(String topic) {

        boolean hasNextPage = true;
        String after=null;
        String fetchGitHubApi = null;
        JSONObject userInfo=new JSONObject();
        Set<String> uniqueLogins = new HashSet<>(); // 用于存储唯一的login
        List<Developer> developers = new ArrayList<>();
        try {
            while (hasNextPage){
                String newTopic = topic.replaceAll("\\s+", "");
                String searchRepositoriesByTopicAndDescriptionQuery = readFile("searchRepositoriesByTopicAndDescriptionQuery.graphql");
                String modifiedQuery = searchRepositoriesByTopicAndDescriptionQuery.replace("$topic", newTopic);;
                if (after!=null){
                    modifiedQuery = modifiedQuery.replace("null", ""+after+"");
                }
                fetchGitHubApi = gitHubClientService.fetchGitHubApi("/graphql", Collections.singletonMap("query", modifiedQuery));
                JSONObject jsonObject = JSONObject.parseObject(fetchGitHubApi);

                JSONObject data = jsonObject.getJSONObject("data");

//                JSONObject pageInfo = data.getJSONObject("search").getJSONObject("pageInfo");
//                hasNextPage = pageInfo.getBoolean("hasNextPage");
//                after = pageInfo.getString("endCursor");
                JSONObject searchObject = data.getJSONObject("search");
                if (searchObject != null) {
                    JSONObject pageInfo = searchObject.getJSONObject("pageInfo");
                    if (pageInfo != null) {
                        hasNextPage = pageInfo.getBoolean("hasNextPage");
                        // 继续处理 pageInfo
                    } else {
                        // 处理 pageInfo 为空的情况
                        hasNextPage = false; // 或其他适当的默认值
                    }
                } else {
                    // 处理 search 为空的情况
                    hasNextPage = false; // 或其他适当的默认值
                }
                JSONArray nodes = data.getJSONObject("search").getJSONArray("edges");
                for (Object node : nodes) {
                    JSONObject jsonObject1 = (JSONObject) node;
                    String login = jsonObject1.getJSONObject("node").getJSONObject("owner").getString("login");

                    if (uniqueLogins.contains(login)) {
                        continue; // 如果这个login已经处理过，跳过
                    }
                    uniqueLogins.add(login); // 将这个login添加到集合中
                    JSONObject owner= jsonObject1.getJSONObject("node").getJSONObject("owner");
                    String __typename =owner.getString("__typename");
                    if (__typename.equals("Organization")){
                        continue;
                    }
                    userInfo = gitHubApi.getUserInfo(login);
                    double talentRank = 0;
                    String level = null;
                    String developerNation = null;
                    developerNation = developerNationService.getDeveloperNation(login);
                    RankResult talentRankByUserName = this.getTalentRankByUserName(login);
                    if (talentRankByUserName!=null){
                        talentRank =100- talentRankByUserName.getPercentile();
                        level=talentRankByUserName.getLevel();
                    }
                    String assessment = null;
                    if (userInfo.getString("blog")!=null){
                        log.info("用户{}的博客地址为：{}",login,userInfo.getString("blog"));
                        assessment = chatGlmApi.doDevelopmentAssessment(userInfo.getString("blog"),userInfo.getString("bio"));
                    }
                    // 使用String.format()方法格式化为两位小数的字符串
                    String talentRankFormatted = String.format("%.2f", talentRank);

                    // 如果你需要talentRank仍然是double类型，可以这样做：
                    talentRank = Double.parseDouble(talentRankFormatted);
                    Developer developer = Developer.builder()
                            .login(login)
                            .bio(userInfo.getString("bio"))
                            .company(userInfo.getString("company"))
                            .field(topic)
                            .location(userInfo.getString("location"))
                            .htmlUrl(userInfo.getString("html_url"))
                            .name(userInfo.getString("name"))
                            .blog(userInfo.getString("blog"))
                            .email(userInfo.getString("email"))
                            .hireable(userInfo.getBoolean("hireable"))
                            .talentRank(talentRank)
                            .level(level)
                            .nation(developerNation)
                            .assessment(assessment)
                            .twitterUsername(userInfo.getString("twitter_username"))
                            .publicRepos(userInfo.getIntValue("public_repos"))
                            .publicGists(userInfo.getIntValue("public_gists"))
                            .followers(userInfo.getIntValue("followers"))
                            .following(userInfo.getIntValue("following"))
                            .type(userInfo.getString("type"))
                            .repositoryUrl(jsonObject1.getJSONObject("node").getString("url"))
                            .avatarUrl(userInfo.getString("avatar_url"))
                            .build();
                    log.info("开发者: {}", developer.toString());
//                    developers.add(developer);
                    githubUserRepository.saveSingle(topic,developer);
                }
            }
            log.info("开发者们: {}", developers);
//            if (!developers.isEmpty()) {
//                githubUserRepository.save(topic, developers);
//            }
        } catch (IOException e) {

            throw new RuntimeException(e);
        }
    }

    // 辅助方法，用于读取文件内容
    private String readFile(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);
        if (inputStream == null) {
            log.error("文件{}未找到", fileName);
            return null;
        }
        StringBuilder contentBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String currentLine;
            while ((currentLine = br.readLine()) != null) {
                contentBuilder.append(currentLine).append("\n");
            }
        } catch (IOException e) {
            log.error("读取文件{}时发生错误: {}", fileName, e.getMessage());
            return null;
        }
        return contentBuilder.toString();
    }
    /**
         * Calculates the exponential cdf.
         *
         * @param x The value.
         * @return The exponential cdf.
         */
    private static double exponentialCdf(double x) {
        return 1 - Math.pow(2, -x);
    }

    /**
     * Calculates the log normal cdf.
     *
     * @param x The value.
     * @return The log normal cdf.
     */
    private static double logNormalCdf(double x) {
        // approximation
        return x / (1 + x);
    }

    /**
     * Calculates the user's rank.
     *
     * @param allCommits Whether include_all_commits was used.
     * @param commits Number of commits.
     * @param prs The number of pull requests.
     * @param issues The number of issues.
     * @param reviews The number of reviews.
     * @param stars The number of stars.
     * @param followers The number of followers.
     * @return A RankResult object containing the user's rank and percentile.
     */
    public static RankResult calculateRank(boolean allCommits, int commits, int prs, int issues,
                                           int reviews, int stars, int followers) {
        final int COMMITS_MEDIAN = allCommits ? 1000 : 250;
        final int COMMITS_WEIGHT = 2;
        final int PRS_MEDIAN = 50;
        final int PRS_WEIGHT = 3;
        final int ISSUES_MEDIAN = 25;
        final int ISSUES_WEIGHT = 1;
        final int REVIEWS_MEDIAN = 2;
        final int REVIEWS_WEIGHT = 1;
        final int STARS_MEDIAN = 50;
        final int STARS_WEIGHT = 4;
        final int FOLLOWERS_MEDIAN = 10;
        final int FOLLOWERS_WEIGHT = 1;

        double totalWeight = COMMITS_WEIGHT + PRS_WEIGHT + ISSUES_WEIGHT + REVIEWS_WEIGHT + STARS_WEIGHT + FOLLOWERS_WEIGHT;

        double rank = 1 - (COMMITS_WEIGHT * exponentialCdf((double) commits / COMMITS_MEDIAN) +
                PRS_WEIGHT * exponentialCdf((double) prs / PRS_MEDIAN) +
                ISSUES_WEIGHT * exponentialCdf((double) issues / ISSUES_MEDIAN) +
                REVIEWS_WEIGHT * exponentialCdf((double) reviews / REVIEWS_MEDIAN) +
                STARS_WEIGHT * logNormalCdf((double) stars / STARS_MEDIAN) +
                FOLLOWERS_WEIGHT * logNormalCdf((double) followers / FOLLOWERS_MEDIAN)) / totalWeight;

        String level = determineLevel(rank);

        return new RankResult(level, rank * 100);
    }

    private static String determineLevel(double rank) {
        double[] thresholds = {1, 12.5, 25, 37.5, 50, 62.5, 75, 87.5, 100};
        String[] levels = {"S", "A+", "A", "A-", "B+", "B", "B-", "C+", "C"};

        for (int i = 0; i < thresholds.length; i++) {
            if (rank * 100 <= thresholds[i]) {
                return levels[i];
            }
        }
        return "Unknown"; // fallback in case of no match
    }

}
