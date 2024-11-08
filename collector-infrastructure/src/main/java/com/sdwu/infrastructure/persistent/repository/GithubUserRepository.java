package com.sdwu.infrastructure.persistent.repository;

import com.sdwu.domain.github.model.entity.Developer;
import com.sdwu.domain.github.model.valobj.DevelopersByFieldReqVo;
import com.sdwu.domain.github.repository.IGithubUserRepository;
import com.sdwu.infrastructure.persistent.po.DeveloperPO;
import com.sdwu.infrastructure.persistent.redis.IRedisService;
import com.sdwu.types.model.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.sdwu.types.common.CacheConstants.*;

@Repository
@Slf4j
public class GithubUserRepository implements IGithubUserRepository {
    @Resource
    private IRedisService redisService;

    @Override
    public void save(String field,List<Developer> developers) {
        developers.forEach(developer -> redisService.addUser(GITHUB_USER_INFO_KEY+field, DeveloperPO.toPO(developer), developer.getTalentRank()));
    }

    @Override
    public void saveSingle(String field, Developer developers) {
        DeveloperPO developerPO = DeveloperPO.toPO(developers);
        redisService.addUser(GITHUB_USER_INFO_KEY+field, developerPO, developerPO.getTalentRank());
    }

    @Override
    public List<Developer> getDevelopersByFields(String field) {
        List<DeveloperPO> developers = redisService.getUsersByRankDesc(GITHUB_USER_INFO_KEY+field, 0, 100);
        return developers.stream().map(DeveloperPO::toDeveloper).collect(Collectors.toList());
    }

    @Override
    public Integer getGitHubPageByField(String field) {
        Object value = redisService.getValue(GITHUB_PAGE + field);
        if (value != null)
            return Integer.parseInt(value.toString());
        return 1;
    }

    @Override
    public void updateGitHubPageByField(String field) {
        redisService.setValue(GITHUB_PAGE + field, getGitHubPageByField(field)+1);
    }

    @Override
    public Boolean getFetchFlag(String field) {
        Object value = redisService.getValue(FETCHING_KEY + field);
        if (value != null)
            return Boolean.parseBoolean(value.toString());
        return false;
    }

    @Override
    public void updateFetchFlag(String field) {
        redisService.setValue(FETCHING_KEY + field, !getFetchFlag(field));
    }

    @Override
    public PageResult<Developer> getDevelopersByFieldsPage(DevelopersByFieldReqVo developersByFieldReqVo) {


//        List<Developer> filteredDevelopers = developers.stream()
//                .filter(po -> po.getNation() != null && po.getNation().matches(".*" + Pattern.quote(developersByFieldReqVo.getNation()) + ".*"))
//                .map(DeveloperPO::toDeveloper)
//                .collect(Collectors.toList());
//        PageResult<Developer> developerPageResult = new PageResult<>(filteredDevelopers, (long) size);
//        PageResult<Developer> developerPageResult = new PageResult<>(developers.stream().map(DeveloperPO::toDeveloper).collect(Collectors.toList()), (long) size);
//        return developerPageResult;



        if (developersByFieldReqVo.getNation() == null || developersByFieldReqVo.getNation().isEmpty()) {
            List<DeveloperPO> developers = redisService.getUsersByRankDesc(GITHUB_USER_INFO_KEY+developersByFieldReqVo.getField(), developersByFieldReqVo.getPageNum(), developersByFieldReqVo.getPageSize());
            Integer size = redisService.getUsersByRankSize(GITHUB_USER_INFO_KEY + developersByFieldReqVo.getField());
            // 如果nation为空，则不进行过滤，直接映射并返回结果
            List<Developer> filteredDevelopers = developers.stream()
                    .map(DeveloperPO::toDeveloper)
                    .collect(Collectors.toList());
            PageResult<Developer> developerPageResult = new PageResult<>(filteredDevelopers, (long) size);
            return developerPageResult;
        }


        Integer usersSizeByNation = redisService.getUsersSizeByRankAndNation(GITHUB_USER_INFO_KEY + developersByFieldReqVo.getField(), developersByFieldReqVo.getField(), developersByFieldReqVo.getNation());

        List<DeveloperPO> filteredDevelopersByNation = redisService.getUsersByRankDescAndNation(GITHUB_USER_INFO_KEY + developersByFieldReqVo.getField(), developersByFieldReqVo.getField(), developersByFieldReqVo.getNation(), developersByFieldReqVo.getPageNum(), developersByFieldReqVo.getPageSize());
        List<Developer> filteredDevelopers = filteredDevelopersByNation.stream()
                    .map(DeveloperPO::toDeveloper)
                    .collect(Collectors.toList());
        PageResult<Developer> developerPageResult = new PageResult<>(filteredDevelopers, (long) usersSizeByNation);
        return developerPageResult;

    }

    @Override
    public String getGitHubAfterPageByTopic(String topic) {
        Object value = redisService.getValue(GITHUB_PAGE + topic);
        if (value != null)
            return value.toString();
        return null;
    }

    @Override
    public void setGitHubAfterPageByTopic(String topic, String after) {
        redisService.setValue(GITHUB_PAGE + topic, after);
    }

    @Override
    public boolean getFieldSearchLock(String topic) {
        return redisService.setNx(GITHUB_NX+topic, 10, java.util.concurrent.TimeUnit.MINUTES);
    }

    @Override
    public void removeFieldSearchLock(String topic) {
        redisService.remove(GITHUB_NX+topic);
    }

    @Override
    public boolean checkLoginExist(String login,String topic) {
        return redisService.isExists(GITHUB_USER_LOGIN+topic+":"+login);
    }


    @Override
    public void addLogin(String login,String topic) {
        redisService.setValue(GITHUB_USER_LOGIN+topic+":"+login, login);
    }

    @Override
    public void countDeveloperEmptyCount(String topic) {
        redisService.incr(GITHUB_EMPTY_COUNT+topic);
    }

    @Override
    public int getDeveloperEmptyCount(String topic) {
        Object value = redisService.getValue(GITHUB_EMPTY_COUNT + topic);
        if (value == null) {
            return 0; // 或者根据业务需求返回默认值
        }
        if (value instanceof Long) {
            return ((Long) value).intValue();
        } else if (value instanceof Integer) {
            return (Integer) value;
        }else {
            log.error("getDeveloperEmptyCount: value is not Long or Integer");
            return 0;
        }

    }

    @Override
    public List<String> getDeveloperNationOptionsByField(String field) {
        return redisService.getDeveloperNationOptionsByField(GITHUB_USER_INFO_KEY+field);
    }

}
