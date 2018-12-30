package com.tide.ailab.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.tide.ailab.model.Floor;

/**
 * 楼层原子操作
 * 
 * @author User
 *
 */
@Repository
public interface FloorDao {

	/**
	 * 查询楼层列表
	 * 
	 * @param floor
	 * @return
	 */
	List<Floor> getFloorList(Floor floor);


}
