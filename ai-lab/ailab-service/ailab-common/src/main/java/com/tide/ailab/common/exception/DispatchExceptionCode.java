package com.tide.ailab.common.exception;

/**
 * 调度系统异常码定义类，定义不同流程的异常码，为6位数字
 * @author user
 */
public class DispatchExceptionCode {

    /** 调度平台通用异常码 start 100001~199999 **/

    /** 未知错误 **/
    public static final int UNKNOW_ERROR = 100001;

    /** 无效的token值 **/
    public static final int TOKEN_INVALID = 100002;

    /** 删除数据失败 **/
    public static final int DELETE_FAILED = 100003;

    /** 调度平台通用异常码 end 100001~199999 **/

    /** 调度平台用户管理模块异常码 start 200001~299999 **/

    /** 用户名或密码错误 */
    public static final int INVALID_USERNAME_OR_PWD = 200001;

    /** 添加账号对应的管理员用户异常 */
    public static final int INSERT_ACCOUNT_ADMIN_USER_ERROR = 200002;

    /** 用户已存在 */
    public static final int USER_HAS_EXISTED = 200003;

    /** 手机号码已注册 */
    public static final int PHONE_HAS_REGISTED = 200004;

    /** email已注册 */
    public static final int EMAIL_HAS_REGISTED = 200005;

    /** 原密码不正确 */
    public static final int PASSWORD_INCORRECT = 200006;

    /** 修改密码失败 */
    public static final int MODIFY_PASSWORD_FAILED = 200007;

    /** 角色已存在 */
    public static final int ROLE_HAS_EXISTED = 200011;

    /** 角色已被使用，不能删除 */
    public static final int ROLE_IS_USED = 200012;

    /** 账号已存在：账号编码或者账号名称已存在 */
    public static final int ACCOUNT_HAS_EXISTED = 200013;

    /** 账号已被使用，不能删除 */
    public static final int ACCOUNT_IS_USED = 200014;

    /** 账号对应的管理员用户已存在 */
    public static final int ACCOUNT_ADMIN_USER_HAS_EXISTED = 200015;

    /** 该账号下存在普通用户，不能删除 */
    public static final int ACCOUNT_HAS_NORMAL_USER = 200016;

    /** 该账号存在任务，不能删除 */
    public static final int ACCOUNT_HAS_TASK = 200017;

    /** 添加账户管理员用户失败 */
    public static final int USER_ADD_FAIL = 200018;

    /** 该账号被公司绑定，不能删除 */
    public static final int ACCOUNT_HAS_COMPANY = 200019;

    /** 该账号被技能绑定，不能删除 */
    public static final int ACCOUNT_HAS_SKILL = 200020;

    /** 调度平台用户管理模块异常码 end 200001~299999 **/

    /** 调度平台配置管理模块异常码 start 300001~399999 **/
    public static final int FORBID_DELETE_FAILED = 300001;

    /** 设备类型名称已经存在 **/
    public static final int DEVICE_TYPE_NAME_EXIST = 300002;

    /** 故障类型名称已经存在 **/
    public static final int FAULT_TYPE_NAME_EXIST = 300003;

    /** 故障类型删除失败 **/
    public static final int FAULT_TYPE_DELETE_FAILED = 300004;

    /** 运维公司已存在：该账户下公司编码已存在或公司名称已存在 **/
    public static final int COMPANY_HAS_EXISTED = 300005;

    /** 公司已被用户绑定，不能删除 */
    public static final int COMPANY_IS_USED = 300006;

    /** 技能名称已存在 */
    public static final int SKILL_NAME_EXIST = 300007;

    /** 参数值不在有效范围内 */
    public static final int PARAM_OUTOF_BOUNDS = 300008;

    /** 技能已经被用户绑定，不能删除 */
    public static final int SKILL_IS_USED = 300009;

    /** 技能有子技能，不能删除 */
    public static final int SKILL_HAS_CHILD = 300010;

    /** 有效值范围错误 */
    public static final int VALUE_RANGE_ERROR = 300011;

    /** 自定义区域已存在 */
    public static final int CUSTOMIZED_AREA_EXIST = 300012;

    /** 自定义区域已被使用，不能删除 */
    public static final int CUSTOMIZED_AREA_IS_USED = 300013;

    /** 调度平台配置管理模块异常码 end 300001~399999 **/

    /** 调度平台任务管理模块异常码 start 400001~499999 **/

    /** 调度平台任务管理模块异常码 end 400001~499999 **/
}
