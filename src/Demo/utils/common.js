export default {

	window: {
		logger(text) {
			if (process.env.NODE_ENV === 'development') {
				alert(text);
			}
		},
		toNew(name, params) {
			let toUrl = "/pages/" + name;
			if (params != null) {
				toUrl += "?" + Object.keys(params).map(function(key) {
					return key + "=" + encodeURIComponent(params[key]); //encodeURIComponent
				}).join("&");
			}
			uni.navigateTo({
				url: toUrl
			})
		},
		reLaunch(name, params) {
			let toUrl = "/pages/" + name;
			if (params != null) {
				toUrl += "?" + Object.keys(params).map(function(key) {
					return key + "=" + encodeURIComponent(params[key]); //encodeURIComponent
				}).join("&");;
			}
			uni.reLaunch({
				url: toUrl
			})
		}
	},

	String: {
		//首字母转大写
		toUpperCaseFirst(text) {
			return text.toLowerCase().replace(/( |^)[a-z]/g, (L) => L.toUpperCase());
		},
		//断文器-超出多少字符显示省略号
		textLimit(text, limit) {
			return text.length > limit ? text.substr(0, limit) + "..." : text;
		}
	},

	Response: {
		isFaild(data) {
			if (data == null || data.code == null) return true;
			if (data.code == 400) return true;
			return false;
		},
		isException(data) {
			if (data == null || data.code == null) return true;
			if (data.code == 500 || data.code == 300) return true;
			return false;
		}
	}
}
