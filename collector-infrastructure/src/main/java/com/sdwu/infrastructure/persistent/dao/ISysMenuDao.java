package com.sdwu.infrastructure.persistent.dao;

import com.sdwu.infrastructure.persistent.po.SysMenuPO;
import com.sdwu.infrastructure.persistent.utils.BaseMapperX;
import com.sdwu.infrastructure.persistent.utils.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Set;
@Mapper
public interface ISysMenuDao extends BaseMapperX<SysMenuPO> {


    List<SysMenuPO> selectMenuByMenuIds(Set<Long> menuIds);

    List<SysMenuPO> selectMenuTreeAll();

    List<SysMenuPO> selectMenuTreeByUserId(Long userId);
}
