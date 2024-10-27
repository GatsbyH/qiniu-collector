package com.sdwu.domain.github.service;

import com.alibaba.fastjson2.JSONObject;
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
    @Override
    public String getDeveloperNation(String username) throws IOException {
        JSONObject userInfo = gitHubApi.getUserInfo(username);
        if (userInfo.get("location") != null){
//            String country = moonShotApi.getCountry(jsonObject.getString("location"));
//            return country;
            return userInfo.getString("location");
        }
        List<String> followersLocations = gitHubApi.getFollowersByUserName(username);
        List<String> followingByUserName = gitHubApi.getFollowingByUserName(username);

        //合成一个集合
        followersLocations.addAll(followingByUserName);

        String country = moonShotApi.getCountryByUserRelations(followersLocations);
//        log.info("followersLocations: " + followersLocations);
        return country;
    }
}
