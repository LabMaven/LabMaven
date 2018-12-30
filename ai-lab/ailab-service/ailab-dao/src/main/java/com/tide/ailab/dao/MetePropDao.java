package com.tide.ailab.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.tide.ailab.model.MeteProperty;

@Repository
public interface MetePropDao {

	// 查询监控量属性
	List<MeteProperty> qryMetePropList(MeteProperty meteProp);

	// 新增监控量属性
	public int addMeteProperty(MeteProperty meteProp);

	// 更新监控量属性
	public int updateMeteProperty(MeteProperty meteProp);

	// 删除监控量属性
	public int delMeteProperty(int id);

}
