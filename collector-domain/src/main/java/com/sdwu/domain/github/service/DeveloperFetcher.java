package com.sdwu.domain.github.service;//package com.sdwu.domain.github.service;
//
//import com.alibaba.fastjson2.JSON;
//import com.alibaba.fastjson2.JSONArray;
//import com.alibaba.fastjson2.JSONObject;
//import com.sdwu.domain.github.model.entity.Developer;
//import com.sdwu.domain.github.repository.IGithubUserRepository;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import javax.annotation.Resource;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.TimeUnit;
//
//@Service
//@Slf4j
//public class DeveloperFetcher {
//    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
//    private String currentField;
//    private String currentNation;
//    private boolean isFetching = false;
//    @Resource
//    private IGitHubApi gitHubApi;
//    @Resource
//    private ITalentRankService talentRankService;
//    @Resource
//    private IDeveloperNationService developerNationService;
//
//    @Resource
//    private IGithubUserRepository githubUserRepository;
//
//
//    public void startFetching(String field, String nation) {
//        Boolean isFetching = githubUserRepository.getFetchFlag(field);
//
//        // 如果已经在获取中，则不再启动新任务
//        if (isFetching) {
//            return;
//        }
//        currentField = field;
//        currentNation = nation;
//        githubUserRepository.updateFetchFlag(field);
//
//        scheduler.scheduleAtFixedRate(() -> {
//            try {
//                getDeveloperByFieldAndNation(currentField, currentNation);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }, 0, 2, TimeUnit.SECONDS);
//    }
//
//    private void getDeveloperByFieldAndNation(String field, String nation) throws IOException {
//        String developerByFieldAndNation = gitHubApi.getDevelopersByFields(field);
//        JSONObject responseObject = JSON.parseObject(developerByFieldAndNation);
//        JSONArray itemsArray = responseObject.getJSONArray("items");
//        List<Developer> developers = new ArrayList<>();
//        for (Object o : itemsArray) {
//            JSONObject jsonObject = (JSONObject) o;
//            JSONObject owner = jsonObject.getJSONObject("owner");
//            String login = owner.getString("login");
//            String htmlUrl = owner.getString("html_url");
//            JSONObject userInfo = gitHubApi.getUserInfo(login);
//            String location = userInfo.getString("location");
//            double talentRank = talentRankService.getTalentRankByUserName(login);
//            String developerNation = developerNationService.getDeveloperNation(login);
//            Developer developer = Developer.builder()
//                    .login(login)
//                    .field(field)
//                    .location(location)
//                    .nation(developerNation)
//                    .htmlUrl(htmlUrl)
//                    .talentRank(talentRank)
//                    .build();
//            developers.add(developer);
//            System.out.println("用户名："+login+"  位置："+location+"  领域："+field+"  国籍："+developerNation+" github地址："+ htmlUrl+"  人才指数："+talentRank);
//        }
//        githubUserRepository.save(field,developers);
//    }
//
//    public void stopFetching(String field) {
//        if (isFetching) {
//            scheduler.shutdown();
//            githubUserRepository.updateFetchFlag(field);
//            log.info("Fetching stopped for field: " + field);
//        }
//    }
//
//
//}
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.sdwu.domain.github.model.entity.Developer;
import com.sdwu.domain.github.model.entity.ScheduledTask;
import com.sdwu.domain.github.model.valobj.RankResult;
import com.sdwu.domain.github.repository.IGithubLockRepository;
import com.sdwu.domain.github.repository.IGithubUserRepository;
import com.sdwu.domain.github.repository.IScheduledTaskRepository;
import com.sdwu.domain.github.service.IDeveloperNationService;
import com.sdwu.domain.github.service.IGitHubApi;
import com.sdwu.domain.github.service.ITalentRankService;
import com.sdwu.types.enums.ResponseCode;
import com.sdwu.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

