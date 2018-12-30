package com.tide.ailab.cfgmanager.init;

import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import com.tide.ailab.common.log.Logger;
import com.tide.ailab.dao.DeviceTypeDao;
import com.tide.ailab.dao.DictDao;
import com.tide.ailab.model.DeviceTypeMete;
import com.tide.ailab.model.DictInfo;

/**
 * 系统启动时，执行run方法，将数据初始化到redis中
 * 
 * @author User
 */

@Component
public class InitData implements ApplicationRunner {

	@Autowired
	private ValueOperations<String, Object> valueOperations;

	@Autowired
	private ZSetOperations<String, Object> zSetOperations;

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	@Autowired
	private DictDao dictDao;

	@Autowired
	private DeviceTypeDao deviceTypeDao;

	@Override
	public void run(ApplicationArguments arg0) throws Exception {

		Logger.d("加载数据到redis......");

		// 加载字典表到redis中
		initDict();

		initDeviceMete();
	}

	private void initDict() {
		Logger.d("清空字典表信息");
		List<String> colNames = dictDao.getColName();

		Set<String> indexKey;
		for (String colName : colNames) {
			indexKey = redisTemplate.keys(colName + "*");
			redisTemplate.delete(indexKey);
		}

		Logger.d("加载字典信息......");
		List<DictInfo> dictInfos = dictDao.getAllDict();

		String key;

		for (DictInfo dictInfo : dictInfos) {

			key = dictInfo.getColName() + ".code." + dictInfo.getDictCode();
			valueOperations.set(key, dictInfo);
			zSetOperations.add(dictInfo.getColName() + "~key", key, 0);

		}
		Logger.d("字典表信息加载完成......");
	}

	private void initDeviceMete() {
		Logger.d("清空设备类型监控量属性信息");
		redisTemplate.delete("devicetype.mete.*");
		
		List<DeviceTypeMete> deviceMeteList = deviceTypeDao.qryDeviceTypeMete();

		if (CollectionUtils.isEmpty(deviceMeteList)) {
			return;
		}

		Logger.d("加载设备类型监控量属性信息......");
		for (DeviceTypeMete deviceMete : deviceMeteList) {
			redisTemplate.opsForSet().add("devicetype.mete." + deviceMete.getDevicetypeId(), deviceMete.getProperty());
		}
		Logger.d("设备类型监控量属性信息加载完成......");

	}

}
