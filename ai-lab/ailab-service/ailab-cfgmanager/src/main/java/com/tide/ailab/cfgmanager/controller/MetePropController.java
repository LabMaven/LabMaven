package com.tide.ailab.cfgmanager.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.tide.ailab.cfgmanager.service.MetePropService;
import com.tide.ailab.common.model.JsonResult;
import com.tide.ailab.common.model.JsonResultType;
import com.tide.ailab.common.model.Page;
import com.tide.ailab.model.MeteProperty;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/meteprop")
public class MetePropController {

	@Autowired
	private MetePropService metePropService;

	/**
	 * 设备类型查询列表（分页）
	 * 
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String qryMetePropList(MeteProperty dt, Page page) {
		JsonResult result = new JsonResult(JsonResultType.SUCCESS);
		PageInfo<MeteProperty> pageInfo = metePropService.qryMetePropList(dt, page);
		result.add("data", pageInfo);
		return result.toJSON();
	}

	/**
	 * 查询设备列表（无分页）接口
	 * 
	 * @param dt
	 * @return
	 */
	@ApiOperation(value = "查询设备列表（无分页）", notes = "查询设备列表（无分页）")
	@RequestMapping(value = "/qryMetePropArr", method = RequestMethod.GET)
	public List<MeteProperty> qryMetePropListNoPage(MeteProperty dt, HttpServletRequest request) {
		return metePropService.qryMetePropListNoPage(dt);
	}

	/**
	 * 新增设备类型
	 * 
	 * @return
	 */
	@ApiOperation(value = "新增设备类型", notes = "新增设备类型")
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String addMeteProp(@RequestBody MeteProperty meteProp, HttpServletRequest request) {
		return metePropService.addMeteProp(meteProp);
	}

	/**
	 * 更新设备类型
	 * 
	 * @return
	 */
	@ApiOperation(value = "更新设备类型", notes = "更新设备类型")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String updateMeteProp(@RequestBody MeteProperty meteProp) {
		return metePropService.updateMeteProp(meteProp);
	}

	/**
	 * 删除设备类型
	 * 
	 * @return
	 */
	@ApiOperation(value = "删除设备类型", notes = "删除设备类型")
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
	public String delMeteProp(@PathVariable("id") int id) {
		JsonResult res = new JsonResult(JsonResultType.SUCCESS);
		metePropService.delMeteProp(id);
		return res.toJSON();
	}

}
