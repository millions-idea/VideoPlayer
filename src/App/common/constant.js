export const devUrl = "http://hpay.natapp1.cc/";
export const prodUrl = "http://hpay.natapp1.cc/";

export default{
	getUrl(){
		if(process.env.NODE_ENV === 'development'){
			console.log('开发环境')
			return devUrl;
		}else{
			console.log('生产环境')
			return prodUrl;
		}
	}
}