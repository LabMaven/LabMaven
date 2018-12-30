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

	/**
	 * redis的key信息
	 */
	public static class RedisKey {

		public static final String KEYS_SUF = "~keys"; // redis中索引集合的key的后缀


		private static final String DEVICETYPE_REDIS = "devicetype"; // redis中设备类型信息的表名
		public static final String DEVICETYPE_REDIS_PRE = DEVICETYPE_REDIS + ".id."; // 设备类型信息的key的前缀
		public static final String DEVICETYPE_INDEX = DEVICETYPE_REDIS + KEYS_SUF; // 设备类型信息索引集合的key

		private static final String FALUTTYPE_REDIS = "faulttype"; // redis中故障类型信息的表名
		public static final String FALUTTYPE_REDIS_PRE = FALUTTYPE_REDIS + ".id."; // 故障类型信息的key的前缀
		public static final String FALUTTYPE_INDEX = FALUTTYPE_REDIS + KEYS_SUF; // 故障类型信息索引集合的key

		public static final String USERCOOR_REDIS_PRE = "usercoor.userid."; // 用户位置信息

		private static final String PARAM_REDIS = "param"; // redis中配置信息的表名
		public static final String PARAM_REDIS_PRE = PARAM_REDIS + ".accountid.name."; // 配置信息的key的前缀
		public static final String PARAM_INDEX = PARAM_REDIS + KEYS_SUF; // 配置信息索引集合的key

		public static final String USER_TYPE_KEY = "user_type~key";

		public static final String EMPLOYEE_TYPE_KEY = "employee_type~key";
		
		public static final String ORDER_STATUS_KEY = "order_status~key";
	}
}
