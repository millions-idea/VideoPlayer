import Vue from 'vue'
import App from './App'

import store from './store/index.js'
import localStore from './store/localStore.js'

import common from "@/utils/common"
import api from '@/common/vmeitime-http/'


Vue.config.productionTip = false

Vue.prototype.$store = store;
Vue.prototype.common = common;
Vue.prototype.$api = api;
Vue.prototype.session = localStore;

App.mpType = 'app'

//全局事件, 解决不同页面之间fuction回调问题
Vue.prototype.appEvents = new Vue();

const app = new Vue({
	store,
	...App
})
app.$mount()
