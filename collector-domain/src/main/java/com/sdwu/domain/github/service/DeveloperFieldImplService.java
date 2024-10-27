package com.sdwu.domain.github.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.sdwu.domain.github.model.entity.Developer;
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
    @Override
    public List<Developer> getDeveloperByFieldAndNation(String field, String nation) throws IOException {
        String developerByFieldAndNation = gitHubApi.getDeveloperByFieldAndNation(field, nation);
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
            System.out.println("用户名："+login+"  位置："+location+"  领域："+field+"  国籍："+nation+" github地址："+ htmlUrl+"  人才指数："+talentRank);
        }
        //使用stream流将developers按照人才指数降序排序
        developers.sort((o1, o2) -> Double.compare(o2.getTalentRank(), o1.getTalentRank()));
        return developers;
    }
}
