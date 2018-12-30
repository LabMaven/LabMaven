package com.tide.ailab.model;

/**
 * 用户角色关联实体类
 * @author
 */
public class UserRole {

    /**
     * 用户id
     */
    private String userId;

    /**
     * 角色id
     */
    private String roleId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String id) {
        this.userId = id;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String id) {
        this.roleId = id;
    }

    @Override
    public String toString() {
        return "Role [Use ID=" + userId + ", Role ID=" + roleId + "]";
    }
}
