import babelpolyfill from 'babel-polyfill'
import Vue from 'vue'
import VueAMap from 'vue-amap'
import App from './App'
import ElementUI from 'element-ui'
//import 'element-ui/lib/theme-default/index.css'
import './assets/theme/theme-green/index.css'
import VueRouter from 'vue-router'
import store from './vuex/store'
import Vuex from 'vuex'
//import NProgress from 'nprogress'
//import 'nprogress/nprogress.css'
import routes from './routes'
import Mock from './mock'
// Mock.bootstrap();
import 'font-awesome/css/font-awesome.min.css'

import echarts from 'echarts'

import $ from 'jquery'
import '../plugins/jquery/jquery-3.3.1.min.js'
import '../plugins/ztree/js/jquery.ztree.core.min.js'
import '../plugins/ztree/js/jquery.ztree.excheck.min.js'

Vue.use(ElementUI)
Vue.use(VueRouter)
Vue.use(Vuex)
Vue.use(VueAMap)
VueAMap.initAMapApiLoader({
  key: 'c4099e2511bed1e9b93a96e770c70398',
  plugin: ['VueAMap.Autocomplete', 'VueAMap.PlaceSearch']
})
//NProgress.configure({ showSpinner: false });

//router实例
const router = new VueRouter({
  // mode:"history",
  routes
})

// 全局前置守卫       
router.beforeEach((to, from, next) => {
  //NProgress.start();
  // 用户校验
  if (to.path == '/login') {
    sessionStorage.removeItem('user');
  }
  let user = JSON.parse(sessionStorage.getItem('user'));
  if (!user && to.path != '/login') {
    next({ path: '/login' })
  } else {
    next()
  }
})

// 后置守卫
//router.afterEach(transition => {
//NProgress.done();
//});

new Vue({
  //el: '#app',
  //template: '<App/>',
  router,
  store,
  created:function(){

  },
  //components: { App }
  render: h => h(App)
}).$mount('#app')
