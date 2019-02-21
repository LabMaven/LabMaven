package com.tide.ailab.model;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

public class Alarm {
    private Long id;
    private String alarmType;
    private String alarmCode;
    private String cId;
    private String sId;
    private String alarmInfo;
    private Long aFlag;
    private String rollbackAlarmId;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date entryTime;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date rollbackTime;
    private Integer rollbackFlag;

    private String   rollbackDes;

    public String getRollbackDes() {
        return rollbackDes;
    }

    public void setRollbackDes(String rollbackDes) {
        this.rollbackDes = rollbackDes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(String alarmType) {
        this.alarmType = alarmType;
    }

    public String getAlarmCode() {
        return alarmCode;
    }

    public void setAlarmCode(String alarmCode) {
        this.alarmCode = alarmCode;
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

    public String getAlarmInfo() {
        return alarmInfo;
    }

    public void setAlarmInfo(String alarmInfo) {
        this.alarmInfo = alarmInfo;
    }

    public Long getaFlag() {
        return aFlag;
    }

    public void setaFlag(Long aFlag) {
        this.aFlag = aFlag;
    }

    public String getRollbackAlarmId() {
        return rollbackAlarmId;
    }

    public void setRollbackAlarmId(String rollbackAlarmId) {
        this.rollbackAlarmId = rollbackAlarmId;
    }

    public Date getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(Date entryTime) {
        this.entryTime = entryTime;
    }

    public Date getRollbackTime() {
        return rollbackTime;
    }

    public void setRollbackTime(Date rollbackTime) {
        this.rollbackTime = rollbackTime;
    }

    public Integer getRollbackFlag() {
        return rollbackFlag;
    }

    public void setRollbackFlag(Integer rollbackFlag) {
        this.rollbackFlag = rollbackFlag;
    }
}