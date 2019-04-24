<template>
	<view>
		<view class="recharge  tips-item">
			<view class="blank-line-20"></view>
			<view class="blank-line-10"></view>
			<view class="tips-border">
				<view class="tips gray">不限次数，不限金额，单笔提现费2元</view>
				<view class="tips gray">请正确填写支付宝账号和姓名，避免造成提现不到账</view>
				<view class="tips gray">提现时间：周一至周六09:00~17:30，提现当天到账。</view>
				<view class="tips gray">非工作日内提现，将由下一个工作日到账</view>
			</view>
			<view class="blank-line-20"></view>
			<view class="blank-line-10"></view>
		</view>
		<view class="blank-line-20"></view>

		<view class="recharge">
			<view class="recharge-item small-item">
				<text class="recharge-label gray">可提现余额</text>
			</view>
			<view class="recharge-item">
				<view class="money-container">
					<text class="money left balance">{{ balance }}</text>
				</view>
			</view>
		</view>

		<view class="blank-line-20"></view>

		<view class="recharge">
			<view class="recharge-item small-item">
				<text class="recharge-label gray">提现金额</text>
			</view>
			<view class="recharge-item">
				<view class="">
					<input
						class="amount"
						maxlength="9"
						type="number"
						value=""
						@input="changeInput"
					/>
				</view>
			</view>
		</view>

		<view class="blank-line-20"></view>

		<view class="recharge">
			<view class="recharge-item small-item">
				<text class="recharge-label gray">支付宝账号</text>
			</view>
			<view class="recharge-item">
				<view class="">
					<input
						class="name"
						maxlength="20"
						type="text"
						value=""
						@input="changeFinanceId"
					/>
				</view>
			</view>
		</view>
		
		<view class="blank-line-20"></view>
		
		<view class="recharge">
			<view class="recharge-item small-item">
				<text class="recharge-label gray">真实姓名</text>
			</view>
			<view class="recharge-item">
				<view class="">
					<input
						class="name"
						maxlength="10"
						type="text"
						value=""
						@input="changeRealName"
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
export default {
	data() {
		return {
			buttonDisabled: true,
			changeValue: '',
			changeName: '',
			changePassword: '',
			balance: '...',
			financeId: ''
		};
	},
	computed: {
		isInput() {
			return this.changeValue.length == 0;
		}
	},
	methods: {
		changeInput(event) {
			this.changeValue = event.detail.value;
		},
		changeRealName(event) {
			this.changeName = event.detail.value;
		},
		changeFinanceId(event) {
			this.financeId = event.detail.value;
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
		},
		nextStep() {
			this.appEvents.$on('onInputPaymentPassword', this.onInputPaymentPassword);
			this.common.window.toNew('user/index/payment', {
				formName: 'payment',
				title: '输入支付密码',
				callback: 'onInputPaymentPassword'
			});
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
					if (_self.common.Response.isFaild(res.data)) {
						uni.showToast({
							icon: 'none',
							title: '支付密码输入错误'
						});
						setTimeout(() => {
							uni.navigateBack({
								delta: 1
							});
						}, 3000);
						return;
					} else if (_self.common.Response.isException(res.data)) {
						uni.showToast({
							icon: 'none',
							title: res.data.msg
						});
						setTimeout(() => {
							uni.navigateBack({
								delta: 1
							});
						}, 3000);
						return;
					}

					//提现
					_self.$api
						.get('api/bill/withdraw', {
							amount: _self.changeValue,
							financeId: _self.financeId,
							realName: _self.changeName,
							paymentPassword: data
						})
						.then(_res => {
							uni.hideLoading();

							if (_self.common.Response.isFaild(_res.data)) {
								if (
									_res.data.msg.length < 10 &&
									_res.data.msg.indexOf('null') == -1
								) {
									uni.showToast({
										icon: 'none',
										title: _res.data.msg
									});
								} else {
									uni.showToast({
										icon: 'none',
										title: '申请提现失败'
									});
								}

								setTimeout(() => {
									uni.navigateBack({
										delta: 1
									});
								}, 3000);
								return;
							} else if (_self.common.Response.isException(_res.data)) {
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

							uni.showToast({
								icon: 'none',
								title: '申请提现成功'
							});
							setTimeout(() => {
								uni.navigateBack({
									delta: 10
								});
							}, 3000);
						});
				});
		}
	},
	onLoad() {
		this.getBalance();
	}
};
</script>

<style lang="less" scoped>
@import '../../../static/common.less';

.recharge {
	min-height: 150upx;
	width: 100%;
	overflow: hidden;
	background-color: #fff;
}

.recharge-item {
	width: 100%;
}

.small-item,
.small-item .recharge-label {
	height: 80upx;
	line-height: 80upx;
	font-size: 15px;
	font-family: @common-font-cn;
}

.recharge-label {
	font-family: @common-font-zh;
	font-size: 18px;
	padding: 30upx;
	height: 150upx;
	line-height: 150upx;
}

.tips-item {
	width: 100%;
}

.tips-border {
	width: 90%;
	margin: 0 auto;
	justify-content: center;
	border: 1px dashed #e6e6e6;
}

.tips {
	ont-family: @common-font-zh;
	width: 100%;
	font-size: 13px;
	padding: 15upx;
}

.left {
	float: left;
}

.money {
	font-size: 40px;
	height: 90upx;
	line-height: 90upx;
	margin-left: 30upx;
	padding-bottom: 30upx;
}

.amount {
	width: 100%;
	height: 100%;
	line-height: 100%;
	font-size: 30px;
	font-family: @common-font-cn;
	padding-left: 30upx;
	padding-bottom: 30upx;
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

.balance {
	font-family: @common-font-cn;
	font-size: 30px;
}

.name {
	width: 100%;
	height: 100%;
	line-height: 100%;
	font-size: 30px;
	font-family: @common-font-zh;
	width: 100%;
	padding: 30upx;
}

.gray {
	color: #888888;
}
</style>