//@Service
//@Slf4j
//public class DeveloperFetcher {
//    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(5);
//    private ScheduledFuture<?> scheduledFutureTime; // 用于跟踪定时任务
////    private String currentField;
////    private String currentNation;
//    private final ConcurrentHashMap<String, ScheduledFuture<?>> scheduledFuturesTime = new ConcurrentHashMap<>();
//
//
//    private ListeningExecutorService executorService;
//    private Map<String, String> scheduledFutures = new HashMap<>();
//    private String currentField;
//    private String currentNation;
//
//    @Resource
//    private IGitHubApi gitHubApi;
//    @Resource
//    private ITalentRankService talentRankService;
//    @Resource
//    private IDeveloperNationService developerNationService;
//    @Resource
//    private IGithubUserRepository githubUserRepository;
//
//    @Resource
//    private IScheduledTaskRepository scheduledTaskRepository;
//    @Resource
//    private IChatGlmApi chatGlmApi;
//
//    @Resource
//    private ITalentRankGraphQLService talentRankGraphQLService;
//
//    @Resource
//    private IGithubLockRepository githubLockRepository;
//    public DeveloperFetcher() {
//        // 初始化线程池，可以根据需要调整线程池的大小
//        executorService = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10)); // 使用Guava装饰线程池
//    }
//    public void startFetchingTime(String field, String nation) {
////        Boolean isFetching = githubUserRepository.getFetchFlag(field);
//        //检查是否已经在获取中
//        if (scheduledFutures.containsKey(field)) {
//            return; // 已在获取中，返回
//        }
//        if (!githubLockRepository.tryLockByField(field)){
//            log.info("获取锁失败，正在等待");
//            return;
//        }
//        if (!scheduledTaskRepository.checkScheduledTaskByField(field)){
//            scheduledTaskRepository.insertScheduledTask(field,"RUNNING");
//        }
//        scheduledTaskRepository.updateScheduledTaskRUNNING(field,"RUNNING");
//
//
//        // 如果已经在获取中，则不再启动新任务
////        if (isFetching) {
////            return;
////        }
//        currentField = field;
//        currentNation = nation;
//        githubUserRepository.updateFetchFlag(field);
//
//        scheduledFutureTime = scheduler.scheduleAtFixedRate(() -> {
//            try {
//                log.info("开始获取用户信息");
//                boolean developerByFieldAndNation = getDeveloperByFieldAndNation(currentField, currentNation);
//                if (developerByFieldAndNation) {
//                    log.info("获取用户信息成功");
//                } else {
//                    log.info("获取用户信息完成");
//                    if (githubLockRepository.isLockedByCurrentThread(field)){
//                        this.stopFetching(field);
//                    }
//                }
//            } catch (IOException e) {
//                log.error("获取用户信息时发生错误: {}", e.getMessage());
//                String errorMessage = e.getMessage();
//                scheduledTaskRepository.updateScheduledTask(field,"FAILED",errorMessage);
//            }
//        }, 0, 30, TimeUnit.SECONDS);
//
//        scheduledFuturesTime.put(field, scheduledFutureTime); // 存储到 Map 中
//
//    }
//
//    public void startFetching(String field, String nation) {
//        if (!scheduledTaskRepository.checkScheduledTaskByField(field)) {
//            scheduledTaskRepository.insertScheduledTask(field, "RUNNING");
//        }
//        scheduledTaskRepository.updateScheduledTaskRUNNING(field, "RUNNING");
//
////        currentField = field;
////        currentNation = nation;
////        githubUserRepository.updateFetchFlag(field);
//
////        // 提交任务到线程池，而不是使用ScheduledExecutorService
////        executorService.submit(() -> {
////            try {
////                getDeveloperByFieldAndNation(currentField, currentNation);
////            } catch (IOException e) {
////                log.error("获取用户信息时发生错误: {}", e.getMessage());
////                String errorMessage = e.getMessage();
////                scheduledTaskRepository.updateScheduledTask(field, "FAILED", errorMessage);
////            }
////        }).addListener(() -> {
////            // 任务完成后的回调
////            scheduledTaskRepository.updateScheduledTask(field, "COMPLETED", null);
////        }, MoreExecutors.directExecutor()); // 使用直接执行器处理回调
//
//    }
//
//
//
//
//
////    private void getDeveloperByFieldAndNation(String field, String nation) throws IOException {
////            List<Developer> developers = null;
////
////        String developerByFieldAndNation = null;
////        try {
////            Integer page = githubUserRepository.getGitHubPageByField(field);
////            if (page>250){
////                log.info("{}领域已抓取完毕,只有前 1000 个搜索结果可用", field);
////                stopFetching(field);
////                return;
////            }
////            developerByFieldAndNation = gitHubApi.getDevelopersByFields(field);
////        } catch (IOException e) {
////            scheduledTaskRepository.updateScheduledTask(field,"FAILED",e.getMessage());
////            throw new RuntimeException(e);
////        }
////        JSONObject responseObject = JSON.parseObject(developerByFieldAndNation);
////            JSONArray itemsArray = responseObject.getJSONArray("items");
////            developers = new ArrayList<>();
////            for (Object o : itemsArray) {
////                JSONObject jsonObject = (JSONObject) o;
////                JSONObject owner = jsonObject.getJSONObject("owner");
////                String login = owner.getString("login");
////                String htmlUrl = owner.getString("html_url");
////                JSONObject userInfo = null;
////                String location = null;
////                double talentRank = 0;
////                String developerNation = null;
////                String assessment="";
////                String level = "";
////                try {
////                    userInfo = gitHubApi.getUserInfo(login);
////                    if (userInfo.getString("blog")!=null){
////                        log.info("用户{}的博客地址为：{}",login,userInfo.getString("blog"));
////                        assessment = chatGlmApi.doDevelopmentAssessment(userInfo.getString("blog"),userInfo.getString("bio"));
////                    }
////                    location = userInfo.getString("location");
//////                    talentRank = talentRankService.getTalentRankByUserName(login);
////
////                    RankResult talentRankByUserName = talentRankGraphQLService.getTalentRankByUserName(login);
////                    if (talentRankByUserName!=null){
////                        talentRank =100- talentRankByUserName.getPercentile();
////                        level=talentRankByUserName.getLevel();
////                    }
////                    // 使用String.format()方法格式化为两位小数的字符串
////                    String talentRankFormatted = String.format("%.2f", talentRank);
////
////                    // 如果你需要talentRank仍然是double类型，可以这样做：
////                    talentRank = Double.parseDouble(talentRankFormatted);
////                    developerNation = developerNationService.getDeveloperNation(login);
////                } catch (IOException e) {
////                    scheduledTaskRepository.updateScheduledTask(field,"FAILED",e.getMessage());
////                    throw new RuntimeException(e);
////                }
////                Developer developer = Developer.builder()
////                        .login(login)
////                        .field(field)
////                        .assessment(assessment)
////                        .location(location)
////                        .nation(developerNation)
////                        .htmlUrl(htmlUrl)
////                        .level(level)
////                        .talentRank(talentRank)
////                        .avatarUrl(userInfo.getString("avatar_url"))
////                        .type(userInfo.getString("type"))
////                        .company(userInfo.getString("company"))
////                        .location(userInfo.getString("location"))
////                        .blog(userInfo.getString("blog"))
////                        .email(userInfo.getString("email"))
////                        .name(userInfo.getString("name"))
////                        .type(userInfo.getString("type"))
////                        .bio(userInfo.getString("bio"))
////                        .publicRepos(userInfo.getIntValue("public_repos"))
////                        .publicGists(userInfo.getIntValue("public_gists"))
////                        .followers(userInfo.getIntValue("followers"))
////                        .following(userInfo.getIntValue("following"))
////                        .hireable(userInfo.getBoolean("hireable"))
////                        .twitterUsername(userInfo.getString("twitter_username"))
////                        .repositoryUrl(jsonObject.getString("html_url"))
////                        .build();
////                developers.add(developer);
////                log.info("用户名：{}  位置：{}  领域：{}  国籍：{} github地址：{}  人才指数：{}",
////                        login, location, field, developerNation, htmlUrl, talentRank);
////            }
////        if (!developers.isEmpty()) {
////            githubUserRepository.save(field, developers);
////        }
////    }
//
//
//    private boolean getDeveloperByFieldAndNation(String field, String nation) throws IOException {
//        //如果是中文
//        if (field.matches(".*[\\u4e00-\\u9fa5].*")) {
//            return talentRankGraphQLService.fetchUserByRepoDescription(field);
//        }
//        return talentRankGraphQLService.fetchUserByRepoTopic(field);
//    }
//
//
//    public void stopFetching(String field) {
////        ScheduledFuture<?> future = scheduledFutures.remove(field); // 从 Map 中获取并移除
//        githubLockRepository.removeFieldSearchLock(field);
//        scheduledTaskRepository.endScheduledTask(field,"COMPLETED");
//        if (scheduledFutureTime != null && !scheduledFutureTime.isCancelled()) {
//            scheduledFutureTime.cancel(true); // 取消定时任务
////            scheduledFuture.cancel(true); // 取消定时任务
////            githubUserRepository.updateFetchFlag(field);
//            log.info("已停止对 {}领域开发者的获取", field);
//        }
//    }
//
//
//
//    @PostConstruct
//    private void init() {
//        // 在服务启动后立即执行一次检查任务
//        checkScheduledTasks();
//        // 每60秒执行一次检查任务
////        scheduledFuture = scheduler.scheduleAtFixedRate(this::checkScheduledTasks, 0, 60, TimeUnit.SECONDS);
//    }
//
//    @Scheduled(fixedDelay = 60000) // 每60秒执行一次
//    public void checkScheduledTasks() {
//        List<ScheduledTask> tasks = scheduledTaskRepository.findAllByStatusIn(Arrays.asList("RUNNING", "FAILED"));
//        for (ScheduledTask task : tasks) {
//            switch (task.getStatus()) {
////                case "RUNNING":
////                    // 检查是否需要重新启动任务
////                    handleRunningTask(task);
////                    break;
//                case "FAILED":
//                    // 检查是否需要修复或重新启动任务
//                    handleFatalTask(task);
//                    break;
//                default:
//                    // 其他状态的处理
//                    break;
//            }
//        }
//    }
//
////    private void handleRunningTask(ScheduledTask task) {
////        // 实现检查逻辑，例如检查任务是否超时或是否需要重新启动
////        log.info("处理正在运行的任务: {}", task.getField());
////        // 假设需要重新启动任务
////        startFetching(task.getField(), null);
////    }
//
//    private void handleFatalTask(ScheduledTask task) {
//        // 实现错误处理逻辑，例如记录错误、尝试修复或通知管理员
//        log.error("处理失败任务: {}", task.getField());
//        // 假设需要重新启动任务
//        startFetching(task.getField(), null);
//    }
//}
@Service
@Slf4j
public class DeveloperFetcher {
    // 1. 添加常量定义
    private static final int CORE_POOL_SIZE = 5;
    private static final long TASK_INTERVAL = 30;
    private static final String TASK_STATUS_RUNNING = "RUNNING";
    private static final String TASK_STATUS_FAILED = "FAILED";
    private static final String TASK_STATUS_COMPLETED = "COMPLETED";

