package com.sdwu.infrastructure.persistent.repository;

import com.sdwu.domain.sysuser.model.entity.SysMenu;
import com.sdwu.domain.sysuser.model.entity.SystemUser;
import com.sdwu.domain.sysuser.model.valobj.MetaVo;
import com.sdwu.domain.sysuser.model.valobj.RouterVo;
import com.sdwu.domain.sysuser.repository.ISysMenuPermRepository;
import com.sdwu.infrastructure.persistent.dao.*;
import com.sdwu.infrastructure.persistent.po.SysMenuPO;
import com.sdwu.infrastructure.persistent.po.SysRoleMenuPO;
import com.sdwu.infrastructure.persistent.po.SysRolePO;
import com.sdwu.infrastructure.persistent.po.SysUserRolePO;
import com.sdwu.infrastructure.persistent.utils.StringUtils;
import com.sdwu.types.common.Constants;
import com.sdwu.types.common.UserConstants;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;
@Repository
public class SysMenuPermRepository implements ISysMenuPermRepository {


    @Resource
    private ISysRoleMenuDao sysRoleMenuDao;

    @Resource
    private ISysUserRoleDao sysUserRoleDao;

    @Resource
    private ISysMenuDao sysMenuDao;


    @Resource
    private ISysRoleDao sysRoleDao;
    @Override
    public List<String> findMenuPermListByUserId(Long userId) {
        List<SysUserRolePO> sysUserRolePOList = sysUserRoleDao.selectSysUserRoleListByUserId(userId);
        if (sysUserRolePOList == null || sysUserRolePOList.isEmpty()) {
            return Collections.emptyList();
        }
        Set<Long> roleIds = sysUserRolePOList.stream()
                .map(SysUserRolePO::getRoleId)
                .filter(Objects::nonNull) // 过滤掉null值
                .collect(Collectors.toSet());
        List<SysRoleMenuPO> sysRoleMenuList = sysRoleMenuDao.selectRoleMenuByRoleIds(roleIds);
        if (sysRoleMenuList == null || sysRoleMenuList.isEmpty()) {
            return Collections.emptyList();
        }
        Set<Long> menuIds = sysRoleMenuList.stream()
                .map(SysRoleMenuPO::getMenuId)
                .filter(Objects::nonNull) // 过滤掉null值
                .collect(Collectors.toSet());
        List<SysMenuPO> sysMenuPOList = sysMenuDao.selectMenuByMenuIds(menuIds);
        Set<String> menuPerm = sysMenuPOList.stream()
                .map(SysMenuPO::getPerms)
                .filter(perms -> perms != null && !perms.isEmpty())
                .collect(Collectors.toSet());
        return new ArrayList<>(menuPerm);
    }

    @Override
    public List<SysMenu> selectMenuTreeByUserId(SystemUser user) {
        List<SysMenuPO> menus = null;
        if (user.isAdmin(user.getUserId())){
            menus=sysMenuDao.selectMenuTreeAll();
        }
        else{
            menus=sysMenuDao.selectMenuTreeByUserId(user.getUserId());
        }
        List<SysMenu> sysMenus = SysMenuPO.menuPoToMenus(menus);
        return getChildPerms(sysMenus, 0);
    }

