package com.tide.ailab.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.tide.ailab.model.DeviceType;
import com.tide.ailab.model.DeviceTypeMete;

@Repository
public interface DeviceTypeDao {
	
	// 查询设备类型列表(支持名称模糊查询)
    public List<DeviceType> getDeviceTypeList(DeviceType query);

    // 查询某个设备类型（支持名称全查询）
    public List<DeviceType> getDeviceType(DeviceType deviceType);

    // 新增设备类型
    public int addDeviceType(DeviceType deviceType);

    // 更新设备类型
    public int updateDeviceType(DeviceType deviceType);

    // 删除设备类型
    public int delDeviceType(int id);
    
    // 查询所有设备类型监控量
    List<DeviceTypeMete> qryDeviceTypeMete();

}
