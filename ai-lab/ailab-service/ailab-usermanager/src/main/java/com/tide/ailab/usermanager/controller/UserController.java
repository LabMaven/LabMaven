package com.tide.ailab.usermanager.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.github.pagehelper.PageInfo;
import com.tide.ailab.common.consts.Consts;
import com.tide.ailab.common.model.JsonResult;
import com.tide.ailab.common.model.JsonResultType;
import com.tide.ailab.common.model.Page;
import com.tide.ailab.common.model.SelectOption;
import com.tide.ailab.common.model.UserInfo;
import com.tide.ailab.common.service.OssStorageService;
import com.tide.ailab.common.util.GuidUtil;
import com.tide.ailab.model.DictInfo;
import com.tide.ailab.model.User;
import com.tide.ailab.usermanager.service.UserService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

/**
 * 用户管理控制类
 * 
 * @author User
 */
@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	OssStorageService ossStorageService;

	/**
	 * 分页查询用户列表
	 * 
	 * @param page
	 * @param condition
	 * @return
	 */
	@ApiOperation(value = "查询用户信息", notes = "查询用户信息")
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String getUserList(Page page, User condition) {
		UserInfo currentUser = userService.getCurrentUser();
		// 当前登录为客户管理员时，只查询当前客户下的账户
		if (Consts.UserType.ACCOUNT == currentUser.getUserType()) {
			condition.setCustomerId(currentUser.getCustomerId());
		} else if (Consts.UserType.NORMAL == currentUser.getUserType()) {
			// 当前登录用户为普通客户用户时，只查询自身
			condition.setUserName(currentUser.getUserName());
		}

		JsonResult result = new JsonResult(JsonResultType.SUCCESS);
		PageInfo<User> pageInfo = userService.getUserListPage(page, condition);
		result.add("data", pageInfo);
		return result.toJSON();
	}

	/**
	 * 查询用户类型（不包含系统管理员和帐号管理员）
	 * 
	 * @return
	 */
	@RequestMapping(value = "/getUserType", method = RequestMethod.GET)
	public String getUserType() {
		List<SelectOption> userTypeList = new ArrayList<SelectOption>();
		JsonResult result = new JsonResult(JsonResultType.SUCCESS);
		UserInfo currentUser = userService.getCurrentUser();

		List<DictInfo> userTypes = userService.getUserTypes();

		if (!CollectionUtils.isEmpty(userTypes)) {
			for (DictInfo dict : userTypes) {
				// 当前登录用户为客户管理员或普通客户用户时，只加载普通用户类型
				if (currentUser.getUserType() == Consts.UserType.NORMAL
						|| currentUser.getUserType() == Consts.UserType.ACCOUNT) {
					if (dict.getDictCode().intValue() == Consts.UserType.NORMAL) {
						SelectOption option = new SelectOption();
						option.setLabel(dict.getDictNote());
						option.setValue(dict.getDictCode().toString());
						userTypeList.add(option);
						break;
					}
				} else {
					// 登录用户为系统管理员时，加载客户管理员和客户普通用户类型
					if (dict.getDictCode().intValue() == Consts.UserType.ACCOUNT
							|| dict.getDictCode().intValue() == Consts.UserType.NORMAL) {
						SelectOption option = new SelectOption();
						option.setLabel(dict.getDictNote());
						option.setValue(dict.getDictCode().toString());
						userTypeList.add(option);
					}
				}
			}
		}

		result.add("data", userTypeList.toArray());
		return result.toJSON();
	}

	/**
	 * 返回员工类型
	 * 
	 * @return
	 */
	@RequestMapping(value = "/getEmployeeType", method = RequestMethod.GET)
	public String getEmployeeType() {
		List<SelectOption> employeeTypeList = new ArrayList<SelectOption>();

		List<DictInfo> employeeTypes = userService.getEmployeeTypes();
		if (!CollectionUtils.isEmpty(employeeTypes)) {
			for (DictInfo dict : employeeTypes) {
				SelectOption option = new SelectOption();
				option.setLabel(dict.getDictNote());
				option.setValue(dict.getDictCode().toString());
				employeeTypeList.add(option);
			}
		}

		JsonResult result = new JsonResult(JsonResultType.SUCCESS);
		result.add("data", employeeTypeList.toArray());
		return result.toJSON();

	}

	/**
	 * 新增用户
	 * 
	 * @param user
	 * @return
	 */
	@ApiOperation(value = "新增用户", notes = "新增用户信息")
	@ApiImplicitParam(name = "user", value = "用户", required = true, dataType = "User")
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String addUser(@RequestBody User user) {
		JsonResult result = new JsonResult(JsonResultType.SUCCESS);
		userService.addUser(user);
		return result.toJSON();
	}

	/**
	 * 修改用户
	 * 
	 * @param user
	 * @return
	 */
	@ApiOperation(value = "修改用户", notes = "修改用户信息")
	@ApiImplicitParam(name = "user", value = "用户", required = true, dataType = "User")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String updateUser(@RequestBody User user) {
		JsonResult result = new JsonResult(JsonResultType.SUCCESS);
		userService.updateUser(user);
		return result.toJSON();
	}

	@ApiOperation(value = "删除用户", notes = "根据用户ID删除用户信息")
	@ApiImplicitParam(name = "user", value = "用户", required = true, dataType = "User")
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public String delete(@RequestBody User user) {
		userService.deleteUser(user);
		// 封装返回结果
		JsonResult result = new JsonResult(JsonResultType.SUCCESS);
		return result.toJSON();
	}

	@ApiOperation(value = "查询所有用户信息", notes = "查询所有用户信息")
	@RequestMapping(value = "/getall", method = RequestMethod.GET)
	public List<User> getAll() {
		return userService.getUserList(null);
	}

	@ApiOperation(value = "根据用户ID查询用户信息", notes = "根据用户ID查询用户信息")
	@ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "String", paramType = "path")
	@RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
	public User getUserbyId(@PathVariable String id) {
		User query = new User();
		query.setId(id);
		User user = userService.getUserInfo(query);
		return user;
	}

	@ApiOperation(value = "上传头像", notes = "上传头像")
	@RequestMapping(value = "/saveImage", method = RequestMethod.POST)
	@ResponseBody
	public String saveImage(MultipartHttpServletRequest mreq) {
		String url = "";
		JsonResult result = new JsonResult(JsonResultType.SUCCESS);
		String ossDir = String.format("user/%s", GuidUtil.getGuidWithoutHyphen());
		// 上传文件到服务器指定位置
		Map<String, MultipartFile> fileMap = mreq.getFileMap();
		if (MapUtils.isNotEmpty(fileMap)) {
			Map<String, String> urlResult = ossStorageService.putFiles(fileMap, ossDir);
			for (String key : urlResult.keySet()) {
				url = urlResult.get(key);
				break;
			}
		}
		result.add("url", url);
		return result.toJSON();
	}

	/**
	 * 修改用户
	 * 
	 * @param user
	 * @return
	 */
	@ApiOperation(value = "修改密码", notes = "修改密码")
	@ApiImplicitParam(name = "user", value = "用户", required = true, dataType = "User")
	@RequestMapping(value = "/modifyPassword", method = RequestMethod.POST)
	public String modifyPassword(@RequestBody HashMap<String, String> map) {
		JsonResult result = new JsonResult(JsonResultType.SUCCESS);
		userService.updatePassword(map);
		return result.toJSON();
	}
}
