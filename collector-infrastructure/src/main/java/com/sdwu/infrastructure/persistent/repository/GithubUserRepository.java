package com.sdwu.infrastructure.persistent.repository;

import com.sdwu.domain.github.model.entity.Developer;
import com.sdwu.domain.github.model.valobj.DevelopersByFieldReqVo;
import com.sdwu.domain.github.repository.IGithubUserRepository;
import com.sdwu.infrastructure.persistent.po.DeveloperPO;
import com.sdwu.infrastructure.persistent.redis.IRedisService;
import com.sdwu.types.model.PageResult;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

import static com.sdwu.types.common.CacheConstants.*;

@Repository
public class GithubUserRepository implements IGithubUserRepository {
    @Resource
    private IRedisService redisService;

    @Override
    public void save(String field,List<Developer> developers) {
        developers.forEach(developer -> redisService.addUser(GITHUB_USER_INFO_KEY+field, DeveloperPO.toPO(developer), developer.getTalentRank()));
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
        List<DeveloperPO> developers = redisService.getUsersByRankDesc(GITHUB_USER_INFO_KEY+developersByFieldReqVo.getField(), developersByFieldReqVo.getPageNum(), developersByFieldReqVo.getPageSize());
        PageResult<Developer> developerPageResult = new PageResult<>(developers.stream().map(DeveloperPO::toDeveloper).collect(Collectors.toList()), (long) developers.size());
        return developerPageResult;
    }
}
