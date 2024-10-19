package com.sdwu.infrastructure.persistent.po;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sdwu.domain.sysuser.model.entity.SysMenu;
import com.sdwu.infrastructure.persistent.utils.BaseDO;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 菜单权限表 sys_menu
 *
 * @author
 */
@TableName("sys_menu")
public class SysMenuPO extends BaseDO implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 菜单ID */
    @TableId(value = "menu_id", type = IdType.AUTO) // 主键策略
    private Long menuId;

    /** 菜单名称 */
    @TableField(value = "menu_name") // 映射到数据库字段
    private String menuName;

    /** 父菜单名称 */
    @TableField(exist = false)
    private String parentName;

    /** 父菜单ID */
    @TableField(value = "parent_id") // 映射到数据库字段
    private Long parentId;

    /** 显示顺序 */
    @TableField(value = "order_num") // 映射到数据库字段
    private Integer orderNum;

    /** 路由地址 */
    @TableField(value = "path") // 映射到数据库字段
    private String path;

    /** 组件路径 */
    @TableField(value = "component") // 映射到数据库字段
    private String component;

    /** 路由参数 */
    @TableField(value = "query") // 映射到数据库字段
    private String query;

    /** 路由名称，默认和路由地址相同的驼峰格式（注意：因为vue3版本的router会删除名称相同路由，为避免名字的冲突，特殊情况可以自定义） */
    @TableField(exist = false)
    private String routeName;

    /** 是否为外链（0是 1否） */
    @TableField(value = "is_frame") // 映射到数据库字段
    private String isFrame;

    /** 是否缓存（0缓存 1不缓存） */
    @TableField(value = "is_cache") // 映射到数据库字段
    private String isCache;

    /** 类型（M目录 C菜单 F按钮） */
    @TableField(value = "menu_type") // 映射到数据库字段
    private String menuType;

    /** 显示状态（0显示 1隐藏） */
    @TableField(value = "visible") // 映射到数据库字段
    private String visible;

    /** 菜单状态（0正常 1停用） */
    @TableField(value = "status") // 映射到数据库字段
    private String status;

    /** 权限字符串 */
    @Size(min = 0, max = 100, message = "权限标识长度不能超过100个字符")
    @TableField(value = "perms") // 映射到数据库字段
    private String perms;

    /** 菜单图标 */
    @TableField(value = "icon") // 映射到数据库字段
    private String icon;


    /** 子菜单 */
    @TableField(exist = false)
    private List<SysMenuPO> children = new ArrayList<SysMenuPO>();

    public static SysMenuPO convertToPO(SysMenu menu) {
        SysMenuPO po = new SysMenuPO();
        po.setMenuId(menu.getMenuId());
        po.setMenuName(menu.getMenuName());
        po.setParentId(menu.getParentId());
        po.setOrderNum(menu.getOrderNum());
        po.setPath(menu.getPath());
        po.setComponent(menu.getComponent());
        po.setQuery(menu.getQuery());
        po.setRouteName(menu.getRouteName());
        po.setIsFrame(menu.getIsFrame());
        po.setIsCache(menu.getIsCache());
        po.setMenuType(menu.getMenuType());
        po.setVisible(menu.getVisible());
        po.setStatus(menu.getStatus());
        po.setPerms(menu.getPerms());
        po.setIcon(menu.getIcon());
        return po;
    }

    public static SysMenu convertToDo(SysMenuPO sysMenuPO) {
        SysMenu menu = new SysMenu();
        menu.setMenuId(sysMenuPO.getMenuId());
        menu.setMenuName(sysMenuPO.getMenuName());
        menu.setParentId(sysMenuPO.getParentId());
        menu.setOrderNum(sysMenuPO.getOrderNum());
        menu.setPath(sysMenuPO.getPath());
        menu.setComponent(sysMenuPO.getComponent());
        menu.setQuery(sysMenuPO.getQuery());
        menu.setRouteName(sysMenuPO.getRouteName());
        menu.setIsFrame(sysMenuPO.getIsFrame());
        menu.setIsCache(sysMenuPO.getIsCache());
        menu.setMenuType(sysMenuPO.getMenuType());
        menu.setVisible(sysMenuPO.getVisible());
        menu.setStatus(sysMenuPO.getStatus());
        menu.setPerms(sysMenuPO.getPerms());
        menu.setIcon(sysMenuPO.getIcon());
        return menu;
    }


    public Long getMenuId()
    {
        return menuId;
    }

    public void setMenuId(Long menuId)
    {
        this.menuId = menuId;
    }

    @NotBlank(message = "菜单名称不能为空")
    @Size(min = 0, max = 50, message = "菜单名称长度不能超过50个字符")
    public String getMenuName()
    {
        return menuName;
    }

    public void setMenuName(String menuName)
    {
        this.menuName = menuName;
    }

    public String getParentName()
    {
        return parentName;
    }

    public void setParentName(String parentName)
    {
        this.parentName = parentName;
    }

    public Long getParentId()
    {
        return parentId;
    }

    public void setParentId(Long parentId)
    {
        this.parentId = parentId;
    }

    @NotNull(message = "显示顺序不能为空")
    public Integer getOrderNum()
    {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum)
    {
        this.orderNum = orderNum;
    }

    @Size(min = 0, max = 200, message = "路由地址不能超过200个字符")
    public String getPath()
    {
        return path;
    }

    public void setPath(String path)
    {
        this.path = path;
    }

    @Size(min = 0, max = 200, message = "组件路径不能超过255个字符")
    public String getComponent()
    {
        return component;
    }

    public void setComponent(String component)
    {
        this.component = component;
    }

    public String getQuery()
    {
        return query;
    }

    public void setQuery(String query)
    {
        this.query = query;
    }

    public String getRouteName()
    {
        return routeName;
    }

    public void setRouteName(String routeName)
    {
        this.routeName = routeName;
    }

    public String getIsFrame()
    {
        return isFrame;
    }

    public void setIsFrame(String isFrame)
    {
        this.isFrame = isFrame;
    }

    public String getIsCache()
    {
        return isCache;
    }

    public void setIsCache(String isCache)
    {
        this.isCache = isCache;
    }

    @NotBlank(message = "菜单类型不能为空")
    public String getMenuType()
    {
        return menuType;
    }

    public void setMenuType(String menuType)
    {
        this.menuType = menuType;
    }

    public String getVisible()
    {
        return visible;
    }

    public void setVisible(String visible)
    {
        this.visible = visible;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    @Size(min = 0, max = 100, message = "权限标识长度不能超过100个字符")
    public String getPerms()
    {
        return perms;
    }

    public void setPerms(String perms)
    {
        this.perms = perms;
    }

    public String getIcon()
    {
        return icon;
    }

    public void setIcon(String icon)
    {
        this.icon = icon;
    }

    public List<SysMenuPO> getChildren()
    {
        return children;
    }

    public void setChildren(List<SysMenuPO> children)
    {
        this.children = children;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("menuId", getMenuId())
            .append("menuName", getMenuName())
            .append("parentId", getParentId())
            .append("orderNum", getOrderNum())
            .append("path", getPath())
            .append("component", getComponent())
            .append("query", getQuery())
            .append("routeName", getRouteName())
            .append("isFrame", getIsFrame())
            .append("IsCache", getIsCache())
            .append("menuType", getMenuType())
            .append("visible", getVisible())
            .append("status ", getStatus())
            .append("perms", getPerms())
            .append("icon", getIcon())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }

    public static List<SysMenu> menuPoToMenus(List<SysMenuPO> menus) {
        return menus.stream().map(SysMenuPO::menuPoToMenu).collect(Collectors.toList());
    }

    private static SysMenu menuPoToMenu(SysMenuPO menuPO) {
        SysMenu sysMenu = new SysMenu();
        sysMenu.setMenuId(menuPO.getMenuId());
        sysMenu.setMenuName(menuPO.getMenuName());
        sysMenu.setParentId(menuPO.getParentId());
        sysMenu.setOrderNum(menuPO.getOrderNum());
        sysMenu.setPath(menuPO.getPath());
        sysMenu.setComponent(menuPO.getComponent());
        sysMenu.setQuery(menuPO.getQuery());
        sysMenu.setRouteName(menuPO.getRouteName());
        sysMenu.setIsFrame(menuPO.getIsFrame());
        sysMenu.setIsCache(menuPO.getIsCache());
        sysMenu.setMenuType(menuPO.getMenuType());
        sysMenu.setVisible(menuPO.getVisible());
        sysMenu.setStatus(menuPO.getStatus());
        sysMenu.setPerms(menuPO.getPerms());
        sysMenu.setIcon(menuPO.getIcon());
        return sysMenu;
    }
}
