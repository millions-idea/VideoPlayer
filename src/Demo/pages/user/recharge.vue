<template>
	<view>
		<view class="recharge">
			<view class="recharge-item"><text class="recharge-label">充值金额</text></view>
			<view class="recharge-item">
				<view class="money-container"><text class="money left">￥</text></view>
				<view class="amount-container"><input class="left amount" maxlength="9" type="number" value="" @input="changeInput" /></view>
			</view>
		</view>
		<view class="blank-line-50"></view>
		<button class="button" hover-class="buttonHover" :disabled="isInput" @tap="nextStep()"><text>下一步</text></button>
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
				title: '不支持的终端',
				content: '请您下载APP客户端后充值',
				showCancel: false,
				confirmText: '知道了',
				success: res => {
					uni.reLaunch({
						url: '../index/index'
					});
				},
				fail: () => {},
				complete: () => {}
			});
			return;
			// #endif

			this.useAlipay();
		},
		useAlipay(data) {
			uni.showLoading({
				title: '请稍后',
				mask: true
			});
			
			let _self = this;
			
			//下订单
			_self.$api
				.get('api/order/buy', {
					id: 0,
					channel: 'alipay',
					amount: _self.changeValue
				})
				.then(_res => {
					uni.hideLoading();
			
					if (_self.common.Response.isFaild(_res.data)) {
						if (_res.data.msg.length < 10 && _res.data.msg.indexOf('null') == -1 && _res.data.msg.indexOf('faild') == -1) {
							uni.showToast({
								icon: 'none',
								title: _res.data.msg
							});
						} else {
							uni.showToast({
								icon: 'none',
								title: '充值失败，请联系在线客服处理！'
							});
						}
			
						setTimeout(() => {
							uni.navigateBack({
								delta: 1
							});
						}, 3000);
						return;
					} else if (_self.common.Response.isException(_res.data)) {
						if (_res.data.msg == 'fail' || _res.data.msg.indexOf('null') != -1) {
							uni.showToast({
								icon: 'none',
								title: '支付失败，请联系在线客服处理！'
							});
							setTimeout(() => {
								uni.navigateBack({
									delta: 1
								});
							}, 1000);
						}
						uni.showToast({
							icon: 'none',
							title: _res.data.msg
						});
						setTimeout(() => {
							uni.navigateBack({
								delta: 1
							});
						}, 3000);
						return;
					}
			
					//发起支付
					this.$api
						.get('api/order/huPayment', {
							amount: _self.changeValue,
							outTradeNo: _res.data.msg
						})
						.then(res => {
							uni.hideLoading();
							this.common.window.toNew('generics-webview/generics-webview', {
								url: res.data,
								title: '在线充值'
							});
						});
				});
		}
		
	}
};
</script>

<style lang="less" scoped>
@import '../../static/common.less';

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
