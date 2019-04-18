let sessionKey = "user_session_key";

export default {
	asyncSetSession(value, callback) {
		uni.setStorage({
			key: sessionKey,
			data: value,
			success: function() {
				if (callback != null) {
					callback("success");
				}
			}
		});
	},
	setSession(value, callback) {
		try {
			uni.setStorageSync(sessionKey, value);
		} catch (e) {
			if (callback != null) {
				callback(null);
			}
		}
	},
	getSession() {
		try {
			const session = uni.getStorageSync(sessionKey);
			if (session) {
				return session;
			}
		} catch (e) {
			// error
			return null;
		}
	},

	clearSession() {
		try {
			uni.removeStorageSync(sessionKey)
		} catch (e) {

		}
	},

	clearState() {
		try {
			uni.removeStorageSync("state")
		} catch (e) {

		}
	},


	setValue(key, value) {
		try {
			uni.setStorageSync(key, value);
		} catch (e) {

		}
	},
	getValue(key) {
		try {
			const value = uni.getStorageSync(key);
			if (value != null) {
				return value;
			}
		} catch (e) {
			// error
			return null;
		}
	},
	removeValue(key) {
		try {
			uni.removeStorageSync(key);
		} catch (e) {

		}
	},

	clear() {
		try {
			uni.clearStorageSync();
		} catch (e) {
			// error
		}
	}
}
