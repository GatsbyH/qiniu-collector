package com.sdwu.infrastructure.persistent.dao;

import com.sdwu.domain.sysuser.model.entity.SystemUser;
import com.sdwu.infrastructure.persistent.po.SysUserRolePO;
import com.sdwu.infrastructure.persistent.po.SystemUserPO;
import com.sdwu.infrastructure.persistent.utils.BaseMapperX;
import com.sdwu.infrastructure.persistent.utils.LambdaQueryWrapperX;
import com.sdwu.types.model.PageResult;

import java.util.*;

public interface ISystemUserDao extends BaseMapperX<SystemUserPO> {
    default SystemUserPO  findByUserName(String username){
        return selectOne(new LambdaQueryWrapperX<SystemUserPO>().eq(SystemUserPO::getUserName, username));
    };

    default PageResult<SystemUserPO> selcetUserPage(SystemUser user){

        // 从params中获取beginTime和endTime，如果不存在则返回空字符串
        String beginTime = Objects.toString(user.getParams().get("beginTime"), null);
        String endTime = Objects.toString(user.getParams().get("endTime"), null);

        return selectPage(user, new LambdaQueryWrapperX<SystemUserPO>()
                .eqIfPresent(SystemUserPO::getUserName, user.getUserName())
                .eqIfPresent(SystemUserPO::getPhoneNumber, user.getPhoneNumber())
                .eqIfPresent(SystemUserPO::getStatus, user.getStatus())
                .eqIfPresent(SystemUserPO::getDeptId, user.getDeptId())
                .betweenIfPresent(SystemUserPO::getCreateTime, beginTime, endTime)
        );
    };

   default Integer deleteUserByIds(Long[] userIds){
       return deleteBatchIds(Arrays.asList(userIds));
   };


    public int deleteRoleByIds(Long[] roleIds);


}
