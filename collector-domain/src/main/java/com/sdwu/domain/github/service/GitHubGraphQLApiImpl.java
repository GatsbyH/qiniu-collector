package com.sdwu.domain.github.service;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.sdwu.domain.github.model.valobj.DevelopeVo;
import com.sdwu.domain.github.model.valobj.RankResult;
import com.sdwu.domain.github.repository.IGithubUserRepository;
import com.sdwu.domain.github.repository.IScheduledTaskRepository;
import com.sdwu.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sun.applet.AppletIOException;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class GitHubGraphQLApiImpl implements IGitHubGraphQLApi{
    @Resource
    private GitHubClientService gitHubClientService;
    @Resource
    private IGithubUserRepository githubUserRepository;
    @Resource
    private IScheduledTaskRepository scheduledTaskRepository;


    final String GRAPHQL_REPOS_FIELD = "repositories(first: 100, ownerAffiliations: OWNER, orderBy: {direction: DESC, field: STARGAZERS}, after: $after) {\n" +
            "    totalCount\n" +
            "    nodes {\n" +
            "      name\n" +
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

    final String GRAPHQL_STATS_QUERY = "query userInfo($login: String!, $after: String, $includeMergedPullRequests: Boolean!, $includeDiscussions: Boolean!, $includeDiscussionsAnswers: Boolean!) {\n" +
            "    user(login: $login) {\n" +
            "      name\n" +
            "      login\n" +
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

            fetchGitHubApi = gitHubClientService.fetchGitHubApi("/graphql", requestMap);
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

        DevelopeVo developeVo = new DevelopeVo().builder()
                .totalCommits(totalCommits)
                .totalPRs(totalPRs)
                .totalIssues(totalIssues)
                .totalReviews(totalReviews)
                .totalStars(totalStars)
                .totalFollowers(totalFollowers)
                .rankResult(talentRank)
                .contributeTo(contributedTo)
                .build();

        return developeVo;
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
