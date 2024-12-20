package com.sdwu.domain.github.repository;

import com.sdwu.domain.github.model.entity.Developer;
import com.sdwu.domain.github.model.valobj.*;
import com.sdwu.types.model.PageResult;

import java.util.List;

public interface IGithubUserRepository {
    void save(String field,List<Developer> developers);
    void saveSingle(String field,Developer developers);

    List<Developer> getDevelopersByFields(String field);

    Integer getGitHubPageByField(String field);

    void updateGitHubPageByField(String field);

    Boolean getFetchFlag(String field);

    void updateFetchFlag(String field);

    PageResult<Developer> getDevelopersByFieldsPage(DevelopersByFieldReqVo developersByFieldReqVo);

    String getGitHubAfterPageByTopic(String topic);

    void setGitHubAfterPageByTopic(String topic, String after);

    boolean getFieldSearchLock(String topic);

    void removeFieldSearchLock(String topic);

    boolean checkLoginExist(String login,String topic);

    void addLogin(String login,String topic);

    void countDeveloperEmptyCount(String topic);

    int getDeveloperEmptyCount(String topic);

    List<String> getDeveloperNationOptionsByField(String field);

    // 获取用户统计信息缓存
    DevelopeVo getUserStatsCache(String username);

    // 保存用户统计信息缓存
    void saveUserStatsCache(String username, DevelopeVo stats);

    void saveSingleByNationAndField(String field, String developerNation, Developer developer);

    RankResult getTalentRankCacheByUserName(String username);

    void setTalentRankCacheByUserName(String username, RankResult talentRank);

    void saveBatchByNationAndField(String field, String nation, List<Developer> devs);

    /**
     * 获取用户SVG统计图缓存
     * @param username 用户名
     * @param theme 主题
     * @return 缓存的SVG内容，如果没有缓存则返回null
     */
    String getSvgCache(String username, String theme);

    /**
     * 保存用户SVG统计图缓存
     * @param username 用户名
     * @param theme 主题
     * @param svg SVG内容
     */
    void saveSvgCache(String username, String theme, String svg);



     /**
     * 获取用户的编程语言统计缓存
     * @param username GitHub用户名
     * @return 语言统计列表,如果没有缓存则返回null
     */
    List<LanguageCountRespVo> getLanguageStatsCache(String username);

    /**
     * 保存用户的编程语言统计缓存
     * @param username GitHub用户名
     * @param stats 要缓存的语言统计数据
     */
    void saveLanguageStatsCache(String username, List<LanguageCountRespVo> stats);

    /**
     * 获取TalentRank缓存
     */
    Double getTalentRankScoreCache(String username);

    /**
     * 保存TalentRank缓存
     */
    void saveTalentRankScoreCache(String username, Double score);

    /**
     * 获取开发者语言统计缓存
     */
    List<LanguageCountRespVo> getDeveloperLanguageCache(String username);

    /**
     * 保存开发者语言统计缓存
     */
    void saveDeveloperLanguageCache(String username, List<LanguageCountRespVo> languages);

    /**
     * 获取开发者国家信息缓存
     * @param username GitHub用户名
     * @return 缓存的国家信息，如果没有缓存则返回null
     */
    String getDeveloperNationCache(String username);

    /**
     * 保存开发者国家信息缓存
     * @param username GitHub用户名
     * @param nation 国家信息
     */
    void saveDeveloperNationCache(String username, String nation);

    /**
     * 获取开发者技术能力评估缓存
     * @param username GitHub用户名
     * @return 缓存的技术能力评估，如果没有缓存则返回null
     */
    String getTechnicalAbilityCache(String username);

    /**
     * 保存开发者技术能力评估缓存
     * @param username GitHub用户名
     * @param ability 技术能力评估
     */
    void saveTechnicalAbilityCache(String username, String ability);

    DevelopeVo getTalentRankCache(String username);

    void saveTalentRankCacheCache(String username, Double totalScore);
}
