package com.tide.ailab.model;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

public class Sensor {

	private String bDes;

	private String fDes;

	private String rDes;

	private String rId;

	private String cId;

	private String sId;

	private String sType;

	private String classType;

	private String des;

	private int sMax;

	private int sMin;

	private int value;

	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date inputTime;

	private Integer alarmType;

	private String alarmTypeDes;

	public String getbDes() {
		return bDes;
	}

	public void setbDes(String bDes) {
		this.bDes = bDes;
	}

	public String getfDes() {
		return fDes;
	}

	public void setfDes(String fDes) {
		this.fDes = fDes;
	}

	public String getrDes() {
		return rDes;
	}

	public void setrDes(String rDes) {
		this.rDes = rDes;
	}

	public String getrId() {
		return rId;
	}

	public void setrId(String rId) {
		this.rId = rId;
	}

	public String getcId() {
		return cId;
	}

	public void setcId(String cId) {
		this.cId = cId;
	}

	public String getsId() {
		return sId;
	}

	public void setsId(String sId) {
		this.sId = sId;
	}

	public String getsType() {
		return sType;
	}

	public void setsType(String sType) {
		this.sType = sType;
	}

	public String getClassType() {
		return classType;
	}

	public void setClassType(String classType) {
		this.classType = classType;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

	public int getsMax() {
		return sMax;
	}

	public void setsMax(int sMax) {
		this.sMax = sMax;
	}

	public int getsMin() {
		return sMin;
	}

	public void setsMin(int sMin) {
		this.sMin = sMin;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public Date getInputTime() {
		return inputTime;
	}

	public void setInputTime(Date inputTime) {
		this.inputTime = inputTime;
	}

	public Integer getAlarmType() {
		return alarmType;
	}

	public void setAlarmType(Integer alarmType) {
		this.alarmType = alarmType;
	}

	public String getAlarmTypeDes() {
		return alarmTypeDes;
	}

	public void setAlarmTypeDes(String alarmTypeDes) {
		this.alarmTypeDes = alarmTypeDes;
	}

}
