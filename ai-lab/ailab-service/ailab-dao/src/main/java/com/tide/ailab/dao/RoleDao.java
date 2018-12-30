package com.tide.ailab.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.tide.ailab.model.Role;
import com.tide.ailab.model.RoleMenu;

/**
 * 角色数据操作原子服务类
 * @author yinyuntao
 */
@Repository
public interface RoleDao {

    /**
     * 查询角色列表
     * @param condition
     * @return
     */
    List<Role> getRoleList(Role condition);

    /**
     * 新增角色
     * @param role
     * @return
     */
    int addRole(Role role);

    /**
     * 修改角色
     * @param role
     * @return
     */
    int updateRole(Role role);

    /**
     * 删除角色
     * @param id
     * @return
     */
    int deleteRole(String id);

    /**
     * 根据角色ID查询菜单ID列表
     * @param roleId
     * @return
     */
    List<RoleMenu> getRoleMenus(String roleId);

    /**
     * 新增角色菜单关联信息
     * @param roleMenus
     * @return
     */
    int batchAddRoleMenus(List<RoleMenu> roleMenus);

    /**
     * 删除角色菜单关联信息
     * @return
     */
    int deleteRoleMenu(String roleId);

}
