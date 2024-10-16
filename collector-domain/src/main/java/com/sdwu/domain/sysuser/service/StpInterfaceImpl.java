package com.sdwu.domain.sysuser.service;

import cn.dev33.satoken.stp.StpInterface;
import cn.dev33.satoken.stp.StpUtil;
import com.sdwu.domain.sysuser.model.entity.SystemUser;
import com.sdwu.domain.sysuser.repository.ISysMenuPermRepository;
import com.sdwu.domain.sysuser.repository.ISysRoleRepository;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 自定义权限加载接口实现类
 */
@Component    // 保证此类被 SpringBoot 扫描，完成 Sa-Token 的自定义权限验证扩展
public class StpInterfaceImpl implements StpInterface {
    @Resource
    private ISysRoleRepository sysRoleRepository;


    @Resource
    private ISysMenuPermRepository sysMenuPermRepository;
    /**
     * 返回一个账号所拥有的权限码集合
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        SystemUser user = (SystemUser) StpUtil.getSession().get("user");
        if (user.isAdmin(user.getUserId())){
            List<String> permList = new ArrayList<>();
            permList.add("*:*:*");
            return permList;
        }
        return  sysMenuPermRepository.findMenuPermListByUserId(user.getUserId());
    }

    /**
     * 返回一个账号所拥有的角色标识集合 (权限与角色可分开校验)
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        SystemUser user = (SystemUser) StpUtil.getSession().get("user");
        return  sysRoleRepository.findRoleListByUserId(user.getUserId());
    }

}
