package com.tide.ailab.devicemanager.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tide.ailab.dao.CtlInfoDao;
import com.tide.ailab.model.CtlInfo;

@Service
public class CtlInfoService {

	@Autowired
	private CtlInfoDao ctlInfoDao;

	public int addCtlInfo(CtlInfo ctlInfo) {
		return ctlInfoDao.insertCtlInfo(ctlInfo);
	}

	public int batchAddCtlInfo(List<CtlInfo> ctlList) {
		return ctlInfoDao.batchAddCtlInfo(ctlList);
	}

}
