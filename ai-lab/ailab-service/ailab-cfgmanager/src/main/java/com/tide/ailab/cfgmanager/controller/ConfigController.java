package com.tide.ailab.cfgmanager.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.tide.ailab.cfgmanager.service.MetePropService;
import com.tide.ailab.common.model.JsonResult;
import com.tide.ailab.common.model.JsonResultType;
import com.tide.ailab.common.model.TreeNode;
import com.tide.ailab.model.MeteProperty;

import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping(value = "/config")
public class ConfigController {

	@Autowired
	MetePropService metePropSerivce;

	/**
	 * 获取监控量树
	 * 
	 * @return
	 */
	@ApiOperation(value = "获取监控量树", notes = "获取监控量树")
	@RequestMapping(value = "/getMeteTree", method = RequestMethod.GET)
	public String getRoleTree() {
		JsonResult result = new JsonResult(JsonResultType.SUCCESS);
		MeteProperty condition = new MeteProperty();
		List<TreeNode> nodeList = metePropSerivce.getMeteTree(condition);
		result.add("data", nodeList);
		return result.toJSON();
	}

}
