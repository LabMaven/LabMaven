package com.tide.ailab.common.consts;

/**
 * 常量，枚举定义
 * 
 * @author
 */
public class Consts {

	public static final String ACCOUNT_ADMIN_USER = "admin";

	public static final String ACCOUNT_ADMIN_USER_NAME = "账号管理员用户";

	public static final String ACCOUNT_ADMIN_ROLE_NAME = "账号管理员角色";

	// 根技能的ID
	public static final String SKILL_ROOT_ID = "0";

	public static final int FALSE = 0;

	public static final int TRUE = 1;

	/**
	 * 用户类型
	 */
	public static class UserType {
		public static final int SYSTEM = 1; // 1：系统管理员
		public static final int ACCOUNT = 2; // 2：客户管理员
		public static final int NORMAL = 3; // 3：普通客户用户
	}

}
