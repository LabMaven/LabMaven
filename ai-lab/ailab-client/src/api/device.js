import { callRemoteApi } from './api';
let base = 'deviceMgr';
const GET  = "get";
const POST = "POST";

// 查询房间列表
export const getRooms = params => {
    return callRemoteApi(`${base}/device/rooms`, params, GET);
}

// 查询传感器列表
export const getSensors = params => {
    return callRemoteApi(`${base}/device/sensors`, params, GET);
}

// 分页查询传感器列表
export const getSensorDevList = params => {
    return callRemoteApi(`${base}/device/sensorlist`, params, GET);
}

// 查询传感器集合列表，用于图标显示
export const getSensorCollect = params => {
    return callRemoteApi(`${base}/device/sensorcollect`, params, GET);
}

export const updateSensor = params => {
	return callRemoteApi(`${base}/device/updatesensor`, params, POST);
}

export const getDeviceTree = params => {
    return callRemoteApi(`${base}/device/getDeviceTree`, params, GET);
}

export const getChartData = params => {
	return callRemoteApi(`${base}/device/getChartData`, params, GET);
}

export const addCtlInfo = params => {
	return callRemoteApi(`${base}/ctl/add`, params, POST);
}

export const batchAddCtlInfo = params => {
	return callRemoteApi(`${base}/ctl/batchadd`, params, POST);
}

export const countSensor = params => {
	return callRemoteApi(`${base}/device/countsensor`, params, GET);
}

export const addCoordinate = params => {
	return callRemoteApi(`${base}/coordinate/add`, params, POST); 
};
