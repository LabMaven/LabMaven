package com.tide.ailab.alarmmanager.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.tide.ailab.alarmmanager.configurer.Result;
import com.tide.ailab.alarmmanager.service.AlarmService;
import com.tide.ailab.common.model.JsonResult;
import com.tide.ailab.common.model.JsonResultType;
import com.tide.ailab.common.model.Page;
import com.tide.ailab.model.Alarm;
import com.tide.ailab.model.AlarmResponse;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;


/**
 * 告警操作控制器
 *
 * @author user
 */
@RestController
@RequestMapping("/alarm")
@Service
@EnableScheduling
public class AlarmController {

    @Autowired
    AlarmService alarmService;
    @Autowired
    private SimpMessageSendingOperations messageTemplate;

    /**
     * 查询告警列表
     *
     * @param page
     * @param alarm
     * @return
     */
    @ApiOperation(value = "查询告警信息", notes = "查询告警信息")
    @RequestMapping(value = "/qryAlarmList", method = RequestMethod.GET)
    public String qryAlarmList(Page page, Alarm alarm) {
        JsonResult result = new JsonResult(JsonResultType.SUCCESS);
        PageInfo<Alarm> pageInfo = alarmService.qryAlarmList(page, alarm);
        result.add("data", pageInfo);
        return result.toJSON();
    }

    @RequestMapping(value="/collectAlarmInfo", method = RequestMethod.POST)
    public AlarmResponse collectAlarmInfo(@RequestParam String alarmLevel, @RequestParam String  alarmStatus) {

        AlarmResponse alarm = new AlarmResponse();
        this.sendMessage(new Result(alarmLevel,alarmStatus));
        alarm.setErrCode(0);
        alarm.setMsg("Success !!");
        System.out.println(alarm);
        return alarm;
    }

    @RequestMapping(value="/getAlarmInfo")
    public Alarm getAlarmInfo(@RequestBody Alarm alarm ) {
        return alarm;
    }

    /**
     * 更新告警信息
     *
     * @param alarm
     * @return
     */
    @ApiOperation(value = "更新告警信息", notes = "更新告警信息")
    @RequestMapping(value = "/updateAlarm", method = RequestMethod.POST)
    public String updateAlarm(@RequestBody Alarm alarm) {
        JsonResult res = new JsonResult(JsonResultType.SUCCESS);
        alarmService.updateAlarm(alarm);
        return res.toJSON();
    }

    /**
     * 插入告警信息
     *
     * @param alarm
     * @return
     */
    @ApiOperation(value = "插入告警信息", notes = "插入告警信息")
    @RequestMapping(value = "/insertAlarm", method = RequestMethod.GET)
    public void insertAlarm(Alarm alarm) {
        alarmService.updateAlarm(alarm);
    }

    public void sendMessage(Result info) {
        if (info == null) {
            throw new NullPointerException("info object not null");
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", info.getCode());
        jsonObject.put("message", info.getMessage());
        String resStr = jsonObject.toJSONString();
        System.out.println("111"+resStr);
        messageTemplate.convertAndSend("/topic/notify", resStr);
    }

}