    // 2. 简化线程池管理，移除未使用的executorService
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(CORE_POOL_SIZE);

    // 3. 统一任务管理，使用一个ConcurrentHashMap
    private final ConcurrentHashMap<String, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();

    @Resource
    private IScheduledTaskRepository scheduledTaskRepository;
    @Resource
    private ITalentRankGraphQLService talentRankGraphQLService;
    @Resource
    private IGithubLockRepository githubLockRepository;

    // 4. 保持原有方法名但优化实现
    public void startFetchingTime(String field, String nation) {
        try {
            // 检查任务是否已存在
            if (scheduledTasks.containsKey(field)) {
                log.info("任务已存在: {}", field);
                return;
            }

            // 尝试获取锁
            if (!githubLockRepository.tryLockByField(field)) {
                log.info("获取锁失败，正在等待");
                return;
            }

            // 初始化任务状态
            initializeTask(field);

            // 创建定时任务
            ScheduledFuture<?> future = scheduler.scheduleAtFixedRate(() -> {
                try {
                    log.info("开始获取用户信息: {}", field);
                    boolean hasMore = getDeveloperByFieldAndNation(field, nation);
                    if (hasMore) {
                        log.info("获取用户信息成功: {}", field);
                    } else {
                        log.info("获取用户信息完成: {}", field);
                        if (githubLockRepository.isLockedByCurrentThread(field)) {
                            stopFetching(field);
                        }
                    }
                } catch (Exception e) {
                    handleTaskError(field, e);
                }
            }, 0, TASK_INTERVAL, TimeUnit.SECONDS);

            scheduledTasks.put(field, future);

        } catch (Exception e) {
            handleStartupError(field, e);
        }
    }

