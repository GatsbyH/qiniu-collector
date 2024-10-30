package com.sdwu.domain.github.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.sdwu.domain.github.model.entity.Developer;
import com.sdwu.domain.github.repository.IGithubUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class DeveloperFetcher {
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private String currentField;
    private String currentNation;
    private boolean isFetching = false;
    @Resource
    private IGitHubApi gitHubApi;
    @Resource
    private ITalentRankService talentRankService;
    @Resource
    private IDeveloperNationService developerNationService;

    @Resource
    private IGithubUserRepository githubUserRepository;


    public void startFetching(String field, String nation) {
        Boolean isFetching = githubUserRepository.getFetchFlag(field);

        // 如果已经在获取中，则不再启动新任务
        if (isFetching) {
            return;
        }
        currentField = field;
        currentNation = nation;
        githubUserRepository.updateFetchFlag(field);

        scheduler.scheduleAtFixedRate(() -> {
            try {
                getDeveloperByFieldAndNation(currentField, currentNation);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, 0, 2, TimeUnit.SECONDS);
    }

    private void getDeveloperByFieldAndNation(String field, String nation) throws IOException {
        String developerByFieldAndNation = gitHubApi.getDevelopersByFields(field);
        JSONObject responseObject = JSON.parseObject(developerByFieldAndNation);
        JSONArray itemsArray = responseObject.getJSONArray("items");
        List<Developer> developers = new ArrayList<>();
        for (Object o : itemsArray) {
            JSONObject jsonObject = (JSONObject) o;
            JSONObject owner = jsonObject.getJSONObject("owner");
            String login = owner.getString("login");
            String htmlUrl = owner.getString("html_url");
            JSONObject userInfo = gitHubApi.getUserInfo(login);
            String location = userInfo.getString("location");
            double talentRank = talentRankService.getTalentRankByUserName(login);
            String developerNation = developerNationService.getDeveloperNation(login);
            Developer developer = Developer.builder()
                    .login(login)
                    .field(field)
                    .location(location)
                    .nation(developerNation)
                    .htmlUrl(htmlUrl)
                    .talentRank(talentRank)
                    .build();
            developers.add(developer);
            System.out.println("用户名："+login+"  位置："+location+"  领域："+field+"  国籍："+developerNation+" github地址："+ htmlUrl+"  人才指数："+talentRank);
        }
        githubUserRepository.save(field,developers);
    }

    public void stopFetching(String field) {
        if (isFetching) {
            scheduler.shutdown();
            githubUserRepository.updateFetchFlag(field);

            log.info("Fetching stopped.");
        }
    }
}
