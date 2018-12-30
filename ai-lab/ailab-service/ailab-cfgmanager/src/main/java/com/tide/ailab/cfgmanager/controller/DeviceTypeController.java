package com.tide.ailab.cfgmanager.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.github.pagehelper.PageInfo;
import com.tide.ailab.cfgmanager.service.DeviceTypeService;
import com.tide.ailab.common.model.JsonResult;
import com.tide.ailab.common.model.JsonResultType;
import com.tide.ailab.common.model.Page;
import com.tide.ailab.model.DeviceType;

import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping(value = "/devicetype")
public class DeviceTypeController {

	@Autowired
	private DeviceTypeService deviceTypeManagerService;

	/**
	 * 设备类型查询列表（分页）
	 * 
	 * @return
	 */
	@ApiOperation(value = "设备类型查询列表（分页）", notes = "设备类型查询列表（分页）")
	@RequestMapping(value = "/qryDeviceTypeList", method = RequestMethod.GET)
	public String qryDeviceTypeList(DeviceType dt, Page page) {
		JsonResult result = new JsonResult(JsonResultType.SUCCESS);
		PageInfo<DeviceType> pageInfo = deviceTypeManagerService.qryDeviceTypeList(dt, page);
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
	@RequestMapping(value = "/qryDeviceTypeArr", method = RequestMethod.GET)
	public List<DeviceType> qryDeviceTypeListNoPage(DeviceType dt, HttpServletRequest request) {
		return deviceTypeManagerService.qryDeviceTypeListNoPage(dt);
	}

	/**
	 * 新增设备类型
	 * 
	 * @return
	 */
	@ApiOperation(value = "新增设备类型", notes = "新增设备类型")
	@RequestMapping(value = "/addDeviceType", method = RequestMethod.POST)
	public String addDeviceType(@RequestBody DeviceType deviceType, HttpServletRequest request) {
		return deviceTypeManagerService.addDeviceType(deviceType);
	}

	/**
	 * 更新设备类型
	 * 
	 * @return
	 */
	@ApiOperation(value = "更新设备类型", notes = "更新设备类型")
	@RequestMapping(value = "/updateDeviceType", method = RequestMethod.POST)
	public String updateDeviceType(@RequestBody DeviceType deviceType) {
		return deviceTypeManagerService.updateDeviceType(deviceType);
	}

	/**
	 * 删除设备类型
	 * 
	 * @return
	 */
	@ApiOperation(value = "删除设备类型", notes = "删除设备类型")
	@RequestMapping(value = "/delDeviceType/{id}", method = RequestMethod.GET)
	public String delDeviceType(@PathVariable("id") int id) {
		JsonResult res = new JsonResult(JsonResultType.SUCCESS);
		deviceTypeManagerService.delDeviceType(id);
		return res.toJSON();
	}

}
