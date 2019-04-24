<template>
	<view>
		<view class="recharge">
			<view class="recharge-item"><text class="recharge-label">充值金额</text></view>
			<view class="recharge-item">
				<view class="money-container"><text class="money left">￥</text></view>
				<view class="amount-container">
					<input
						class="left amount"
						maxlength="9"
						type="number"
						value=""
						@input="changeInput"
					/>
				</view>
			</view>
		</view>
		<view class="blank-line-50"></view>
		<button class="button" hover-class="buttonHover" :disabled="isInput" @tap="nextStep()">
			<text>下一步</text>
		</button>
	</view>
</template>

<script>
	import { mapState, mapActions } from 'vuex';

export default {
	data() {
		return {
			buttonDisabled: true,
			changeValue: ''
		};
	},
	computed: {
		...mapState(['hasLogin', 'profile']),
		isInput() {
			return this.changeValue.length == 0;
		}
	},
	destroyed() {
		this.appEvents.$off('onInputPaymentPassword');
	},
	methods: {
		...mapActions(['setProfile', 'authOpenWindow']),
		changeInput(event) {
			this.changeValue = event.detail.value;
		},
		nextStep() {
			// #ifdef H5/MP
			uni.showModal({
				title: '下载APP',
				content: '本终端只支持微信充值, 如需支付宝充值请下载APP!',
				showCancel: true,
				cancelText: '知道了',
				confirmText: '立刻下载',
				success: res => {
					if(res.confirm){
						plus.runtime.openURL(this.session.getValue("homeConfig").appDownloadLink);
					}else if(res.cancel){
						
					}
				},
				fail: () => {},
				complete: () => {},
			});
			return;
			// #endif
			
			// #ifdef APP-PLUS
			this.useAlipay();
			// #endif
			
			// #ifdef H5
			this.useWxpay();
			// #endif
		},
		useAlipay(data) {
			uni.showLoading({
				title: '请稍后',
				mask: true
			});
			 
			
			//发起支付
			this.$api.get("api/order/getRechargeOrderInfo", {
				amount: this.changeValue 
			}).then((res)=>{
				uni.hideLoading();
				if(res.data != null && res.data.length > 1){
					uni.requestPayment({
						provider: 'alipay',
						orderInfo: res.data, //微信、支付宝订单数据
						success: function(res) {
							console.log('success:' + JSON.stringify(res));
							//TODO::APP支付权限
							uni.showToast({
								icon: 'none',
								title: '支付成功, 稍后跳转……'
							});
							setTimeout(() => {
								uni.reLaunch({
									url: "../index/index"
								})
							}, 3000);
						},
						fail: function(err) {
							console.log('fail:' + JSON.stringify(err));
							
							this.session.setValue("recharge_alipay_error", JSON.stringify(err));
							
							uni.showToast({
								icon: 'none',
								title: "支付失败, 错误代码:[" + JSON.stringify(err) + "]"
							});
							setTimeout(() => {
								uni.navigateBack({
									delta: 1
								});
							}, 1000);
						}
					});
					
				}else{
					uni.hideLoading();
					uni.showToast({
						title: '拉起支付请求失败',
						icon: "none"
					});
				}
			})
			
		},
		useWxpay(data) {
			uni.showLoading({
				title: '请稍后',
				mask: true
			});
			
			//发起支付
			this.$api
				.get('api/wx/recharge', {
					totalFee: _self.amount
				})
				.then(res => {
					uni.hideLoading();
					if (res.data != null && JSON.stringify(res.data).length > 5) {
						
						try{
							WeixinJSBridge.invoke(
								'getBrandWCPayRequest',
								{
									appId: res.data.appId, //公众号名称,由商户传入
									timeStamp: res.data.timeStamp, //时间戳,自1970年以来的秒数
									nonceStr: res.data.nonceStr, //随机串
									package: res.data.package,
									signType: res.data.signType, //微信签名方式：
									paySign: res.data.paySign //微信签名
								},
								function(payResult) {
									if (payResult.err_msg == 'get_brand_wcpay_request:ok') {
										console.log('支付成功');
										//支付成功后跳转的页面
										uni.showToast({
											icon: 'none',
											title: '支付成功, 稍后跳转……'
										});
										setTimeout(() => {
											uni.reLaunch({
												url: '../index/index'
											});
										}, 3000);
									} else if (
										payResult.err_msg == 'get_brand_wcpay_request:cancel'
									) {
										console.log('支付取消');
										uni.showToast({
											icon: 'none',
											title: '支付中途取消'
										});
										setTimeout(() => {
											uni.navigateBack({
												delta: 1
											});
										}, 1000);
									} else if (payResult.err_msg == 'get_brand_wcpay_request:fail') {
										console.log('支付失败');
										//WeixinJSBridge.call('closeWindow');
							
										_self.session.setValue(
											'recharge_wxpay_error',
											JSON.stringify(err)
										);
							
										uni.showToast({
											icon: 'none',
											title: '支付失败'
										});
										setTimeout(() => {
											uni.navigateBack({
												delta: 1
											});
										}, 1000);
									} //使用以上方式判断前端返回,微信团队郑重提示：res.err_msg将在用户支付成功后返回ok,但并不保证它绝对可靠。
								}
							);
						}catch(e){
							//TODO handle the exception
							_self.common.window.logger("getBrandWCPayRequest_exception_" + JSON.stringify(e));
			
							uni.hideLoading();
							uni.showToast({
								title: '拉起支付请求失败',
								icon: 'none'
							});
						}
					} else {
						uni.hideLoading();
						uni.showToast({
							title: '拉起支付请求失败',
							icon: 'none'
						});
					}
				});
			
		}
	}
};
</script>

<style lang="less" scoped>
@import '../../../static/common.less';

.recharge {
	min-height: 300upx;
	background-color: #fff;
}

.recharge-item {
	width: 100%;
}

.recharge-label {
	font-family: @common-font-zh;
	font-size: 18px;
	padding: 30upx;
	height: 150upx;
	line-height: 150upx;
}

.left {
	float: left;
}

.money {
	font-size: 40px;
	height: 90upx;
	line-height: 90upx;
	margin-left: 30upx;
}

.amount {
	width: 100%;
	height: 100%;
	line-height: 100%;
	font-size: 50px;
	font-family: @common-font-num;
}

.amount-container {
	float: right;
	width: 80%;
	height: 90upx;
	line-height: 90upx;
	margin-right: 10upx;
}

.button {
	background-color: #0083fe;
	color: #fff;
	border-radius: 0px;
	width: 80%;
	border: 0px solid #fff !important;
}

.buttonHover {
	opacity: 0.6;
	border: 0px solid #fff !important;
}

.button:after {
	opacity: 0.6;
	border: 0px solid #fff !important;
}

.button[disabled] {
	opacity: 0.6;
	border: 0px solid #fff !important;
	background-color: #0083fe;
	color: #fff;
	border-radius: 0px;
}
</style>
