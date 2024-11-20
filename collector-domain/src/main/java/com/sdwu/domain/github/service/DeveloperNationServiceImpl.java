package com.sdwu.domain.github.service;

import com.alibaba.fastjson2.JSONObject;
import com.sdwu.domain.censorship.factory.DefaultLogicFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        String userLocation = userInfo.getString("location");
        String firstResult=null;
        if (StringUtils.isNotBlank(userLocation)){
            firstResult = chatGlmApi.getCountry(userLocation);
            if (isHighConfidence(firstResult)) {
                return formatResult(firstResult);
            }
        }
//            String country = moonShotApi.getCountry(userInfo.getString("location"));
//            String country = chatGlmApi.getCountry(userInfo.getString("location"));

//            if (country != null && country.contains("N/A")) {
//                return "N/A";
//            }
//            return country;
//            return userInfo.getString("location");

        List<String> followersLocations = gitHubApi.getFollowersByUserName(username);

        List<String> followingByUserName = gitHubApi.getFollowingByUserName(username);

        //合成一个集合
        followersLocations.addAll(followingByUserName);
        Map<String, Integer> locationFrequency = this.analyzeLocationFrequency(followersLocations);
        String secondResult = chatGlmApi.predictCountryWithNetworkContext(userLocation, locationFrequency);
        if (isHighConfidence(secondResult)) {
            return formatResult(secondResult);
        }

        return formatResult(chatGlmApi.detailedAnalysisPrediction(userLocation, followersLocations, firstResult, secondResult));


//        String country = moonShotApi.getCountryByUserRelations(followersLocations);
//        String country = chatGlmApi.getCountryByUserRelations(followersLocations);
//        log.info("followersLocations: " + followersLocations);
//        if (country != null && country.contains("N/A")) {
//            return "N/A";
//        }
//        return country;
    }

    /**
     * 检查置信度是否足够高
     */
    private boolean isHighConfidence(String result) {
        if (result == null || result.contains("N/A")) {
            return false;
        }

        try {
            String[] parts = result.split("\\s+");
            if (parts.length >= 2) {
                String confidenceStr = parts[parts.length - 1].replace("%", "");
                int confidence = Integer.parseInt(confidenceStr);
                return confidence >= 80; // 80%以上认为是高置信度
            }
        } catch (Exception e) {
            log.warn("Failed to parse confidence: {}", result);
        }
        return false;
    }

    /**
     * 分析位置频率
     */
    private Map<String, Integer> analyzeLocationFrequency(List<String> locations) {
        return locations.stream()
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.groupingBy(
                        location -> location,
                        Collectors.collectingAndThen(Collectors.counting(), Long::intValue)
                ));
    }
    /**
     * 格式化结果，确保只返回"国家 置信度%"格式
     */
    private String formatResult(String result) {
        if (result == null || result.contains("N/A")) {
            return "N/A";
        }

        try {
            // 提取最后一个空格前的所有内容作为国家名，最后一个空格后的内容作为置信度
            int lastSpaceIndex = result.lastIndexOf(" ");
            if (lastSpaceIndex > 0) {
                String country = result.substring(0, lastSpaceIndex).trim();
                String confidence = result.substring(lastSpaceIndex + 1).trim();

                // 移除置信度中的百分号
                confidence = confidence.replace("%", "");

                // 如果置信度为大于等于80，直接返回国家名

                if (80<=(Integer.valueOf(confidence))) {
                    return country;
                }

                // 其他情况返回国家名和置信度
                return country + " " + confidence + "%";
            } else {
                // 如果没有空格，说明可能是直接返回的国家名（100%确定的情况）
                return result.trim();
            }
        } catch (Exception e) {
            log.warn("结果格式化失败: {}", result);
        }
        return "N/A";
    }

}
