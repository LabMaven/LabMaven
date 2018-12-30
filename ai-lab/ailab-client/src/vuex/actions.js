//test
import { getLoginUserMenu } from '../api/user';

export const increment = ({ commit }) => {
    commit('INCREMENT')
}
export const decrement = ({ commit }) => {
    commit('DECREMENT')
}
let defaultMenu = [{
    path: '/',
    name: '工具',
    iconCls: 'el-icon-message', //图标样式class
    children: [
        { path: '/deviceQry', name: '设备查询' },
    ]
}, {
    path: '/',
    name: '参考界面',
    iconCls: 'fa fa-address-card', //图标样式class
    children: [
        { path: '/main', name: '主页', hidden: true },
        { path: '/table', name: 'Table' },
        { path: '/form', name: 'Form' },
        { path: '/user', name: '列表' },
    ]
}]
export const initLoginUserMenu = ({ commit, state }, param) => {
    //state.user_menu = defaultMenu;
    getLoginUserMenu(param).then(res => {
        let loginUserMenu = [];
        var { code, msg, data } = res;

        if (code == 0) {
            data.forEach((item, index) => {
                let { label: name, path, children, style: iconCls } = item;
                let childrenTemp = [];
                children && (children.forEach((cItem, cIndex) => {
                    let { label: name, path } = cItem;
                    let childrenItem = { name, path };
                    childrenTemp.push(childrenItem);
                }))
                let child = {};
                if (childrenTemp.length > 0) {
                    child = { name, path, iconCls: iconCls, children: childrenTemp, leaf: false };
                } else {
                    child = { name: '', path: '/', iconCls, leaf: true, children: [{ name, path }] };
                }

                loginUserMenu.push(child);
            })
            state.user_menu = loginUserMenu;
        }
    });
}