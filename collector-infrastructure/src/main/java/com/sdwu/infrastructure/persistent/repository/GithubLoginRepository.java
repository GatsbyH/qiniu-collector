package com.sdwu.infrastructure.persistent.repository;

import com.sdwu.domain.github.model.entity.GithubUser;
import com.sdwu.domain.github.repository.IGithubLoginRepository;
import com.sdwu.infrastructure.persistent.dao.IGithubUserDao;
import com.sdwu.infrastructure.persistent.po.GithubUserPO;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public class GithubLoginRepository implements IGithubLoginRepository {

    @Resource
    private IGithubUserDao githubUserDao;

    @Override
    public GithubUser findByUuid(String uuid) {
        GithubUserPO po = githubUserDao.selectByUuid(uuid);
        return GithubUserPO.convertToDomain(po);
    }

    @Override
    public Long save(GithubUser githubUser) {
        GithubUserPO po = GithubUserPO.convertFromDomain(githubUser);
        if (githubUser.getId() == null) {
            githubUserDao.insertUser(po);
        } else {
            githubUserDao.updateUser(po);
        }
        return po.getId();
    }

    @Override
    public GithubUser findById(Long id) {
        GithubUserPO po = githubUserDao.selectById(id);
        return GithubUserPO.convertToDomain(po);
    }
}
