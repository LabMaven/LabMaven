package com.tide.ailab.devicemanager.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tide.ailab.common.context.UserContextHolder;
import com.tide.ailab.common.log.Logger;
import com.tide.ailab.common.model.JsonResult;
import com.tide.ailab.common.model.JsonResultType;
import com.tide.ailab.common.util.StringUtil;
import com.tide.ailab.devicemanager.service.CtlInfoService;
import com.tide.ailab.devicemanager.service.DeviceService;
import com.tide.ailab.model.CtlInfo;
import com.tide.ailab.model.Sensor;

@RestController
@RequestMapping("/ctl")
public class CtlInfoController {

	@Autowired
	private CtlInfoService ctlService;

	@Autowired
	private DeviceService deviceService;

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String addCtlInfo(@RequestBody CtlInfo ctlInfo) {
		JsonResult result = new JsonResult(JsonResultType.ERROR);

		String userId = UserContextHolder.getUserId();
		if (StringUtil.isNullOrEmpty(userId)) {
			result.setMessage("管理员ID为空");
			return result.toJSON();
		}

		try {
			ctlInfo.setoId(userId);
			int count = ctlService.addCtlInfo(ctlInfo);

			if (count > 0) {
				result.setType(JsonResultType.SUCCESS);
			} else {
				result.setMessage("该传感器存在未处理的控制信息!");
			}
		} catch (Exception e) {
			Logger.e(e);
		}

		return result.toJSON();
	}

	@RequestMapping(value = "/batchadd", method = RequestMethod.POST)
	public String batchAddCtlInfo(@RequestBody CtlInfo ctlInfo) {
		JsonResult result = new JsonResult(JsonResultType.ERROR);

		String userId = UserContextHolder.getUserId();
		if (StringUtil.isNullOrEmpty(userId)) {
			result.setMessage("管理员ID为空");
			return result.toJSON();
		}

		if (ctlInfo == null || StringUtil.isNullOrEmpty(ctlInfo.getrId()) || ctlInfo.getvType() == null
				|| ctlInfo.getvType() != 2) {
			result.setMessage("參數不合法");
			return result.toJSON();
		}

		try {
			// 批量一键强排
			Sensor cond = new Sensor();
			cond.setrId(ctlInfo.getrId());
			cond.setsType("5"); // 通风柜

			List<Sensor> sensorList = deviceService.getSensorCollect(cond);

			if (CollectionUtils.isNotEmpty(sensorList)) {
				List<CtlInfo> ctlList = new ArrayList<CtlInfo>();
				for (Sensor sensor : sensorList) {
					CtlInfo ctl = new CtlInfo();
					BeanUtils.copyProperties(ctl, sensor);
					ctl.setsStatus(ctlInfo.getsStatus());
					ctl.setoId(userId);

					ctlList.add(ctl);
				}
				int addCount = ctlService.batchAddCtlInfo(ctlList);
				result.add("addCount", addCount);
				result.add("totalCount", sensorList.size());
				result.setType(JsonResultType.SUCCESS);
			}
		} catch (Exception e) {
			Logger.e(e);
			result.setMessage("批量一键强排失败");
		}

		return result.toJSON();
	}

}
