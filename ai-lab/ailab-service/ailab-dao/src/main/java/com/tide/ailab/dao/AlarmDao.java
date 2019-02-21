package com.tide.ailab.dao;

import com.tide.ailab.model.Alarm;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlarmDao {
    List<Alarm> getAlarmList(Alarm alarm);

     void updateAlarm(Alarm alarm);

     void insertAlarm(Alarm alarm);
}
