import axios from 'axios';
var http = require('http');
const ERROE_404 = 404;
const ERROE_405 = 405;
const ERROE_500 = 500;
const ERROE_504 = 504;

// 拦截器，拦截每一个请求
axios.interceptors.request.use(
    config => {
        config.headers.Token = localStorage.getItem("token");
        return config;
    }
);

export const callRemoteApi = (url, para = {}, method = 'post') => {
    //防止缓存，增加时间戳
    var time = Date.parse(new Date());
    url = url + "?time=" + time;
    if (method == "post" || method == "POST") {
        return axios.post(url, para).then(res => res.data, error => { return handleError(error) })
    } else {
        return axios.get(url, { params: para }).then(res => res.data, error => { return handleError(error) })
    }
    // 2种赋值的区别？？？
}

export const handleError = error => {
    let errorMsg = error.toString();
    let msg = errorMsg;
    if (errorMsg.includes(ERROE_404)) {
        msg = '接口方法不存在'
    }
    if (errorMsg.includes(ERROE_405)) {
        msg = '请求方法错误（get/post）'
    }
    if (errorMsg.includes(ERROE_500)) {
        msg = '服务器内部错误'
    }
    if (errorMsg.includes(ERROE_504)) {
        msg = '请求超时，请确认服务是否开启'
    }

    return {
        code: -1,
        msg
    }
}
// 404 xhr.js?14ed:175 POST http://localhost:8080/cfg/devicetype/addDeviceType3 404 (Not Found)
// 405 xhr.js?14ed:175 POST http://localhost:8080/cfg/devicetype/hello 405 (Method Not Allowed) 资源被禁止（请求方式不对）
// 500 POST http://localhost:8080/cfg/devicetype/addDeviceType 500 (Server Error) 服务器内部错误
// 504 POST http://localhost:8080/login/checklogin 504 (Gateway Timeout) 网管超时