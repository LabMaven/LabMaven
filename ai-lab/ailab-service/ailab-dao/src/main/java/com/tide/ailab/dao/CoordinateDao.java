package com.tide.ailab.dao;

import org.springframework.stereotype.Repository;

import com.tide.ailab.model.CoordinateInfo;

/**
 * 传感器热区坐标原子操作
 * 
 * @author User
 *
 */
@Repository
public interface CoordinateDao {

	/**
	 * 插入传感器热区坐标信息
	 * 
	 * @param coordInfo
	 */
	int insertCoordinateInfo(CoordinateInfo coordInfo);

}
