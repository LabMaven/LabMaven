import { callRemoteApi } from './api';
let base = 'orderMgr';
const GET  = "get";
const POST = "POST";

// 查询订单列表
export const getOrderList = params => {
    return callRemoteApi(`${base}/order/list`, params, GET);
}

// 查询订单详情
export const getOrder = params => {
    return callRemoteApi(`${base}/order/getorder`, params, GET);
}

// 新增订单
export const addOrder = params => {
    return callRemoteApi(`${base}/order/add`, params, POST);
};

// 修改订单
export const updateOrder = params => {
    return callRemoteApi(`${base}/order/update`, params, POST);
};


// 查询订单状态
export const getOrderStatus = params => {
    return callRemoteApi(`${base}/order/getOrderStatus`, params, GET);
};

