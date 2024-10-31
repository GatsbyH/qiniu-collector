package com.sdwu.domain.github.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.sdwu.domain.github.model.entity.Developer;
import com.sdwu.domain.github.model.valobj.DevelopersByFieldReqVo;
import com.sdwu.domain.github.repository.IGithubUserRepository;
import com.sdwu.domain.github.repository.IScheduledTaskRepository;
import com.sdwu.types.model.PageResult;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class DeveloperFieldImplService implements IDeveloperFieldService {
    @Resource
    private IGitHubApi gitHubApi;
    @Resource
    private ITalentRankService talentRankService;
    @Resource
    private IDeveloperNationService developerNationService;

    @Resource
    private IGithubUserRepository githubUserRepository;

    @Resource
    private DeveloperFetcher developerFetcher;
    @Resource
    private IScheduledTaskRepository scheduledTaskRepository;

    @Override
    public List<Developer> getDeveloperByFieldAndNation(String field, String nation) throws IOException {
//        String developerByFieldAndNation = gitHubApi.getDeveloperByFieldAndNation(field, nation);
        List<Developer> developersByFields = githubUserRepository.getDevelopersByFields(field);
        if (developersByFields.size()>0){
            return developersByFields;
        }
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
        //使用stream流将developers按照人才指数降序排序
        developers.sort((o1, o2) -> Double.compare(o2.getTalentRank(), o1.getTalentRank()));
        developerFetcher.startFetching(field, nation);
        return developers;
    }

    @Override
    public Boolean startGetDeveloperByField(String field) {
        String nation = "all";
        try {
            developerFetcher.startFetching(field, nation);
        } catch (Exception e) {
            scheduledTaskRepository.updateScheduledTask(field,"FALED",e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public Boolean stopGetDeveloperByField(String field) {
        String nation = "all";
        try {
            developerFetcher.stopFetching(field);
        } catch (Exception e) {
            scheduledTaskRepository.updateScheduledTask(field,"FALED",e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public PageResult<Developer> getDevelopersByFieldsPage(DevelopersByFieldReqVo developersByFieldReqVo) {
        PageResult<Developer> developersByFields = githubUserRepository.getDevelopersByFieldsPage(developersByFieldReqVo);
        return developersByFields;
    }
}
