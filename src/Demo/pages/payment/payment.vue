<template>
	<view>
		<view class="recharge product">
			<view class="recharge-item"><text class="recharge-label gray">商品名称</text></view>
			<view class="recharge-item">
				<view class="money-container">
					<text class="money product-title">{{ getTitle }}</text>
				</view>
			</view>
		</view>

		<view class="recharge">
			<view class="recharge-item"><text class="recharge-label gray">付款金额</text></view>
			<view class="recharge-item">
				<view class="money-container">
					<text class="money left balance">{{ amount }}</text>
					<text class="left unit gray">元</text>
				</view>
			</view>
		</view>

		<view class="blank-line-20"></view>

		<view class="recharge">
			<view class="recharge-item"><text class="recharge-label gray">付款方式</text></view>
			<view class="recharge-item">
				<view class="grace-list ">
					<navigator v-if="amount > 0" class="items" url="" @tap="useAlipay">
						<view class="icons"><image class="icon" src="../../../static/user/payment-alipay.png"></image></view>
						<view class="title">支付宝支付(推荐)</view>
						<view class="arrow-right"></view>
					</navigator>

					<navigator class="items" url="" @tap="useBalance">
						<view class="icons"><image class="icon" src="../../../static/user/payment-master.png"></image></view>
						<view class="title">
							余额支付
							<text>{{ balance }}元</text>
						</view>
						<view class="arrow-right"></view>
					</navigator>

					<!-- #ifdef H5/MP -->
					<!-- <navigator  v-if="amount > 0" class="items" url="" @tap="useWechat">
						<view class="icons">
							<image
								class="icon"
								src="../../../static/user/payment-wechat.png"
							></image>
						</view>
						<view class="title">微信支付</view>
						<view class="arrow-right"></view>
					</navigator> -->
					<!-- #endif -->
				</view>
			</view>
		</view>
	</view>
</template>

