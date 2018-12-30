package com.tide.ailab.model;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 角色实体类
 * 
 * @author
 */
public class Role {

	/**
	 * 角色id
	 */
	private String id;
	/**
	 * 角色名
	 */
	private String name;

	/**
	 * 描述
	 */
	private String description;

	/**
	 * 角色创建时间
	 */
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;

	/**
	 * 是否派单角色
	 */
	private int isDispatch;

	/**
	 * 角色可以访问的菜单ID
	 */
	private String menuIds;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public int getIsDispatch() {
		return isDispatch;
	}

	public void setIsDispatch(int isDispatch) {
		this.isDispatch = isDispatch;
	}

	public String getMenuIds() {
		return menuIds;
	}

	public void setMenuIds(String menuIds) {
		this.menuIds = menuIds;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Role {id=").append(id).append(", name=").append(name).append(", isDispatch=").append(isDispatch)
				.append(", menuIds=").append(menuIds).append("}");

		return sb.toString();
	}
}
