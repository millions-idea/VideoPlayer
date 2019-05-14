<template>
	<view>
		<view><web-view :webview-styles="webviewStyles" :src="src"></web-view></view>
	</view>
</template>

<script>
	let pollCounter = 0;
	
	export default {
		onLoad(option) {
			this.src = option.url == null ? '' : option.url;
			this.orderId = option.orderId == null ? "" : option.orderId;
			uni.setNavigationBarTitle({
				title: option.title == null ? '请稍后' : option.title
			}); 
			//启动订单状态轮询线程
			startStatusPolling();
		},
		data() {
			return {
				src: '',
				webviewStyles: {
					progress: {
						color: '#0F8DE8'
					}
				},
				orderId: ""
			};
		},
		methods:{
			/**
			 * 订单状态轮询
			 */
			startStatusPolling(){
				var timer = setTimeout(()=>{
					pollCounter++;
					if(pollCounter > 300){
						pollCounter == 0;
						timer = null;
						return;
					}
					
					this.$api.get("api/order/getStatus", {
						orderId: this.orderId
					}).then((res) => {
						if(this.common.Response.isFaild(res)){
							uni.showToast({
								title: '查询订单状态失败',
								icon : "none"
							});
							pollCounter = pollCounter + 10;
							return;
						}else if(this.common.Response.isException(res)){
							uni.showToast({
								title: res.data.msg,
								icon : "none"
							});
							setTimeout(()=> {
								uni.navigateBack({
									delta: 2
								});
							}, 1000)
							return;
						}
						
						if(res.data.msg == "success"){
							uni.showToast({
								title: '支付成功，正在准备跳转……',
								icon: "success",
								duration: 3000
							});
							setTimeout(()=> {
								uni.navigateBack({
									delta: 2
								});
							}, 1000)
						}
						
					})
					
				}, 1000);
			}
		}
	}
</script>

<style>

</style>
