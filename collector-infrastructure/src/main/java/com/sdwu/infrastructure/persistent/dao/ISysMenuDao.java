package com.sdwu.infrastructure.persistent.dao;

import com.sdwu.infrastructure.persistent.po.SysMenuPO;
import com.sdwu.infrastructure.persistent.utils.BaseMapperX;
import com.sdwu.infrastructure.persistent.utils.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;
import java.util.Set;
@Mapper
public interface ISysMenuDao extends BaseMapperX<SysMenuPO> {


    List<SysMenuPO> selectMenuByMenuIds(@Param("menuIds")Set<Long> menuIds);

    List<SysMenuPO> selectMenuTreeAll();

    List<SysMenuPO> selectMenuTreeByUserId(Long userId);

    List<Long> selectMenuListByRoleId(@Param("roleId") Long roleId, @Param("menuCheckStrictly") boolean menuCheckStrictly);
    default List<SysMenuPO> findMenuByName(String menuName){
        return selectList(SysMenuPO::getMenuName, menuName);
    };

    default int insertMenu(SysMenuPO sysMenuPO){
        return insert(sysMenuPO);
    };

    default int updateMenu(SysMenuPO sysMenuPO){
        return updateById(sysMenuPO);
    };

    SysMenuPO selectMenuById(Long menuId);


    default Long hasChildrenByMenuId(Long menuId){
        return selectCount(SysMenuPO::getParentId, menuId);
    };

    int deleteMenuById(Long menuId);
}
