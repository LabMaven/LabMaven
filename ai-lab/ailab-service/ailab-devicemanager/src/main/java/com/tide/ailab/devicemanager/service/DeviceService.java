package com.tide.ailab.devicemanager.service;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tide.ailab.common.log.Logger;
import com.tide.ailab.common.model.Page;
import com.tide.ailab.common.model.TreeNode;
import com.tide.ailab.common.util.DateTimeUtil;
import com.tide.ailab.common.util.StringUtil;
import com.tide.ailab.common.util.TreeBuilder;
import com.tide.ailab.dao.BuildingDao;
import com.tide.ailab.dao.DeviceDao;
import com.tide.ailab.dao.DictDao;
import com.tide.ailab.dao.FloorDao;
import com.tide.ailab.dao.RoomDao;
import com.tide.ailab.devicemanager.mgr.MgTemplate;
import com.tide.ailab.model.Building;
import com.tide.ailab.model.Device;
import com.tide.ailab.model.DictInfo;
import com.tide.ailab.model.Floor;
import com.tide.ailab.model.Room;
import com.tide.ailab.model.Sensor;

/**
 * 设备相关业务处理
 * 
 * @author User
 */
@Service
@EnableScheduling
public class DeviceService {

	@Autowired
	MgTemplate mgTemplate;

	@Autowired
	private DeviceDao deviceDao;

	@Autowired
	private RoomDao roomDao;

	@Autowired
	private BuildingDao bDao;

	@Autowired
	private FloorDao fDao;

	@Autowired
	private RoomDao rDao;

	@Autowired
	private DictDao dictDao;

	/**
	 * 分页获取控制器列表数据
	 * 
	 * @param page
	 * @param device
	 * @return PageInfo
	 */
	@Deprecated
	public PageInfo<Device> getDeviceListPage(Page page, Device cond) {
		PageHelper.startPage(page.getPageNum(), page.getPageSize());
		if (!StringUtil.isNullOrEmpty(page.getOrderBy())) {
			PageHelper.orderBy(page.getOrderBy());
		}

		List<Device> deviceList = deviceDao.getDeviceList(cond);
		PageInfo<Device> pageResult = new PageInfo<Device>(deviceList);
		return pageResult;
	}

	/**
	 * 分页获取传感器集合列表数据
	 * 
	 * @param page
	 * @return PageInfo
	 */
	public PageInfo<Sensor> getSensorCollectPage(Page page, Sensor cond) {
		PageHelper.startPage(page.getPageNum(), page.getPageSize());
		if (!StringUtil.isNullOrEmpty(page.getOrderBy())) {
			PageHelper.orderBy(page.getOrderBy());
		}

		// 查询传感器集合（s_pid为空或者s_pid=s_id的数据）
		List<Sensor> deviceList = deviceDao.getSensorCollect(cond);
		convertSensorList(deviceList);

		PageInfo<Sensor> pageResult = new PageInfo<Sensor>(deviceList);
		return pageResult;
	}

	public List<Sensor> getSensorCollect(Sensor cond) {
		return deviceDao.getSensorCollect(cond);
	}

	/**
	 * 获取房间列表数据
	 * 
	 * @param cond
	 * @return List
	 */
	public List<Room> getRoomList(Room cond) {
		List<Room> roomList = roomDao.getRoomList(cond);
		return roomList;
	}

	/**
	 * 获取传感器列表数据
	 * 
	 * @param cond
	 * @return List
	 */
	public List<Sensor> getSensorList(Sensor cond) {
		List<Sensor> deviceList = deviceDao.getSensorDataList(cond);
		convertSensorList(deviceList);

		return deviceList;
	}

	/**
	 * 分页获取具体的传感器列表数据
	 * 
	 * @param page
	 * @param device
	 * @return PageInfo
	 */
	public PageInfo<Sensor> getSensorListPage(Page page, Sensor cond) {
		PageHelper.startPage(page.getPageNum(), page.getPageSize());
		if (!StringUtil.isNullOrEmpty(page.getOrderBy())) {
			PageHelper.orderBy(page.getOrderBy());
		}

		// 查询具体的传感器数据（s_pid不为空的数据）
		List<Sensor> deviceList = deviceDao.getSensorDataList(cond);
		convertSensorList(deviceList);

		PageInfo<Sensor> pageResult = new PageInfo<Sensor>(deviceList);
		return pageResult;
	}

	public void updateSensor(Sensor sensor) {
		deviceDao.updateSensor(sensor);
	}

	public Map<String, Object> countSensor() {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		Map<String, Object> param = new HashMap<String, Object>();
		param.put("qryNormal", "true"); // 统计普通设备个数
		Map<String, Object> countMap1 = deviceDao.countDeviceData(param);
		resultMap.put("total", countMap1.get("total"));
		resultMap.put("abnormalCount", countMap1.get("abnormalCount"));

		param.clear();
		param.put("qryFan", "true"); // 统计风机盘管设备个数
		Map<String, Object> countMap2 = deviceDao.countDeviceData(param);
		resultMap.put("fanTotal", countMap2.get("total"));
		resultMap.put("fanAbnormalCount", countMap2.get("abnormalCount"));

		return resultMap;
	}