<script>
import { mapState, mapActions } from 'vuex';
export default {
	data() {
		return {
			buttonDisabled: true,
			changeValue: '',
			title: '',
			amount: 0,
			id: '',
			balance: ''
		};
	},
	computed: {
		...mapState(['hasLogin', 'profile']),
		isInput() {
			return this.changeValue.length == 0;
		},
		getTitle() {
			return this.common.String.textLimit(this.title, 12);
		}
	},
	methods: {
		...mapActions(['setProfile', 'authOpenWindow']),
		downloadApp() {
			this.common.window.toNew('generics-webview/generics-webview', {
				url: this.session.getValue('homeConfig').appDownloadLink
			});
		},
		changeInput(event) {
			this.changeValue = event.detail.value;
		},
		useBalance() {
			this.appEvents.$off('onInputPaymentPassword', this.onInputPaymentPassword);
			this.appEvents.$on('onInputPaymentPassword', this.onInputPaymentPassword);
			this.common.window.toNew('user/payment', {
				formName: 'payment',
				title: '输入支付密码',
				callback: 'onInputPaymentPassword'
			});
		},
		useAlipay() {
			/* this.appEvents.$off(
				'onInputPaymentPasswordWithAlipay',
				this.onInputPaymentPasswordWithAlipay
			);
			this.appEvents.$on(
				'onInputPaymentPasswordWithAlipay',
				this.onInputPaymentPasswordWithAlipay
			);
			this.common.window.toNew('user/index/payment', {
				formName: 'payment',
				title: '输入支付密码',
				callback: 'onInputPaymentPasswordWithAlipay'
			}); */
			this.onInputPaymentPasswordWithAlipay();
		},
		useWechat() {
			/* this.appEvents.$off(
				'onInputPaymentPasswordWithWechat',
				this.onInputPaymentPasswordWithWechat
			);
			this.appEvents.$on(
				'onInputPaymentPasswordWithWechat',
				this.onInputPaymentPasswordWithWechat
			);
			this.common.window.toNew('user/index/payment', {
				formName: 'payment',
				title: '输入支付密码',
				callback: 'onInputPaymentPasswordWithWechat'
			}); */
			this.onInputPaymentPasswordWithWechat();
		},
		onInputPaymentPassword(data) {
			uni.showLoading({
				title: '请稍后',
				mask: true
			});

			let _self = this;

			_self.$api
				.get('api/user/verifyPaymentPassword', {
					password: data
				})
				.then(res => {
					uni.hideLoading();

					if (_self.common.Response.isFaild(res.data)) {
						uni.showToast({
							icon: 'none',
							title: '支付密码输入错误'
						});
						return;
					} else if (_self.common.Response.isException(res.data)) {
						uni.showToast({
							icon: 'none',
							title: res.data.msg
						});
						return;
					}

					//下订单
					uni.showLoading({
						title: '支付中',
						mask: true
					});

					_self.$api
						.get('api/order/buy', {
							id: _self.id,
							channel: 'balance',
							amount: _self.amount
						})
						.then(_res => {
							if (_self.common.Response.isFaild(_res.data)) {
								if (_res.data.msg.length < 10 && _res.data.msg.indexOf('null') == -1) {
									uni.showToast({
										icon: 'none',
										title: _res.data.msg
									});
								} else {
									uni.showToast({
										icon: 'none',
										title: '订单失败，请联系在线客服处理！'
									});
								}
								setTimeout(() => {
									uni.navigateBack({
										delta: 1
									});
								}, 1000);
								return;
							} else if (_self.common.Response.isException(_res.data)) {
								if (_res.data.msg == 'fail' || _res.data.msg.indexOf('null')) {
									uni.showToast({
										icon: 'none',
										title: '订单错误，请联系在线客服处理！'
									});
									setTimeout(() => {
										uni.navigateBack({
											delta: 1
										});
									}, 1000);
									return;
								}
								uni.showToast({
									icon: 'none',
									title: _res.data.msg
								});
								setTimeout(() => {
									uni.navigateBack({
										delta: 1
									});
								}, 2000);
								return;
							}

							//余额支付
							_self.$api
								.get('api/order/payment', {
									orderId: _res.data.msg,
									channel: 'balance',
									amount: _self.amount
								})
								.then(__res => {
									uni.hideLoading();

									if (_self.common.Response.isFaild(__res.data)) {
										if (__res.data.msg.length < 10 && __res.data.msg.indexOf('null') == -1 && __res.data.msg.indexOf('fail') == -1) {
											uni.showToast({
												icon: 'none',
												title: __res.data.msg
											});
										} else {
											uni.showToast({
												icon: 'none',
												title: '支付失败'
											});
										}

										setTimeout(() => {
											uni.navigateBack({
												delta: 1
											});
										}, 1000);
										return;
									} else if (_self.common.Response.isException(__res.data)) {
										uni.showToast({
											icon: 'none',
											title: __res.data.msg
										});
										setTimeout(() => {
											uni.navigateBack({
												delta: 1
											});
										}, 1000);
										return;
									}

									uni.showToast({
										icon: 'none',
										title: '支付成功, 稍后跳转……'
									});
									setTimeout(() => {
										uni.reLaunch({
											url: '/pages/user/index/index'
										});
									}, 1000);
								});
						});
				});
		},
		onInputPaymentPasswordWithAlipay(data) {
			uni.showLoading({
				title: '请稍后',
				mask: true
			});

			let _self = this;

			//下订单
			_self.$api
				.get('api/order/buy', {
					id: _self.id,
					channel: 'alipay',
					amount: _self.amount
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
								title: '订单失败，请联系在线客服处理！'
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
							amount: _self.amount,
							outTradeNo: _res.data.msg
						})
						.then(res => {
							uni.hideLoading();
							this.common.window.toNew('generics-webview/generics-webview', {
								url: res.data,
								title: '手机支付'
							});
						});
				});
		},
		onInputPaymentPasswordWithWechat(data) {
			uni.showLoading({
				title: '请稍后',
				mask: true
			});

			let _self = this;

			//下订单
			_self.$api
				.get('api/order/buy', {
					id: _self.id,
					channel: 'wxpay'
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
								title: '订单失败，请联系在线客服处理！'
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
						.get('api/wx/unifiedOrder', {
							totalFee: _self.amount,
							outTradeNo: _res.data.msg
						})
						.then(res => {
							uni.hideLoading();
							if (res.data != null && JSON.stringify(res.data).length > 5) {
								try {
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
														url: '/pages/user/index/index'
													});
												}, 3000);
											} else if (payResult.err_msg == 'get_brand_wcpay_request:cancel') {
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

												_self.session.setValue('recharge_wxpay_error', JSON.stringify(err));

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
								} catch (e) {
									//TODO handle the exception
									_self.common.window.logger('getBrandWCPayRequest_exception_' + JSON.stringify(e));

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
				});
		},
		getBalance() {
			let _self = this;
			this.$api.get('api/bill/getBalance', {}).then(res => {
				if (_self.common.Response.isFaild(res.data)) {
					uni.showToast({
						title: '获取可用余额超时',
						icon: 'none'
					});
					_self.balance = '-';
					return;
				} else if (_self.common.Response.isException(res.data)) {
					uni.showToast({
						title: res.data.msg,
						icon: 'none'
					});
					_self.balance = '-';
					return;
				}
				_self.balance = res.data.msg;
			});
		}
	},
	onShow() {
		if (!this.hasLogin) {
			uni.reLaunch({
				url: '../../user/bootstrap/login'
			});
			return;
		}
	},
	onLoad(option) {
		this.title = option.title;
		this.amount = option.amount;
		this.id = option.id;
		if (this.id == null) {
			uni.showLoading({
				title: '请稍后',
				mask: false
			});
			if (this.profile.isVip) {
				uni.hideLoading();
				uni.showToast({
					title: '您已经是VIP会员啦',
					icon: 'none'
				});
				setTimeout(() => {
					uni.navigateBack({
						delta: 1
					});
				}, 1000);
				return;
			}
			this.$api.get('api/user/getVipOrderInfo', {}).then(res => {
				uni.hideLoading();
				if (this.common.Response.isFaild(res.data)) {
					uni.showToast({
						title: '拉取服务器信息超时',
						icon: 'none'
					});
					return;
				} else if (this.common.Response.isException(res.data)) {
					uni.showToast({
						title: res.data.msg,
						icon: 'none'
					});
					return;
				}

				this.title = res.data.msg.title;
				this.amount = res.data.msg.amount;
				this.id = res.data.msg.id;
			});
		}

		this.getBalance();
	}
};
</script>

<style lang="less" scoped>
@import '../../static/common.less';

.recharge {
	min-height: 300upx;
	background-color: #fff;
}

.product {
	min-height: 200upx;
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

.product .label {
	font-family: @common-font-zh;
	font-size: 18px;
	padding: 30upx;
	height: 100upx;
	line-height: 100upx;
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
	padding: 30upx;
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

uni-button:after {
	opacity: 0.6;
	border: 0px solid #fff !important;
}

uni-button[disabled] {
	opacity: 0.6;
	border: 0px solid #fff !important;
	background-color: #0083fe;
	color: #fff;
	border-radius: 0px;
}

.balance {
	font-family: @common-font-num;
	font-size: 50px;
	color: #fe3d3d;
}

.name {
	width: 100%;
	height: 100%;
	line-height: 100%;
	font-size: 20px;
	font-family: @common-font-zh;
	width: 100%;
	padding: 30upx;
}

.gray {
	color: #888888;
}

.unit {
	height: 90upx;
	line-height: 120upx;
	margin-left: 10upx;
}

.product-title {
	font-size: 30px;
}
</style>