    // 5. 简化startFetching方法
    public void startFetching(String field, String nation) {
        try {
            initializeTask(field);
            boolean result = getDeveloperByFieldAndNation(field, nation);
            scheduledTaskRepository.updateScheduledTask(field,
                    result ? TASK_STATUS_RUNNING : TASK_STATUS_COMPLETED, null);
        } catch (Exception e) {
            handleTaskError(field, e);
        }
    }

    private boolean getDeveloperByFieldAndNation(String field, String nation) throws IOException {
        return field.matches(".*[\\u4e00-\\u9fa5].*")
                ? talentRankGraphQLService.fetchUserByRepoDescription(field)
                : talentRankGraphQLService.fetchUserByRepoTopic(field);
    }

    // 6. 优化stopFetching方法
    public void stopFetching(String field) {
        try {
            // 释放锁
            if (githubLockRepository.isLockedByCurrentThread(field)) {
                githubLockRepository.removeFieldSearchLock(field);
            }

            // 更新任务状态
            scheduledTaskRepository.endScheduledTask(field, TASK_STATUS_COMPLETED);

            // 取消并清理定时任务
            ScheduledFuture<?> future = scheduledTasks.remove(field);
            if (future != null && !future.isCancelled()) {
                future.cancel(true);
                log.info("已停止对 {}领域开发者的获取", field);
            }
        } catch (Exception e) {
            log.error("停止任务失败: {}", field, e);
        }
    }

