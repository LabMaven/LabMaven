package com.tide.ailab.model;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 监控量属性
 * 
 * @author yinyuntao
 *
 */
public class MeteProperty {

	private int id;

	private String property;

	private String description;

	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
