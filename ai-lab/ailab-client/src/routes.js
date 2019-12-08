import Login from './views/Login.vue'
import Desk from './views/desk/Desk.vue'
import GetCoordinate from './views/desk/getCoordinate.vue'
import NotFound from './views/404.vue'
import Home from './views/Home.vue'
import DefaultPage from './views/DefaultPage.vue'

import customer from './views/user/customer.vue'
import user from './views/user/user.vue'
import role from './views/user/role.vue'

import dict from './views/config/dict.vue'

import system from './views/device/system.vue'
import deviceQry from './views/device/deviceQry.vue'
import deviceCfg from './views/device/deviceCfg.vue'
import fanCoiler from './views/device/fanCoiler.vue'

import report from './views/report/report.vue'

import hisChart from './views/report/hisChart.vue'

import alarmQry from './views/alarm/alarmQry.vue'


let routes = [{
        path: '/login',
        component: Login,
        name: '',
        hidden: true
    },
    {
        path: '/',
        component: Home,
        name: '工作台',
        children: [
            { path: '/desk', component: Desk },
            { path: '/getCoordinate', component: GetCoordinate }
        ]
    },
    {
        path: '/',
        component: Home,
        name: '',
        hidden: true,
        children: [
            { path: '/404', component: NotFound }
        ]
    },
    {
        path: '/',
        component: Home,
        name: '设备', 
        children: [        
            { path: '/system', component: system, name: '系统布局' },
            { path: '/deviceQry', component: deviceQry, name: '设备查询' },
            { path: '/deviceQry/:rId', component: deviceQry, name: '设备查询' },
            { path: '/deviceCfg', component: deviceCfg, name: '设备配置管理' }
        ]
    },
    {
        path: '/',
        component: Home,
        name: '风机盘管',
        children: [
            { path: '/fanCoiler',  component: fanCoiler, name: '风机盘管查询' }
        ]
    },
    {
        path: '/',
        component: Home,
        name: '报表',
        children: [
            { path: '/report',  component: report, name: '报表' }
        ]
    },
    {
        path: '/',
        component: Home,
        name: '历史曲线',
        children: [
            { path: '/hisChart',  component: hisChart, name: '历史曲线' }
        ]
    },
    {
        path: '/',
        component: Home,
        name: '告警',
        children: [
            { path: '/alarmQry', component: alarmQry, name: '告警查询' }
        ]
    },
    {
        path: '/',
        component: Home,
        name: '用户',
        children: [
            { path: '/user', component: user, name: '用户管理' },
            { path: '/role', component: role, name: '角色管理' }
        ]
    },
    {
        path: '/default',
        component: DefaultPage,
        name: '',
    },
    {
        path: '*',
        hidden: true,
        redirect: { path: '/404' }
    }
];

// 输出
export default routes;