    /**
     * 构建前端路由所需要的菜单
     *
     * @param menus 菜单列表
     * @return 路由列表
     */
    @Override
    public List<RouterVo> buildMenus(List<SysMenu> menus)
    {
        List<RouterVo> routers = new LinkedList<RouterVo>();
        for (SysMenu menu : menus)
        {
            RouterVo router = new RouterVo();
            router.setHidden("1".equals(menu.getVisible()));
            router.setName(getRouteName(menu));
            router.setPath(getRouterPath(menu));
            router.setComponent(getComponent(menu));
            router.setQuery(menu.getQuery());
            router.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), StringUtils.equals("1", menu.getIsCache()), menu.getPath()));
            List<SysMenu> cMenus = menu.getChildren();
            if (StringUtils.isNotEmpty(cMenus) && UserConstants.TYPE_DIR.equals(menu.getMenuType()))
            {
                router.setAlwaysShow(true);
                router.setRedirect("noRedirect");
                router.setChildren(buildMenus(cMenus));
            }
            else if (isMenuFrame(menu))
            {
                router.setMeta(null);
                List<RouterVo> childrenList = new ArrayList<RouterVo>();
                RouterVo children = new RouterVo();
                children.setPath(menu.getPath());
                children.setComponent(menu.getComponent());
                children.setName(getRouteName(menu.getRouteName(), menu.getPath()));
                children.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), StringUtils.equals("1", menu.getIsCache()), menu.getPath()));
                children.setQuery(menu.getQuery());
                childrenList.add(children);
                router.setChildren(childrenList);
            }
            else if (menu.getParentId().intValue() == 0 && isInnerLink(menu))
            {
                router.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon()));
                router.setPath("/");
                List<RouterVo> childrenList = new ArrayList<RouterVo>();
                RouterVo children = new RouterVo();
                String routerPath = innerLinkReplaceEach(menu.getPath());
                children.setPath(routerPath);
                children.setComponent(UserConstants.INNER_LINK);
                children.setName(getRouteName(menu.getRouteName(), routerPath));
                children.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), menu.getPath()));
                childrenList.add(children);
                router.setChildren(childrenList);
            }
            routers.add(router);
        }
        return routers;
    }

    @Override
    public List<SysMenu> selectMenuListByUserId(Long userId) {
        List<SysMenuPO> menus = null;
        if (SystemUser.isAdmin(userId)){
            menus=sysMenuDao.selectMenuTreeAll();
        }
        else{
            menus=sysMenuDao.selectMenuTreeByUserId(userId);
        }
        List<SysMenu> sysMenus = SysMenuPO.menuPoToMenus(menus);
        return sysMenus;
    }

    @Override
    public List<SysMenu> buildMenuTree(List<SysMenu> menus)
    {
        List<SysMenu> returnList = new ArrayList<SysMenu>();
        List<Long> tempList = menus.stream().map(SysMenu::getMenuId).collect(Collectors.toList());
        for (Iterator<SysMenu> iterator = menus.iterator(); iterator.hasNext();)
        {
            SysMenu menu = (SysMenu) iterator.next();
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(menu.getParentId()))
            {
                recursionFn(menus, menu);
                returnList.add(menu);
            }
        }
        if (returnList.isEmpty())
        {
            returnList = menus;
        }
        return returnList;
    }

    @Override
    public List<Long> selectMenuListByRoleId(Long roleId) {
        SysRolePO sysRolePO = sysRoleDao.findByRoleId(roleId);
        return sysMenuDao.selectMenuListByRoleId(roleId, sysRolePO.isMenuCheckStrictly());
    }

    @Override
    public boolean checkMenuNameUnique(SysMenu menu) {
        if (sysMenuDao.findMenuByName(menu.getMenuName()).size() > 0) {
            return true;
        }
        return false;
    }

    @Override
    public int insertMenu(SysMenu menu) {
        SysMenuPO sysMenuPO = SysMenuPO.convertToPO(menu);
        return sysMenuDao.insertMenu(sysMenuPO);
    }

    @Override
    public int updateMenu(SysMenu menu) {
        SysMenuPO sysMenuPO = SysMenuPO.convertToPO(menu);
        return sysMenuDao.updateMenu(sysMenuPO);
    }

    @Override
    public SysMenu selectMenuById(Long menuId) {
        SysMenuPO sysMenuPO = sysMenuDao.selectMenuById(menuId);
        SysMenu sysMenu = SysMenuPO.convertToDo(sysMenuPO);
        return sysMenu;
    }

    @Override
    public boolean hasChildrenByMenuId(Long menuId) {
        if (sysMenuDao.hasChildrenByMenuId(menuId) > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean checkMenuExistRole(Long menuId) {
        if (sysRoleMenuDao.checkMenuExistRole(menuId) > 0) {
            return true;
        }
        return false;
    }

    @Override
    public int deleteMenuById(Long menuId) {
        return sysMenuDao.deleteMenuById(menuId);
    }


    /**
     * 内链域名特殊字符替换
     *
     * @return 替换后的内链域名
     */
    public String innerLinkReplaceEach(String path)
    {
        return StringUtils.replaceEach(path, new String[] { Constants.HTTP, Constants.HTTPS, Constants.WWW, ".", ":" },
                new String[] { "", "", "", "/", "/" });
    }
    /**
     * 是否为内链组件
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isInnerLink(SysMenu menu)
    {
        return menu.getIsFrame().equals(UserConstants.NO_FRAME) && StringUtils.ishttp(menu.getPath());
    }

    /**
     * 获取组件信息
     *
     * @param menu 菜单信息
     * @return 组件信息
     */
    public String getComponent(SysMenu menu)
    {
        String component = UserConstants.LAYOUT;
        if (StringUtils.isNotEmpty(menu.getComponent()) && !isMenuFrame(menu))
        {
            component = menu.getComponent();
        }
        else if (StringUtils.isEmpty(menu.getComponent()) && menu.getParentId().intValue() != 0 && isInnerLink(menu))
        {
            component = UserConstants.INNER_LINK;
        }
        else if (StringUtils.isEmpty(menu.getComponent()) && isParentView(menu))
        {
            component = UserConstants.PARENT_VIEW;
        }
        return component;
    }
    /**
     * 是否为parent_view组件
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isParentView(SysMenu menu)
    {
        return menu.getParentId().intValue() != 0 && UserConstants.TYPE_DIR.equals(menu.getMenuType());
    }
    public String getRouterPath(SysMenu menu)
    {
        String routerPath = menu.getPath();
        // 内链打开外网方式
        if (menu.getParentId().intValue() != 0 && isInnerLink(menu))
        {
            routerPath = innerLinkReplaceEach(routerPath);
        }
        // 非外链并且是一级目录（类型为目录）
        if (0 == menu.getParentId().intValue() && UserConstants.TYPE_DIR.equals(menu.getMenuType())
                && UserConstants.NO_FRAME.equals(menu.getIsFrame()))
        {
            routerPath = "/" + menu.getPath();
        }
        // 非外链并且是一级目录（类型为菜单）
        else if (isMenuFrame(menu))
        {
            routerPath = "/";
        }
        return routerPath;
    }

    /**
     * 获取路由名称
     *
     * @param menu 菜单信息
     * @return 路由名称
     */
    public String getRouteName(SysMenu menu)
    {
        // 非外链并且是一级目录（类型为目录）
        if (isMenuFrame(menu))
        {
            return StringUtils.EMPTY;
        }
        return getRouteName(menu.getRouteName(), menu.getPath());
    }

    /**
     * 获取路由名称，如没有配置路由名称则取路由地址
     *
     * @param routerName 路由名称
     * @param path 路由地址
     * @return 路由名称（驼峰格式）
     */
    public String getRouteName(String name, String path)
    {
        String routerName = StringUtils.isNotEmpty(name) ? name : path;
        return StringUtils.capitalize(routerName);
    }


    /**
     * 是否为菜单内部跳转
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isMenuFrame(SysMenu menu)
    {
        return menu.getParentId().intValue() == 0 && UserConstants.TYPE_MENU.equals(menu.getMenuType())
                && menu.getIsFrame().equals(UserConstants.NO_FRAME);
    }

    /**
     * 根据父节点的ID获取所有子节点
     *
     * @param list 分类表
     * @param parentId 传入的父节点ID
     * @return String
     */
    public List<SysMenu> getChildPerms(List<SysMenu> list, int parentId)
    {
        List<SysMenu> returnList = new ArrayList<SysMenu>();
        for (Iterator<SysMenu> iterator = list.iterator(); iterator.hasNext();)
        {
            SysMenu t = (SysMenu) iterator.next();
            // 一、根据传入的某个父节点ID,遍历该父节点的所有子节点
            if (t.getParentId() == parentId)
            {
                recursionFn(list, t);
                returnList.add(t);
            }
        }
        return returnList;
    }

    /**
     * 递归列表
     *
     * @param list 分类表
     * @param t 子节点
     */
    private void recursionFn(List<SysMenu> list, SysMenu t)
    {
        // 得到子节点列表
        List<SysMenu> childList = getChildList(list, t);
        t.setChildren(childList);
        for (SysMenu tChild : childList)
        {
            if (hasChild(list, tChild))
            {
                recursionFn(list, tChild);
            }
        }
    }
    /**
     * 得到子节点列表
     */
    private List<SysMenu> getChildList(List<SysMenu> list, SysMenu t)
    {
        List<SysMenu> tlist = new ArrayList<SysMenu>();
        Iterator<SysMenu> it = list.iterator();
        while (it.hasNext())
        {
            SysMenu n = (SysMenu) it.next();
            if (n.getParentId().longValue() == t.getMenuId().longValue())
            {
                tlist.add(n);
            }
        }
        return tlist;
    }
    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<SysMenu> list, SysMenu t)
    {
        return getChildList(list, t).size() > 0;
    }

}
