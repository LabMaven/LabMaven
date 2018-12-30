package com.tide.ailab.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.tide.ailab.model.Room;

/**
 * 设备原子操作
 * 
 * @author User
 *
 */
@Repository
public interface EListDao {

	/**
	 * 查询房间列表
	 * 
	 * @param room
	 * @return
	 */
	List<Room> getRoomList(Room room);


}
