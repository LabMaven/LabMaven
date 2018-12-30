import { callRemoteApi } from './api';
let base = 'userMgr';
let cfgBase = 'cfgMgr';


export const requestLogin = params =>{
	 return callRemoteApi(`${base}/login/checklogin`, params)
    // return callRemoteApi(`${base}/login/checklogin`, params)
}


// export const requestLogin = params => {
//     return axios.post(`${base}/login/checklogin`, params).then(
//         res => res.data
//     );
// };



export const getRoleTree = params => { 
	return callRemoteApi(`${base}/role/getRoleTree`, params, "GET");
};

export const getUserList = params => {
 	return callRemoteApi(`${base}/user/list`, params, "GET");
};

export const getUserType = params => { 
	return callRemoteApi(`${base}/user/getUserType`, params, "GET");
};

export const getCustomers = params => { 
	return callRemoteApi(`${base}/customer/getCustomers`, params, "GET");
};

export const addUser = params => {
	return callRemoteApi(`${base}/user/add`, params); 
};

export const editUser = params => { 
	return callRemoteApi(`${base}/user/update`, params); 
};

export const removeUser = params => { 
	return callRemoteApi(`${base}/user/delete`, params); 
};



export const getRoleList = params => {
    return callRemoteApi(`${base}/role/list`, params, "GET");
};

export const addRole = params => {
    return callRemoteApi(`${base}/role/add`, params);
};

export const editRole = params => {
    return callRemoteApi(`${base}/role/update`, params);
};

export const removeRole = params => {
    return callRemoteApi(`${base}/role/delete`, params);
};

export const getMenuTree = params => {
    return callRemoteApi(`${base}/role/getMenuTree`, params, "GET");
};
export const getLoginUserMenu = params => {
    return callRemoteApi(`${base}/role/getLoginUserMenu`, params, "GET");
};


export const modifyPassword = params => {
    return callRemoteApi(`${base}/user/modifyPassword`, params, "POST");
};


export const getCustomerList = params => {
    return callRemoteApi(`${base}/customer/list`, params, "GET");
};

export const addCustomer = params => {
    return callRemoteApi(`${base}/customer/add`, params);
};

export const editCustomer = params => {
    return callRemoteApi(`${base}/customer/update`, params);
};

export const removeCustomer = params => {
    return callRemoteApi(`${base}/customer/delete`, params);
};
