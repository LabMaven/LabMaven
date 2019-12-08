package com.tide.ailab.devicemanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tide.ailab.common.log.Logger;
import com.tide.ailab.common.model.JsonResult;
import com.tide.ailab.common.model.JsonResultType;
import com.tide.ailab.devicemanager.service.CoordinateService;
import com.tide.ailab.model.CoordinateInfo;

@RestController
@RequestMapping("/coordinate")
public class CoordinateController {

	@Autowired
	private CoordinateService coordService;

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String addCoordinate(@RequestBody CoordinateInfo coordInfo) {
		JsonResult result = new JsonResult(JsonResultType.SUCCESS);

		try {
			coordService.addCoordInfo(coordInfo);
		} catch (Exception e) {
			result.setType(JsonResultType.ERROR);
			Logger.e(e);
		}

		return result.toJSON();
	}

}
