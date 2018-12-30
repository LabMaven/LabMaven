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

// 查询传感器图标列表
export const getSensorChartList = params => {
    return callRemoteApi(`${base}/device/sensorchartlist`, params, GET);
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



