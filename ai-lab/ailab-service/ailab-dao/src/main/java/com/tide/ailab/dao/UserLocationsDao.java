package com.tide.ailab.dao;

import java.util.Map;

import org.springframework.stereotype.Repository;

/**
 * 用户轨迹原子操作
 * @author User
 */
@Repository
public interface UserLocationsDao {

    /**
     * 添加用户轨迹信息
     * @param UserLocations
     * @return
     */
    void addUserLocations(Map<String, Object> mapUserLocation);
}
