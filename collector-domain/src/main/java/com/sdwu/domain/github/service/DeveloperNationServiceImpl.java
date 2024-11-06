package com.sdwu.domain.github.service;

import com.alibaba.fastjson2.JSONObject;
import com.sdwu.domain.censorship.factory.DefaultLogicFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class DeveloperNationServiceImpl implements IDeveloperNationService {
    @Resource
    private IGitHubApi gitHubApi;
    @Resource
    private IMoonShotApi moonShotApi;
    @Resource
    private IChatGlmApi chatGlmApi;

    @Override
    public String getDeveloperNation(String username) throws IOException {
        JSONObject userInfo = gitHubApi.getUserInfo(username);
        if (userInfo.get("location") != null){
//            String country = moonShotApi.getCountry(userInfo.getString("location"));
            String country = chatGlmApi.getCountry(userInfo.getString("location"));
            if (country != null && country.contains("N/A")) {
                return "N/A";
            }
            return country;
//            return userInfo.getString("location");
        }
        List<String> followersLocations = gitHubApi.getFollowersByUserName(username);
        List<String> followingByUserName = gitHubApi.getFollowingByUserName(username);

        //合成一个集合
        followersLocations.addAll(followingByUserName);

//        String country = moonShotApi.getCountryByUserRelations(followersLocations);
        String country = chatGlmApi.getCountryByUserRelations(followersLocations);
//        log.info("followersLocations: " + followersLocations);
        if (country != null && country.contains("N/A")) {
            return "N/A";
        }
        return country;
    }
}
