package com.sdwu.infrastructure.persistent.dao;

import com.sdwu.infrastructure.persistent.po.GithubUserPO;
import com.sdwu.infrastructure.persistent.utils.BaseMapperX;
import com.sdwu.infrastructure.persistent.utils.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IGithubUserDao extends BaseMapperX<GithubUserPO> {
    
    default GithubUserPO selectByUuid(String uuid) {
        return selectOne(new LambdaQueryWrapperX<GithubUserPO>()
                .eq(GithubUserPO::getUuid, uuid)
                .eq(GithubUserPO::getDeleted, 0));
    }
    
    default GithubUserPO selectById(Long id) {
        return selectOne(new LambdaQueryWrapperX<GithubUserPO>()
                .eq(GithubUserPO::getId, id)
                .eq(GithubUserPO::getDeleted, 0));
    }
    
    default int insertUser(GithubUserPO githubUser) {
        return insert(githubUser);
    }
    
    default int updateUser(GithubUserPO githubUser) {
        return update(githubUser, new LambdaQueryWrapperX<GithubUserPO>()
                .eq(GithubUserPO::getId, githubUser.getId()));
    }
}
