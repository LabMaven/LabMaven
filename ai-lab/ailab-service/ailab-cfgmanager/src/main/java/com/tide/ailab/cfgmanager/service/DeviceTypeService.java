package com.tide.ailab.cfgmanager.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tide.ailab.common.exception.DispatchException;
import com.tide.ailab.common.exception.DispatchExceptionCode;
import com.tide.ailab.common.model.JsonResult;
import com.tide.ailab.common.model.JsonResultType;
import com.tide.ailab.common.model.Page;
import com.tide.ailab.common.util.ObjectUtil;
import com.tide.ailab.dao.DeviceTypeDao;
import com.tide.ailab.model.DeviceType;

@Service
public class DeviceTypeService {

	@Autowired
	private DeviceTypeDao deviceTypeDao;

	public PageInfo<DeviceType> qryDeviceTypeList(DeviceType dt, Page page) {
		PageHelper.startPage(page.getPageNum(), page.getPageSize());
		PageHelper.orderBy(page.getOrderBy());
		List<DeviceType> res = deviceTypeDao.getDeviceTypeList(dt);
		return new PageInfo<DeviceType>(res);
	}

	public List<DeviceType> qryDeviceTypeListNoPage(DeviceType dt) {
		List<DeviceType> res = deviceTypeDao.getDeviceTypeList(dt);
		return res;
	}

	public String addDeviceType(DeviceType deviceType) {
		JsonResult res = new JsonResult(JsonResultType.SUCCESS);
		// 检查名称是否重复
		boolean exist = checkDeviceTypeExist(deviceType);
		if (exist) {
			throw new DispatchException(DispatchExceptionCode.DEVICE_TYPE_NAME_EXIST);
		}
		deviceTypeDao.addDeviceType(deviceType);

		return res.toJSON();
	}

	/**
	 * 检查设备类型名称是否存在
	 * 
	 * @param dt
	 * @return
	 */
	private boolean checkDeviceTypeExist(DeviceType dt) {
		List<DeviceType> res = deviceTypeDao.getDeviceType(dt);
		return (!ObjectUtil.isNull(res) && res.size() > 0) ? true : false;
	}

	/**
	 * 更新设备类型
	 * 
	 * @param deviceType
	 * @return
	 */
	public String updateDeviceType(DeviceType deviceType) {
		JsonResult res = new JsonResult(JsonResultType.SUCCESS);
		deviceTypeDao.updateDeviceType(deviceType);
		return res.toJSON();
	}

	/**
	 * 删除设备类型
	 * 
	 * @param id
	 */
	public void delDeviceType(int id) {
		try {
			// if (checkDeviceTypeUsed(id)) {
			// // 设备类型被引用无法删除
			// throw new DispatchException(DispatchExceptionCode.FORBID_DELETE_FAILED);
			// }
			deviceTypeDao.delDeviceType(id);
		} catch (DispatchException ex) {
			throw ex;
		}
	}

}
