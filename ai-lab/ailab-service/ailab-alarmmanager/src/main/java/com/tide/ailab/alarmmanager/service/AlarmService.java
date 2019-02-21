package com.tide.ailab.alarmmanager.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tide.ailab.common.model.Page;
import com.tide.ailab.common.util.StringUtil;
import com.tide.ailab.dao.AlarmDao;
import com.tide.ailab.model.Alarm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 告警相关业务处理
 *
 * @author User
 */
@Service
@EnableScheduling
public class AlarmService {

    @Autowired
    AlarmDao alarmDao;

    /**
     * 分页获取告警列表数据
     *
     * @param page
     * @param cond
     * @return PageInfo
     */
    public PageInfo<Alarm> qryAlarmList(Page page, Alarm cond) {
        PageHelper.startPage(page.getPageNum(), page.getPageSize());
        if (!StringUtil.isNullOrEmpty(page.getOrderBy())) {
            PageHelper.orderBy(page.getOrderBy());
        }

        List<Alarm> alarmList = alarmDao.getAlarmList(cond);

        return new PageInfo<Alarm>(alarmList);
    }

    /**
     * 更新告警数据
     *
     * @param page
     * @param cond
     * @return PageInfo
     */
    public void updateAlarm(Alarm cond) {
        alarmDao.updateAlarm(cond);
    }


    /**
     * 更新告警数据
     *
     * @param page
     * @param cond
     * @return PageInfo
     */
    public void insertAlarm(Alarm cond) {
        alarmDao.insertAlarm(cond);
    }
}
