package com.tide.ailab.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.tide.ailab.model.Device;
import com.tide.ailab.model.Sensor;

/**
 * 设备原子操作
 * 
 * @author User
 *
 */
@Repository
public interface DeviceDao {
	
	/**
	 * 查询设备列表
	 * 
	 * @param device
	 * @return
	 */
	List<Sensor> getSensorDevList(Sensor sensor);

	List<Sensor> getChartData(Sensor sensor);

	List<Device> getDeviceList(Device device);

	void updateSensor(Sensor sensor);

	void insertSensorData(Sensor sensor);

}
