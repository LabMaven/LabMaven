package com.tide.ailab.model;

/**
 * 控制信息数据类
 * 
 * @author yinyuntao
 *
 */
public class CoordinateInfo {

	private Integer sMapId;

	private Integer sPointId;

	/**
	 * 传感器Id
	 */
	private String sId;

	private String coordinate;

	private String des;

	public Integer getsMapId() {
		return sMapId;
	}

	public void setsMapId(Integer sMapId) {
		this.sMapId = sMapId;
	}

	public Integer getsPointId() {
		return sPointId;
	}

	public void setsPointId(Integer sPointId) {
		this.sPointId = sPointId;
	}

	public String getsId() {
		return sId;
	}

	public void setsId(String sId) {
		this.sId = sId;
	}

	public String getCoordinate() {
		return coordinate;
	}

	public void setCoordinate(String coordinate) {
		this.coordinate = coordinate;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

}
