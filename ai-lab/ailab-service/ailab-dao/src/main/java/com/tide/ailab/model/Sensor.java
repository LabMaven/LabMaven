package com.tide.ailab.model;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

public class Sensor {

	private String bDes;

	private String fDes;

	private String rDes;

	private String rId;

	private String cId;

	/**
	 * ����������id
	 */
	private String sPid;

	private String sId;

	private String sType;

	private String classType;

	private String des;

	private Integer sMax;

	private Integer sMin;

	private int value;

	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date inputTime;

	private String alarmType;

	private String alarmTypeDes;

	/**
	 * ֻ�����ԣ�0ֻ����1����д��
	 */
	private int readOrWrite;

	/**
	 * �Ƿ�֧�ֿ����ã�0֧�֣�1��֧��
	 */
	private int configurable;

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

	public String getsPid() {
		return sPid;
	}

	public void setsPid(String sPid) {
		this.sPid = sPid;
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

	public Integer getsMax() {
		return sMax;
	}

	public void setsMax(Integer sMax) {
		this.sMax = sMax;
	}

	public Integer getsMin() {
		return sMin;
	}

	public void setsMin(Integer sMin) {
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

	public String getAlarmType() {
		return alarmType;
	}

	public void setAlarmType(String alarmType) {
		this.alarmType = alarmType;
	}

	public String getAlarmTypeDes() {
		return alarmTypeDes;
	}

	public void setAlarmTypeDes(String alarmTypeDes) {
		this.alarmTypeDes = alarmTypeDes;
	}

	public int getReadOrWrite() {
		return readOrWrite;
	}

	public void setReadOrWrite(int readOrWrite) {
		this.readOrWrite = readOrWrite;
	}

	public int getConfigurable() {
		return configurable;
	}

	public void setConfigurable(int configurable) {
		this.configurable = configurable;
	}

}