	/**
	 * 分页获取传感器列表数据
	 * 
	 * @param page
	 * @param cond
	 * @return
	 */
	public List<Map<String, Object>> getChartData(Page page, Sensor cond) {
		List<Sensor> sensorList = new ArrayList<Sensor>();
		if (!StringUtil.isNullOrEmpty(cond.getsPid())) {
			sensorList = deviceDao.getSubSensors(cond.getsPid());
		} else if (!StringUtil.isNullOrEmpty(cond.getsId())) {
			sensorList.add(cond);
		}

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if (CollectionUtils.isNotEmpty(sensorList)) {
			for (Sensor sensor : sensorList) {
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("sId", sensor.getsId());
				param.put("start", page.getPageNum() * page.getPageSize());
				param.put("size", page.getPageSize());
				List<Sensor> dataList = deviceDao.getChartData(param);

				Map<String, Object> map = new HashMap<String, Object>();
				map.put("sId", sensor.getsId());
				map.put("des", sensor.getDes());
				convertChartData(dataList, map);

				list.add(map);
			}
		}

		return list;
	}

	// @Scheduled(initialDelay = 30000, fixedDelay = 10000)
	@Transactional(propagation = Propagation.REQUIRED)
	public void refreshDeviceData() {
		Random rand = new Random();

		String[] cIdArr = { "101", "102", "103", "104", "105" };
		String cId = "r000000%s0001";

		String[] sIdArr = { "001", "002", "003", "004", "005" };

		Sensor sensor = new Sensor();
		sensor.setcId(String.format(cId, cIdArr[rand.nextInt(5)]));
		sensor.setsId(sensor.getcId() + sIdArr[rand.nextInt(5)]);
		sensor.setValue(rand.nextInt(150));

		deviceDao.insertSensorData(sensor);

		// insertDevice(device);

		Logger.d("refresh device data success.");

	}

	public Object getValue(Object dto, String name) throws Exception {
		Method[] m = dto.getClass().getMethods();
		for (int i = 0; i < m.length; i++) {
			if (("get" + name).toLowerCase().equals(m[i].getName().toLowerCase())) {
				return m[i].invoke(dto);
			}
		}

		return null;
	}

	/**
	 * 获取层级结构的技能列表
	 * 
	 * @return
	 */
	public List<TreeNode> getDeviceTree(String bId) {
		Building bCond = new Building();
		bCond.setbId(bId);
		List<Building> bList = bDao.getBuildingList(bCond);

		Floor fCond = new Floor();
		fCond.setbId(bId);
		List<Floor> fList = fDao.getFloorList(fCond);

		Room rCond = new Room();
		rCond.setbId(bId);
		List<Room> rList = rDao.getRoomList(rCond);
		// List<Device> eList = deviceDao.getDeviceList(null);

		List<TreeNode> nodeList = new ArrayList<TreeNode>();

		for (Building building : bList) {
			TreeNode node = new TreeNode();
			node.setId(building.getbId());
			node.setLabel(building.getDes());
			node.setName(building.getDes());
			node.setParentId("0");
			node.setNocheck(true);
			nodeList.add(node);
		}

		for (Floor floor : fList) {
			TreeNode node = new TreeNode();
			node.setId(floor.getfId());
			node.setLabel(floor.getDes());
			node.setName(floor.getDes());
			node.setParentId(String.valueOf(floor.getbId()));
			node.setNocheck(true);
			nodeList.add(node);
		}

		for (Room room : rList) {
			TreeNode node = new TreeNode();
			node.setId(room.getrId());
			node.setLabel(room.getDes());
			node.setName(room.getDes());
			node.setParentId(room.getfId());
			nodeList.add(node);
		}

		// for (Device device : eList) {
		// TreeNode node = new TreeNode();
		// node.setId(device.getcId());
		// node.setLabel(device.getDes());
		// node.setName(device.getDes());
		// node.setParentId(device.getcId().substring(0, 10));
		// nodeList.add(node);
		// }

		List<TreeNode> nodes = TreeBuilder.buildByRecursive(nodeList);

		return nodes;
	}

	private void convertSensorList(List<Sensor> sensorList) {
		if (CollectionUtils.isEmpty(sensorList)) {
			return;
		}

		DictInfo dictCond = new DictInfo();
		dictCond.setColName("sensor_type");

		for (Sensor sensor : sensorList) {

			dictCond.setDictCode(Integer.valueOf(sensor.getsType()));
			DictInfo sensorDict = dictDao.findByCode(dictCond);
			String sensorClass = sensorDict.getRemark();

			Sensor sensorCond = new Sensor();
			sensorCond.setsPid(sensor.getsId());
			List<Sensor> subSensorList = deviceDao.getSensorDataList(sensorCond);

			boolean hasAlarm = false;
			List<String> valueList = new ArrayList<String>();
			for (Sensor subSensor : subSensorList) {
				if (!hasAlarm
						&& (subSensor.getValue() < subSensor.getsMin() || subSensor.getValue() > subSensor.getsMax())) {
					hasAlarm = true;
				}

				dictCond.setDictCode(Integer.valueOf(subSensor.getsType()));
				DictInfo subSensorDict = dictDao.findByCode(dictCond);

				valueList.add(String.format("%s:%d", subSensorDict.getDictNote(), subSensor.getValue()));
			}
			sensor.setValueList(valueList);

			if (hasAlarm) {
				sensorClass += "_Alarm";
			} else {
				sensorClass += "_Normal";
			}
			sensor.setClassType(sensorClass);
		}
	}

	private void convertChartData(List<Sensor> dataList, Map<String, Object> map) {
		List<String> xList = new ArrayList<String>();
		List<String> yList = new ArrayList<String>();

		if (!CollectionUtils.isEmpty(dataList)) {
			// dataList是按时间降序的，此处再改成按时间升序
			for (int i = dataList.size() - 1; i >= 0; i--) {
				Sensor sensor = dataList.get(i);
				xList.add(DateTimeUtil.format(DateTimeUtil.getLocalDate(sensor.getInputTime(), TimeZone.getDefault())));
				yList.add(String.valueOf(sensor.getValue()));
			}
		}

		map.put("xData", xList);
		map.put("yData", yList);

	}

}
