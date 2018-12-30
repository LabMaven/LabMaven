package com.tide.ailab.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.tide.ailab.model.User;
import com.tide.ailab.model.UserRole;

/**
 * 用户角色原子操作
 * @author User
 */
@Repository
public interface UserRoleDao {

    /**
     * 根据条件查询用户角色列表
     * @param condition
     * @return
     */
    List<UserRole> getUserRoleList(UserRole condition);

    /**
     * 新增用户角色
     * @param userRole
     */
    void addUserRole(UserRole userRole);

    /**
     * 删除用户角色
     * @param userRole
     */
    void deleteUserRole(UserRole userRole);

    /**
     * 批量插入用户角色关系
     * @param userRoles
     */
    void batchAddUserRoles(List<UserRole> userRoles);

    /**
     * 根据用户删除用户角色
     * @param user
     * @return
     */
    int deleteUserRoleByUser(User user);
}
