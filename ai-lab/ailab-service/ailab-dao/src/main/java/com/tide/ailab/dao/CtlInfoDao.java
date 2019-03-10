package com.tide.ailab.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.tide.ailab.model.CtlInfo;

/**
 * 控制信息原子操作
 * 
 * @author User
 *
 */
@Repository
public interface CtlInfoDao {

	/**
	 * 插入控制信息
	 * 
	 * @param ctlInfo
	 */
	int insertCtlInfo(CtlInfo ctlInfo);

	/**
	 * 批量插入控制信息
	 * 
	 * @param ctlInfo
	 */
	int batchAddCtlInfo(List<CtlInfo> ctlList);

}
