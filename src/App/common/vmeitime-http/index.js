import http from './interface';
import constant from '../constant';

/**
 * 将业务所有接口统一起来便于维护
 * 如果项目很大可以将 url 独立成文件，接口分成不同的模块
 */


http.config.baseUrl = constant.getUrl();

// 单独导出(测试接口) import {test} from '@/common/vmeitime-http/'
export const get = (url, params) => {
	/* http.config.baseUrl = "http://localhost:8080/api/"
	//设置请求前拦截器
	http.interceptor.request = (config) => {
		config.header = {
			"token": "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"
		}
	} */
	let token = "";
	
	http.interceptor.request = (config) => {
		try {
			token = uni.getStorageSync('token'); 
		} catch (e) {
			
		}
		config.header = {
			"token": token
		}
	}
	
	
	//设置请求结束后拦截器
	http.interceptor.response = (response) => {
		//判断返回状态 执行相应操作
		return response;
	}
	
	
	console.warn("get_url_" + url + "_params_" + JSON.stringify(params));

    return http.get(url, params);
}

export const post = (url, params) => {
	/* http.config.baseUrl = "http://localhost:8080/api/"
	//设置请求前拦截器
	http.interceptor.request = (config) => {
		config.header = {
			"token": "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"
		}
	} */
	
	let token = "";
	
	http.interceptor.request = (config) => {
		try {
			token = uni.getStorageSync('token'); 
		} catch (e) {
			
		}
		config.header = {
			"token": token
		}
	}
	
	//设置请求结束后拦截器
	http.interceptor.response = (response) => {
		//判断返回状态 执行相应操作
		return response;
	}
	
	console.warn("post_url_" + url + "_params_" + JSON.stringify(params));
	
	return http.post(url, params);
}

 
// 默认全部导出  import api from '@/common/vmeitime-http/'
export default {
	get,
    post
}