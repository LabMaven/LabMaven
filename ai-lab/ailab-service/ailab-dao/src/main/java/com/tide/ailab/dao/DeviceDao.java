package com.tide.ailab.dao;

import java.util.List;
import java.util.Map;

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
	 * 查询传感器及数据列表
	 * 
	 * @param device
	 * @return
	 */
	List<Sensor> getSensorDataList(Sensor sensor);

	/**
	 * 查询传感器集合列表， 如 1. 通风柜是一个传感器集合，包含通风柜门高、人员工作状态、面风速、通风柜变风量排风阀风量； 2.
	 * 压差传感器是一个具体的传感器，没有层级结构 此方法返回的集合包含通风柜（s_pid为空）和压差传感器（s_pid=s_id）
	 * 
	 * @param sensor
	 * @return
	 */
	List<Sensor> getSensorCollect(Sensor sensor);

	List<Sensor> getChartData(Map<String, Object> param);

	List<Device> getDeviceList(Device device);

	void updateSensor(Sensor sensor);

	void insertSensorData(Sensor sensor);

	/**
	 * 统计传感器个数(总数、异常数)
	 * 
	 * @param param
	 * @return
	 */
	Map<String, Object> countDeviceData(Map<String, Object> param);

	List<Sensor> getSubSensors(String sPid);

}
