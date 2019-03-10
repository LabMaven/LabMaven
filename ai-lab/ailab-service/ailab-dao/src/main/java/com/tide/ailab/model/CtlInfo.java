package com.tide.ailab.model;

import java.util.Date;

/**
 * 控制信息数据类
 * 
 * @author yinyuntao
 *
 */
public class CtlInfo {

	private int id;

	private String rId;

	private String cCode;

	/**
	 * 控制器Id
	 */
	private String cId;

	private String sCode;

	/**
	 * 传感器Id
	 */
	private String sId;

	/**
	 * 下发值类型：0开关量；1模拟量；2一键强排
	 */
	private Integer vType;

	/**
	 * 开关量：0打开，1关闭
	 */
	private Integer switchFlag;

	private Integer sValue;

	/**
	 * 一键强排：0打开，1关闭
	 */
	private Integer sStatus;

	/**
	 * 记录下发状态：0未下发，1已经下发
	 */
	private int recStatus;

	/**
	 * 
	 */
	private Date recCreateTime;

	/**
	 * 记录更新时间
	 */
	private Date recUpdateTime;

	/**
	 * 管理员ID
	 */
	private String oId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getrId() {
		return rId;
	}

	public void setrId(String rId) {
		this.rId = rId;
	}

	public String getcCode() {
		return cCode;
	}

	public void setcCode(String cCode) {
		this.cCode = cCode;
	}

	public String getcId() {
		return cId;
	}

	public void setcId(String cId) {
		this.cId = cId;
	}

	public String getsCode() {
		return sCode;
	}

	public void setsCode(String sCode) {
		this.sCode = sCode;
	}

	public String getsId() {
		return sId;
	}

	public void setsId(String sId) {
		this.sId = sId;
	}

	public Integer getvType() {
		return vType;
	}

	public void setvType(Integer vType) {
		this.vType = vType;
	}

	public Integer getSwitchFlag() {
		return switchFlag;
	}

	public void setSwitchFlag(Integer switchFlag) {
		this.switchFlag = switchFlag;
	}

	public Integer getsValue() {
		return sValue;
	}

	public void setsValue(Integer sValue) {
		this.sValue = sValue;
	}

	public Integer getsStatus() {
		return sStatus;
	}

	public void setsStatus(Integer sStatus) {
		this.sStatus = sStatus;
	}

	public int getRecStatus() {
		return recStatus;
	}

	public void setRecStatus(int recStatus) {
		this.recStatus = recStatus;
	}

	public Date getRecCreateTime() {
		return recCreateTime;
	}

	public void setRecCreateTime(Date recCreateTime) {
		this.recCreateTime = recCreateTime;
	}

	public Date getRecUpdateTime() {
		return recUpdateTime;
	}

	public void setRecUpdateTime(Date recUpdateTime) {
		this.recUpdateTime = recUpdateTime;
	}

	public String getoId() {
		return oId;
	}

	public void setoId(String oId) {
		this.oId = oId;
	}

}
