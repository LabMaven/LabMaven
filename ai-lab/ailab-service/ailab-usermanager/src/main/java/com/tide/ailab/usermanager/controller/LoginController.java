package com.tide.ailab.usermanager.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tide.ailab.common.exception.DispatchException;
import com.tide.ailab.common.exception.DispatchExceptionCode;
import com.tide.ailab.common.model.JsonResult;
import com.tide.ailab.common.model.JsonResultType;
import com.tide.ailab.common.model.UserInfo;
import com.tide.ailab.common.service.TokenService;
import com.tide.ailab.model.User;
import com.tide.ailab.usermanager.service.UserService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

/**
 * 登录管理控制类
 * 
 * @author User
 */
@RestController
@RequestMapping("/login")
public class LoginController {

	@Autowired
	private UserService userService;

	@Autowired
	private TokenService tokenService;

	/**
	 * 登录
	 * 
	 * @return
	 */
	@ApiOperation(value = "登录", notes = "登录")
	@ApiImplicitParam(name = "map", value = "用户名、密码，格式如{\"username\":\"\",\"password\":\"\"}", paramType = "body")
	@RequestMapping(value = "/checklogin", method = RequestMethod.POST)
	public String checkLogin(@RequestBody HashMap<String, String> map) {
		if (map.get("username") == null || map.get("password") == null) {
			throw new DispatchException(DispatchExceptionCode.INVALID_USERNAME_OR_PWD);
		}

		User query = new User();
		query.setUserName((String) map.get("username"));
		query.setPassword((String) map.get("password"));
		User user = userService.checkUserLogin(query);

		// 将登录用户存放到redis中,不保存密码
		user.setPassword("");

		// 拷贝同名属性
		UserInfo userInfo = new UserInfo();
		BeanUtils.copyProperties(user, userInfo);

		JsonResult result = new JsonResult();
		result.setType(JsonResultType.SUCCESS);
		String token = tokenService.createJWT(user.getId(), user.getUserName());
		result.add("token", token);

		Map<String, Object> userMap = new HashMap<String, Object>();
		userMap.put("userName", user.getUserName());
		userMap.put("trueName", user.getTrueName());
		userMap.put("customerId", user.getCustomerId());
		userMap.put("userType", user.getUserType());

		result.add("user", userMap);
		return result.toJSON();
	}
}
