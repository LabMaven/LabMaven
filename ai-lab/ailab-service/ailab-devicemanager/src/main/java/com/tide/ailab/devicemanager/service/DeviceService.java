package com.tide.ailab.devicemanager.service;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TimeZone;

import org.apache.commons.collections.CollectionUtils;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
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
	private RedisTemplate<String, Object> redisTemplate;

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
		PageHelper.startPage(page.getPageNum(), page.getPageSize() * 3);
		if (!StringUtil.isNullOrEmpty(page.getOrderBy())) {
			PageHelper.orderBy(page.getOrderBy());
		}

		// 查询传感器集合（s_pid为空或者s_pid=s_id的数据）
		List<Sensor> deviceList = deviceDao.getSensorCollect(cond);
		convertSensorList(deviceList);

		PageInfo<Sensor> pageResult = new PageInfo<Sensor>(deviceList);
		long total = 0;
		if (!CollectionUtils.isEmpty(deviceList)) {
			total = pageResult.getTotal() % 3 == 0 ? pageResult.getTotal() / 3 : pageResult.getTotal() / 3 + 1;
		}
		pageResult.setTotal(total);
		return pageResult;
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

	/**
	 * 分页获取传感器列表数据
	 * 
	 * @param page
	 * @param cond
	 * @return
	 */
	public Map<String, List<String>> getChartData(Page page, Sensor cond) {
		PageHelper.startPage(page.getPageNum(), page.getPageSize());
		if (!StringUtil.isNullOrEmpty(page.getOrderBy())) {
			PageHelper.orderBy(page.getOrderBy());
		}

		List<Sensor> dataList = deviceDao.getChartData(cond);
		PageInfo<Sensor> pageResult = new PageInfo<Sensor>(dataList);

		Map<String, List<String>> map = new HashMap<String, List<String>>();

		convertChartData(pageResult.getList(), map);
		return map;
	}

	@Scheduled(initialDelay = 30000, fixedDelay = 10000)
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

	/**
	 * 插入Device信息
	 * 
	 * @param device
	 */
	public void insertDevice(Device device) {
		try {
			ObjectId objectId = ObjectId.get();
			Document devObj = new Document("_id", objectId);

			Set<Object> props = redisTemplate.opsForSet().members("devicetype.mete.1");
			for (Object key : props) {
				String prop = (String) key;
				devObj.put(prop, String.valueOf(getValue(device, prop)));
			}
			mgTemplate.getCurMonthDeviceTable().insertOne(devObj);
		} catch (Exception e) {
			Logger.e(e);
		}
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

		List<DictInfo> dictList = dictDao.getByColName("sensor_type");

		for (Sensor sensor : sensorList) {
			for (DictInfo dictInfo : dictList) {
				if (!StringUtil.isNullOrEmpty(sensor.getsType())
						&& sensor.getsType().equals(String.valueOf(dictInfo.getDictCode()))) {
					String sensorClass = dictInfo.getRemark();
					if (sensor.getValue() < sensor.getsMin() || sensor.getValue() > sensor.getsMax()) {
						sensorClass += "_Alarm";
					} else {
						sensorClass += "_Normal";
					}

					sensor.setClassType(sensorClass);
				}
			}
		}
	}

	private void convertChartData(List<Sensor> dataList, Map<String, List<String>> map) {
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
