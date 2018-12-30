import { callRemoteApi } from './api';
let base = 'cfgMgr';
let userBase = 'userMgr';
const GET  = "get";
const POST = "POST";

// 查询设备列表
export const qryDeviceTypeList = params => {
    return callRemoteApi(`${base}/devicetype/qryDeviceTypeList`, params,GET)
};

// 查询设备列表（不分页）
export const qryDeviceTypeAllList = params => {
    return callRemoteApi(`${base}/devicetype/qryDeviceTypeArr`, params,GET)
};
// 新增设备类型
export const addDeviceType = params => {
    return callRemoteApi(`${base}/devicetype/addDeviceType`, params)
};
// 更新设备类型
export const editDeviceType = params => {
    return callRemoteApi(`${base}/devicetype/updateDeviceType`, params)
};
// 删除设备类型
export const delDeviceType = params => {
    return callRemoteApi(`${base}/devicetype/delDeviceType/`+params.id,{},"GET")
};

export const getMeteTree = params => { 
    return callRemoteApi(`${base}/config/getMeteTree`, params, "GET");
};


// 查询设备列表（不分页）
export const qryMetePropList = params => {
    return callRemoteApi(`${base}/meteprop/list`, params, "GET")
};
// 新增设备类型
export const addMeteProp = params => {
    return callRemoteApi(`${base}/meteprop/add`, params)
};
// 更新设备类型
export const editMeteProp = params => {
    return callRemoteApi(`${base}/meteprop/update`, params)
};
// 删除设备类型
export const delMeteProp = params => {
    return callRemoteApi(`${base}/meteprop/delete/`+params.id,{},"GET")
};

// 查询故障类型列表(分页)
export const qryFaultTypeList = params => {
    return callRemoteApi(`${base}/faulttype/qryFaultTypeList`, params,GET)
};

// 查询故障类型列表(不分页)
export const qryFaultTypeAllList = params => {
    return callRemoteApi(`${base}/faulttype/qryFaultTypeArr`, params,GET)
};

// 新增故障类型
export const addFaultType = params => {
    return callRemoteApi(`${base}/faulttype/addFaultType`, params)
};
// 更新故障类型
export const editFaultType = params => {
	console.log(params);
    return callRemoteApi(`${base}/faulttype/updateFaultType`, params)
};
// 删除故障类型
export const delFaultType = params => {
    return callRemoteApi(`${base}/faulttype/delFaultType/`+params.id,{},"GET")
};

// 获取参数配置（分页）
export const qryParamList = params => {
    return callRemoteApi(`${base}/param/getparam`, params,GET)
};

// 编辑参数配置
export const editParam = params => {
    return callRemoteApi(`${base}/param/updateparam`, params,POST)
};

export const getAreaTree = params => { 
    return callRemoteApi(`${userBase}/area/getAreaTree`, params, GET);
};

export const getCustomizedArea = params => { 
    return callRemoteApi(`${base}/customizedArea/getCustomizedArea`, params, GET);
};

export const addCustomizedArea = params => { 
    return callRemoteApi(`${base}/customizedArea/add`, params);
};

export const updateCustomizedArea = params => { 
    return callRemoteApi(`${base}/customizedArea/update`, params);
};

export const deleteCustomizedArea = params => { 
    return callRemoteApi(`${base}/customizedArea/delete`, params);
};