    // 7. 添加辅助方法
    private void initializeTask(String field) {
        if (!scheduledTaskRepository.checkScheduledTaskByField(field)) {
            scheduledTaskRepository.insertScheduledTask(field, TASK_STATUS_RUNNING);
        }
        scheduledTaskRepository.updateScheduledTaskRUNNING(field, TASK_STATUS_RUNNING);
    }

    private void handleTaskError(String field, Exception e) {
        log.error("任务执行失败: {}", field, e);
        scheduledTaskRepository.updateScheduledTask(field, TASK_STATUS_FAILED, e.getMessage());
        stopFetching(field);
    }

    private void handleStartupError(String field, Exception e) {
        log.error("启动任务失败: {}", field, e);
        githubLockRepository.removeFieldSearchLock(field);
        scheduledTaskRepository.updateScheduledTask(field, TASK_STATUS_FAILED, e.getMessage());
    }

    // 8. 优化定时检查任务
    @PostConstruct
    private void init() {
        checkScheduledTasks();
    }

    @Scheduled(fixedDelay = 60000)
    public void checkScheduledTasks() {
        try {
            if (!githubLockRepository.checkScheduledTasksLock()){
                return;
            }
            List<ScheduledTask> failedTasks = scheduledTaskRepository.findAllByStatusIn(
                    Collections.singletonList(TASK_STATUS_FAILED));

            for (ScheduledTask task : failedTasks) {
                handleFatalTask(task);
            }
        }
        catch (Exception e) {
            log.error("检查任务状态失败", e);
        }finally {
            if (githubLockRepository.isLockedByCurrentThreadByTasks()){
                githubLockRepository.removeScheduledTasksLock();
            }
        }
    }

    private void handleFatalTask(ScheduledTask task) {
        log.error("处理失败任务: {}", task.getField());
        startFetching(task.getField(), null);
    }

    // 9. 添加优雅关闭
    @PreDestroy
    public void shutdown() {
        try {
            scheduler.shutdown();
            if (!scheduler.awaitTermination(60, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }

            // 清理所有任务
            for (String field : scheduledTasks.keySet()) {
                stopFetching(field);
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
