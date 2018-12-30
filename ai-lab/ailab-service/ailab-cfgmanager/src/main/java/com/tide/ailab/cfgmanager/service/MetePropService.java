package com.tide.ailab.cfgmanager.service;

import java.util.ArrayList;
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
import com.tide.ailab.common.model.TreeNode;
import com.tide.ailab.common.util.ObjectUtil;
import com.tide.ailab.dao.MetePropDao;
import com.tide.ailab.model.MeteProperty;

@Service
public class MetePropService {

	@Autowired
	private MetePropDao metePropDao;

	public PageInfo<MeteProperty> qryMetePropList(MeteProperty meteProp, Page page) {
		PageHelper.startPage(page.getPageNum(), page.getPageSize());
		PageHelper.orderBy(page.getOrderBy());
		List<MeteProperty> res = metePropDao.qryMetePropList(meteProp);
		return new PageInfo<MeteProperty>(res);
	}

	public List<MeteProperty> qryMetePropListNoPage(MeteProperty meteProp) {
		List<MeteProperty> res = metePropDao.qryMetePropList(meteProp);
		return res;
	}

	public String addMeteProp(MeteProperty meteProp) {
		JsonResult res = new JsonResult(JsonResultType.SUCCESS);
		// 检查名称是否重复
		boolean exist = checkMetePropExist(meteProp);
		if (exist) {
			throw new DispatchException(DispatchExceptionCode.DEVICE_TYPE_NAME_EXIST);
		}
		metePropDao.addMeteProperty(meteProp);

		return res.toJSON();
	}

	/**
	 * 检查设备类型名称是否存在
	 * 
	 * @param dt
	 * @return
	 */
	private boolean checkMetePropExist(MeteProperty meteProp) {
		List<MeteProperty> res = metePropDao.qryMetePropList(meteProp);
		return (!ObjectUtil.isNull(res) && res.size() > 0) ? true : false;
	}

	/**
	 * 更新设备类型
	 * 
	 * @param deviceType
	 * @return
	 */
	public String updateMeteProp(MeteProperty meteProp) {
		JsonResult res = new JsonResult(JsonResultType.SUCCESS);
		metePropDao.updateMeteProperty(meteProp);
		return res.toJSON();
	}

	/**
	 * 删除设备类型
	 * 
	 * @param id
	 */
	public void delMeteProp(int id) {
		try {
			// if (checkMetePropUsed(id)) {
			// // 设备类型被引用无法删除
			// throw new DispatchException(DispatchExceptionCode.FORBID_DELETE_FAILED);
			// }
			metePropDao.delMeteProperty(id);
		} catch (DispatchException ex) {
			throw ex;
		}
	}

	/**
	 * 获取监控量树
	 * 
	 * @param condition
	 * @return
	 */
	public List<TreeNode> getMeteTree(MeteProperty condition) {
		List<MeteProperty> roleList = qryMetePropListNoPage(condition);

		List<TreeNode> nodeList = new ArrayList<TreeNode>();

		for (MeteProperty meteProp : roleList) {
			TreeNode node = new TreeNode();
			node.setId(String.valueOf(meteProp.getId()));
			node.setLabel(meteProp.getProperty());
			node.setParentId("0");

			nodeList.add(node);
		}

		return nodeList;
	}

}
