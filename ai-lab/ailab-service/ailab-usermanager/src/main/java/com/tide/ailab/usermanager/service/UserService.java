package com.tide.ailab.usermanager.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tide.ailab.common.context.UserInfoHandler;
import com.tide.ailab.common.exception.DispatchException;
import com.tide.ailab.common.exception.DispatchExceptionCode;
import com.tide.ailab.common.model.Page;
import com.tide.ailab.common.model.UserInfo;
import com.tide.ailab.common.util.EncryptUtil;
import com.tide.ailab.common.util.GuidUtil;
import com.tide.ailab.common.util.SmsUtil;
import com.tide.ailab.common.util.StringUtil;
import com.tide.ailab.dao.UserDao;
import com.tide.ailab.dao.UserRoleDao;
import com.tide.ailab.model.User;
import com.tide.ailab.model.UserRole;

/**
 * 用户业务处理
 * 
 * @author User
 */
@Service
@Scope("prototype")
public class UserService {

	@Autowired
	private UserDao userDao;

	@Autowired
	private UserRoleDao userRoleDao;

	@Autowired
	private UserInfoHandler userInfoHandler;

	/**
	 * 新增用户，使用spring进行事物管理
	 * 
	 * @param user
	 */
	@Transactional
	public void addUser(User user) {
		// 校验用户是否已存在
		checkUserExist(user);

		// 生成32位UUID作为主键
		if (StringUtil.isNullOrEmpty(user.getId())) {
			user.setId(GuidUtil.getGuidWithoutHyphen());
		}
		// 对密码进行加密
		if (!StringUtil.isNullOrEmpty(user.getPassword())) {
			user.setPassword(EncryptUtil.md5(user.getPassword()));
		}

		user.setCustomerId(this.getCurrentUser().getCustomerId());
		user.setCreateTime(new Date());

		// 添加用户角色关系
		addUserRole(user);

		userDao.addUser(user);

	}

	public User getUserInfo(User user) {
		return userDao.getUserInfo(user);
	}

	/**
	 * 校验用户登录
	 * 
	 * @param condition
	 * @return
	 */
	public User checkUserLogin(User condition) {
		User user = userDao.getUserInfo(condition);

		// 根据用户名查询不到用户或者密码不匹配，抛出异常
		if (user == null || !condition.getPassword().equals(user.getPassword())) {
			throw new DispatchException(DispatchExceptionCode.INVALID_USERNAME_OR_PWD);
		}

		return user;
	}

	/**
	 * 修改用户
	 * 
	 * @param user
	 */
	@Transactional
	public void updateUser(User user) {
		if (StringUtil.isNullOrEmpty(user.getId())) {
			return;
		}

		// 校验修改后的phone和Email是否重复
		checkUserExist(user);

		// 对密码进行加密
		String needSendText = "";
		if (!StringUtil.isNullOrEmpty(user.getPassword())) {
			needSendText = user.getPassword();
			user.setPassword(EncryptUtil.md5(user.getPassword()));
		}

		// 添加用户角色关系
		addUserRole(user);

		userDao.updateUser(user);

		// 用户密码变更后发送短信
		if (!StringUtil.isNullOrEmpty(needSendText)) {
			SmsUtil.sendResetAcntAdminRemind(user.getUserName(), needSendText, user.getPhone());
		}
	}

	public List<User> getUserList(User condition) {
		return userDao.getUserList(condition);
	}

	/**
	 * 实现用户的逻辑删除
	 * 
	 * @param user
	 */
	public void deleteUser(User user) {
		userDao.deleteUser(user);
	}

	/**
	 * 分页查询用户信息
	 * 
	 * @param page
	 * @param condition
	 * @return
	 */
	public PageInfo<User> getUserListPage(Page page, User condition) {
		PageHelper.startPage(page.getPageNum(), page.getPageSize(), page.getOrderBy());
		List<User> userList = userDao.getUserList(condition);
		return new PageInfo<User>(userList);
	}

	/**
	 * 获取当前登录用户
	 * 
	 * @return
	 */
	public UserInfo getCurrentUser() {
		return userInfoHandler.getCurrentUser();
	}

	/**
	 * 添加用户角色关联关系
	 * 
	 * @param user
	 */
	private void addUserRole(User user) {
		// 先删除原有用户角色关联关系
		UserRole cond = new UserRole();
		cond.setUserId(user.getId());
		userRoleDao.deleteUserRole(cond);

		// 重新插入用户角色关联关系
		if (!StringUtil.isNullOrEmpty(user.getRoleIds())) {
			List<UserRole> userRoles = new ArrayList<UserRole>();
			String[] roleIds = user.getRoleIds().split(",");
			for (String roleId : roleIds) {
				UserRole userRole = new UserRole();
				userRole.setUserId(user.getId());
				userRole.setRoleId(roleId);
				userRoles.add(userRole);
			}

			userRoleDao.batchAddUserRoles(userRoles);
		}

	}

	/**
	 * 校验用户名、手机号、email是否已注册
	 * 
	 * @param user
	 */
	private void checkUserExist(User user) {
		User cond = null;

		// 校验用户名是否存在(t_cfg_user.user_name有唯一约束，校验时不判断状态)
		if (!StringUtil.isNullOrEmpty(user.getUserName())) {
			cond = new User();
			cond.setUserName(user.getUserName());
			User result = userDao.getUserInfo(cond);
			if (result != null && (StringUtil.isNullOrEmpty(user.getId()) || !result.getId().equals(user.getId()))) {
				throw new DispatchException(DispatchExceptionCode.USER_HAS_EXISTED);
			}
		}

		// 校验手机号是否已注册
		if (!StringUtil.isNullOrEmpty(user.getPhone())) {
			cond = new User();
			cond.setPhone(user.getPhone());
			User result = userDao.getUserInfo(cond);
			if (result != null && (StringUtil.isNullOrEmpty(user.getId()) || !result.getId().equals(user.getId()))) {
				throw new DispatchException(DispatchExceptionCode.PHONE_HAS_REGISTED);
			}
		}

		// 校验Email是否已注册
		if (!StringUtil.isNullOrEmpty(user.getEmail())) {
			cond = new User();
			cond.setEmail(user.getEmail());
			User result = userDao.getUserInfo(cond);
			if (result != null && (StringUtil.isNullOrEmpty(user.getId()) || !result.getId().equals(user.getId()))) {
				throw new DispatchException(DispatchExceptionCode.EMAIL_HAS_REGISTED);
			}
		}
	}

	/**
	 * 修改当前用户密码
	 * 
	 * @param map
	 */
	public void updatePassword(HashMap<String, String> map) {
		User cond = new User();
		cond.setId(getCurrentUser().getId());

		User user = userDao.getUserInfo(cond);
		if (!user.getPassword().equals(EncryptUtil.md5(map.get("password")))) {
			throw new DispatchException(DispatchExceptionCode.PASSWORD_INCORRECT);
		}

		try {
			cond.setPassword(EncryptUtil.md5(map.get("newPassword")));
			userDao.updateUser(cond);
		} catch (Exception e) {
			throw new DispatchException(DispatchExceptionCode.MODIFY_PASSWORD_FAILED);
		}
	}

	/**
	 * 更新用户密码
	 * 
	 * @param user
	 */
	public void updatePwd(User user) {
		userDao.updateUser(user);
	}
}
