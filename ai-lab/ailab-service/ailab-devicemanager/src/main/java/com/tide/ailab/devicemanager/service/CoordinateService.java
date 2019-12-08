package com.tide.ailab.devicemanager.service;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tide.ailab.dao.CoordinateDao;
import com.tide.ailab.model.CoordinateInfo;

@Service
public class CoordinateService {

	@Value("${rectangle.area.x.offset}")
	private int xOffset;

	@Value("${rectangle.area.y.offset}")
	private int yOffset;

	@Autowired
	private CoordinateDao coordDao;

	public int addCoordInfo(CoordinateInfo coordInfo) {
		if (StringUtils.isNotEmpty(coordInfo.getCoordinate())) {
			String[] coords = coordInfo.getCoordinate().split(",");
			coordInfo.setCoordinate(String.format("%s,%s,%s", coordInfo.getCoordinate(),
					Integer.parseInt(coords[0]) + xOffset, Integer.parseInt(coords[1]) + yOffset));
		}

		return coordDao.insertCoordinateInfo(coordInfo);
	}

}
