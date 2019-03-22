package com.tide.ailab.devicemanager.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.tide.ailab.common.model.JsonResult;
import com.tide.ailab.common.model.JsonResultType;
import com.tide.ailab.common.model.Page;
import com.tide.ailab.common.model.TreeNode;
import com.tide.ailab.devicemanager.service.DeviceService;
import com.tide.ailab.model.Room;
import com.tide.ailab.model.Sensor;

import io.swagger.annotations.ApiOperation;

/**
 * 设备操作控制器
 * 
 * @author user
 */
@RestController
@RequestMapping("/device")
public class DeviceController {

	/**
	 * 任务操作服务器
	 */
	@Resource
	private DeviceService deviceService;

	/**
	 * 分页查询传感器列表
	 * 
	 * @param page
	 * @param sensor
	 * @return
	 */
	@RequestMapping(value = "/sensorlist", method = RequestMethod.GET)
	public String getSensorPageList(Page page, Sensor sensor) {
		JsonResult result = new JsonResult(JsonResultType.SUCCESS);
		PageInfo<Sensor> pageInfo = deviceService.getSensorListPage(page, sensor);
		result.add("data", pageInfo);
		return result.toJSON();
	}

	/**
	 * 查询房间列表
	 * 
	 * @param room
	 * @return
	 */
	@RequestMapping(value = "/rooms", method = RequestMethod.GET)
	public String getRooms(Room room) {
		JsonResult result = new JsonResult(JsonResultType.SUCCESS);
		List<Room> roomList = deviceService.getRoomList(room);
		result.add("data", roomList);
		return result.toJSON();
	}

	/**
	 * 查询传感器列表
	 * 
	 * @param page
	 * @param sensor
	 * @return
	 */
	@RequestMapping(value = "/sensors", method = RequestMethod.GET)
	public String getSensorList(Sensor sensor) {
		JsonResult result = new JsonResult(JsonResultType.SUCCESS);
		List<Sensor> sensorList = deviceService.getSensorList(sensor);

		// 是否允许显示全部一键强排
		boolean allExhaustVisable = false;
		if (CollectionUtils.isNotEmpty(sensorList)) {
			for (Sensor s : sensorList) {
				if ("5".equals(s.getsType()) && s.getConfigurable() == 0) {
					allExhaustVisable = true;
					break;
				}
			}
		}
		result.add("data", sensorList);
		result.add("allExhaustVisable", allExhaustVisable);
		return result.toJSON();
	}

	/**
	 * 分页查询传感器集合列表,显示图标
	 * 
	 * @param page
	 * @param sensor
	 * @return
	 */
	@RequestMapping(value = "/sensorcollect", method = RequestMethod.GET)
	public String getSensorCollect(Page page, Sensor sensor) {
		JsonResult result = new JsonResult(JsonResultType.SUCCESS);
		PageInfo<Sensor> pageInfo = deviceService.getSensorCollectPage(page, sensor);
		result.add("data", pageInfo);
		return result.toJSON();
	}

	@RequestMapping(value = "updatesensor", method = RequestMethod.POST)
	public String updateSensor(@RequestBody Sensor sensor) {
		JsonResult res = new JsonResult(JsonResultType.SUCCESS);
		deviceService.updateSensor(sensor);
		return res.toJSON();
	}

	/**
	 * 分页查询传感器列表
	 * 
	 * @param page
	 * @param sensor
	 * @return
	 */
	@RequestMapping(value = "/getChartData", method = RequestMethod.GET)
	public String getChartData(Page page, Sensor sensor) {
		JsonResult result = new JsonResult(JsonResultType.SUCCESS);
		List<Map<String, Object>> data = deviceService.getChartData(page, sensor);
		result.add("data", data);
		return result.toJSON();
	}

	/**
	 * 获取设备树
	 * 
	 * @return
	 */
	@ApiOperation(value = "获取设备树", notes = "获取设备树")
	@RequestMapping(value = "/getDeviceTree", method = RequestMethod.GET)
	public String getDeviceTree(String bId) {
		JsonResult result = new JsonResult(JsonResultType.SUCCESS);
		List<TreeNode> nodeList = deviceService.getDeviceTree(bId);
		result.add("data", nodeList);
		return result.toJSON();
	}

	/**
	 * 分页查询传感器列表
	 * 
	 * @param page
	 * @param sensor
	 * @return
	 */
	@RequestMapping(value = "/countsensor", method = RequestMethod.GET)
	public String countSensor() {
		JsonResult result = new JsonResult(JsonResultType.SUCCESS);
		Map<String, Object> resultMap = deviceService.countSensor();
		result.add("total", resultMap.get("total") != null ? resultMap.get("total") : 0);
		result.add("abnormalCount", resultMap.get("abnormalCount") != null ? resultMap.get("abnormalCount") : 0);
		result.add("fanTotal", resultMap.get("fanTotal") != null ? resultMap.get("fanTotal") : 0);
		result.add("fanAbnormalCount",
				resultMap.get("fanAbnormalCount") != null ? resultMap.get("fanAbnormalCount") : 0);
		return result.toJSON();
	}

}
