package com.tide.ailab.usermanager.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tide.ailab.common.exception.DispatchException;
import com.tide.ailab.common.exception.DispatchExceptionCode;
import com.tide.ailab.common.model.Page;
import com.tide.ailab.common.model.TreeNode;
import com.tide.ailab.common.util.GuidUtil;
import com.tide.ailab.common.util.StringUtil;
import com.tide.ailab.dao.RoleDao;
import com.tide.ailab.dao.UserRoleDao;
import com.tide.ailab.model.Role;
import com.tide.ailab.model.RoleMenu;
import com.tide.ailab.model.UserRole;

/**
 * 角色业务处理
 * @author User
 */
@Service
@Scope("prototype")
public class RoleService {

    @Autowired
    private UserRoleDao userRoleDao;

    @Autowired
    private RoleDao roleDao;

    /**
     * 按条件查询角色列表
     * @param condition
     * @return
     */
    public List<Role> getRoleList(Role condition) {
        return roleDao.getRoleList(condition);
    }

    /**
     * 获取角色树
     * @param condition
     * @return
     */
    public List<TreeNode> getRoleTree(Role condition) {
        List<Role> roleList = getRoleList(condition);

        List<TreeNode> nodeList = new ArrayList<TreeNode>();

        for (Role role : roleList) {
            TreeNode node = new TreeNode();
            node.setId(role.getId());
            node.setLabel(role.getName());
            node.setParentId("0");

            nodeList.add(node);
        }

        return nodeList;
    }

    /**
     * 分页查询角色列表
     * @param condition
     * @param page
     * @return
     */
    public PageInfo<Role> getRoleListPage(Role condition, Page page) {
        PageHelper.startPage(page.getPageNum(), page.getPageSize());
        List<Role> roleList = roleDao.getRoleList(condition);
        return new PageInfo<Role>(roleList);
    }

    /**
     * 新增角色
     * @param role
     * @return
     */
    @Transactional
    public int addRole(Role role) {
        if (StringUtil.isNullOrEmpty(role.getId())) {
            role.setId(GuidUtil.getGuidWithoutHyphen());
        }

        // 该账户下已存在同名的角色，则抛出异常
        if (isRoleExist(role)) {
            throw new DispatchException(DispatchExceptionCode.ROLE_HAS_EXISTED);
        }

        // 插入角色菜单关联数据
        addRoleMenus(role);

        return roleDao.addRole(role);
    }

    /**
     * 修改角色
     * @param role
     * @return
     */
    @Transactional
    public int updateRole(Role role) {
        // 该账户下已存在同名的角色，则抛出异常
        if (isRoleExist(role)) {
            throw new DispatchException(DispatchExceptionCode.ROLE_HAS_EXISTED);
        }

        // 替换角色菜单关联数据
        addRoleMenus(role);

        return roleDao.updateRole(role);
    }

    @Transactional
    public int deleteRole(String roleId) {
        // 判断角色是否被使用，已有用户使用则抛出异常，不允许删除
        UserRole condition = new UserRole();
        condition.setRoleId(roleId);
        if (!CollectionUtils.isEmpty(userRoleDao.getUserRoleList(condition))) {
            throw new DispatchException(DispatchExceptionCode.ROLE_IS_USED);
        }

        // 删除角色菜单关联数据
        roleDao.deleteRoleMenu(roleId);

        return roleDao.deleteRole(roleId);
    }

    /**
     * 根据条件查询角色是否存在
     * @param role
     * @return
     */
    private boolean isRoleExist(Role role) {
        List<Role> roleList = roleDao.getRoleList(role);
        if (CollectionUtils.isEmpty(roleList)) {
            return false;
        }

        return !roleList.get(0).getId().equals(role.getId());
    }

    /**
     * 添加角色菜单数据
     * 新增角色时添加角色菜单数据;修改角色时替换角色菜单数据
     * 配置时传入的角色菜单数据为空时，清空原有的角色菜单数据
     * @param role
     */
    private void addRoleMenus(Role role) {
        // 先清空该角色所有菜单权限
        roleDao.deleteRoleMenu(role.getId());

        // 再根据配置插入菜单权限
        if (!StringUtil.isNullOrEmpty(role.getMenuIds())) {
            List<RoleMenu> roleMenus = new ArrayList<RoleMenu>();
            String[] menuIds = role.getMenuIds().split(",");
            for (String menuId : menuIds) {
                RoleMenu roleMenu = new RoleMenu();
                roleMenu.setRoleId(role.getId());
                roleMenu.setMenuId(Integer.parseInt(menuId));
                roleMenus.add(roleMenu);
            }

            roleDao.batchAddRoleMenus(roleMenus);
        }
    }

}
