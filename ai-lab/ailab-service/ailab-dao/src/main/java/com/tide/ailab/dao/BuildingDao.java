package com.tide.ailab.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.tide.ailab.model.Building;

/**
 * 楼宇原子操作
 * 
 * @author User
 *
 */
@Repository
public interface BuildingDao {

	/**
	 * 查询楼宇列表
	 * 
	 * @param building
	 * @return
	 */
	List<Building> getBuildingList(Building building);

}
