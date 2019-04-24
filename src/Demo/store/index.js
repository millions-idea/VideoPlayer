import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

const localStore = {
	setValue(key, value, callback) {
		try {
			uni.setStorageSync(key, value);
		} catch (e) {
			if (callback != null) {
				callback(null);
			}
		}
	},
	getValue(key) {
		try {
			const session = uni.getStorageSync(key);
			if (session) {
				return session;
			}
		} catch (e) {
			// error
			return null;
		}
	}
}

const defaultProfile = {
	userId: 0,
	avatar: "../../static/user/default-avatar.png",
	account: "",
	nickName: "",
	phone: "",
	exitsPayment: false,
	isVip: false,
	level: 0,
	parentId: 0,
	type: 0,
	promotionCode: "",
	financeId: "",
	financeName: "",
	wechatBusinessCard: "",
	parentUser: {
		userId: 0,
		nickName: "",
		phone: "",
		avatar: "",
		wechatBusinessCard: ""
	},
	businessSchoolLink: "",
	commissionSystemLink: "",
	agentHelpLink: "",
	communityBanner: [],
	yabo: {
		flags: "",
		status: "",
		status_code: 0,
		message: "",
		data: {
			grade: "",
			money: ""
		}
	},
	yaboUrl: ""
}

const store = new Vuex.Store({
	state: localStore.getValue('state') ? JSON.parse(localStore.getValue('state')) : {
		h5: "https://www.2stack.cn/",
		hasLogin: false,
		profile: {
			userId: 0,
			avatar: "../../static/user/default-avatar.png",
			account: "",
			nickName: "",
			phone: "",
			exitsPayment: false,
			isVip: false,
			level: 0,
			parentId: 0,
			type: 0,
			promotionCode: "",
			financeId: "",
			financeName: "",
			wechatBusinessCard: "",
			parentUser: {
				userId: 0,
				nickName: "",
				phone: "",
				avatar: "",
				wechatBusinessCard: ""
			}
		},
		businessSchoolLink: "",
		commissionSystemLink: "",
		agentHelpLink: "",
		communityBanner: [],
		yabo: {
			flags: "",
			status: "",
			status_code: 0,
			message: "",
			data: {
				grade: "",
				money: ""
			}
		},
		yaboUrl: ""
	},
	mutations: {
		setProfile(state, data) {
			state.hasLogin = true;
			state.profile = data;
		},
		login(state, data) {
			state.hasLogin = true;
		},
		sysLogout(state) {
			state.hasLogin = false;
			state.profile = defaultProfile;
		}
	},
	actions: {
		// 更新档案信息
		setProfile: (context, payload) => {
			context.commit('setProfile', payload);
		},

		// 登录
		login: (context, payload) => {
			context.commit('login', payload);
		},

		sysLogout: (context) => {
			context.commit('sysLogout');
		},

		// 打开登录模态窗口
		authOpenWindow: (context, payload) => {
			if (store.state.hasLogin) {
				Vue.prototype.common.window.toNew(payload, null);
			} else {
				Vue.prototype.common.window.toNew('user/bootstrap/login', null);
			}
		},

		authOpenWindowParams: (context, payload, params) => {
			if (store.state.hasLogin) {
				Vue.prototype.common.window.toNew(payload, params);
			} else {
				Vue.prototype.common.window.toNew('user/bootstrap/login', null);
			}
		}
	}
})

export default store
