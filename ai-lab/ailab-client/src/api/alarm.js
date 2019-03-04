import { callRemoteApi } from './api';
let base = 'alarmMgr';
const GET  = "get";
const POST = "POST";

// 查询传感器列表
export const qryAlarmList = params => {
    return callRemoteApi(`${base}/alarm/qryAlarmList`, params, GET);
}

export const updateAlarm = params => {
    return callRemoteApi(`${base}/alarm/updateAlarm`, params, POST);
}

