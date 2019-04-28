<template>
	<view>
		<view class="container">
			<view class="item">
				<input
					class="item-input"
					maxlength="11"
					type="number"
					placeholder="请输入手机号"
					@input="changePhone"
				/>
			</view>

			<view class="item">
				<input
					class="item-input"
					maxlength="6"
					type="number"
					placeholder="请输入短信验证码"
					@input="changeSmsCode"
				/>
				<button
					class="inline-button"
					hover-class="hover"
					@tap="getSmsCode"
					:disabled="countDownDisabled"
				>
					{{ countDownText }}
				</button>
			</view>
			
			<view class="item">
				<input
					class="item-input"
					maxlength="8"
					type="text"
					placeholder="请输入邀请码(选填)"
					@input="changePromotionCode"
				/>
			</view>
			<view class="item">
				<button class="item-button" hover-class="hover" @tap="register">绑定</button>
			</view>

		</view>
	</view>
</template>
<script>
let countDown = 60;

export default {
	components:{
	},
	data() {
		return {
			countDownText: '获取',
			countDownDisabled: false,

			phone: '',
			password: '',
			smsCode: '',
			pcode: '',
			userId: 0,
			promotionCode: ""
		};
	},
	onLoad(option) {
		if(option.pcode != null){
			this.pcode = option.pcode;
		}
		if(option.userId != null){
			this.userId = option.userId;
		}
	},
	methods: {
		changePhone(event) {
			this.phone = event.detail.value;
		},
		changePassword(event) {
			this.password = event.detail.value;
		},
		changeSmsCode(event) {
			this.smsCode = event.detail.value;
		},
		changePromotionCode(event) {
			this.promotionCode = event.detail.value;
		},
		getSmsCode() {
			let _self = this;
			if (this.phone.length != 11) {
				uni.showToast({
					title: '请输入11位手机号码',
					icon: 'none'
				});
				return;
			}

			this.$api
				.get('api/bootstrap/sendSmsCode', {
					phone: _self.phone
				})
				.then(res => {
					if (_self.common.Response.isFaild(res.data)) {
						uni.showToast({
							title: '获取失败',
							icon: 'none'
						});
						return;
					}

					uni.showToast({
						title: '已发送, 请注意查收!',
						icon: 'none'
					});

					_self.countDownDisabled = true;
					_self.countDown = 60;
					setInterval(() => {
						if (_self.countDown <= 0) {
							_self.countDownText = '获取';
							_self.countDownDisabled = false;
							return;
						}
						_self.countDown--;
						_self.countDownText = _self.countDown + 's';
					}, 1000);
				});
		},
		register() {
			if (this.phone.length != 11) {
				uni.showToast({
					title: '请输入11位手机号码',
					icon: 'none'
				});
				return;
			}
			if (this.smsCode.length != 6) {
				uni.showToast({
					title: '请输入6位短信验证码',
					icon: 'none'
				});
				return;
			}
			
			
			let _self = this;
			this.$api
				.post('api/bootstrap/bind', {
					userId: _self.userId,
					phone: _self.phone,
					smsCode: _self.smsCode,
					promotionCode: _self.promotionCode
				})
				.then(res => {
					if (_self.common.Response.isFaild(res.data)) {
						uni.showToast({
							title: '注册失败',
							icon: 'none'
						});
						return;
					}else if(_self.common.Response.isException(res.data)){
						uni.showToast({
							title: res.data.msg,
							icon: 'none'
						});
						return;
					}

					uni.showToast({
						title: '注册成功',
						icon: 'none'
					});

					_self.session.setValue('token', res.data.msg);

					uni.reLaunch({
						url: '../../index/index'
					});
				});
		}
	}
};
</script>
<style lang="less" scoped>
@import '../../../static/common.less';

page {
	background-color: #fff;
}

.container {
	padding: 100upx;
	height: 100%;
}

.container .item {
	width: 100%;
	display: flex;
	height: 60upx;
	line-height: 60upx;
	margin-bottom: 40upx;
}

.container .item-input {
	width: 100%;
	height: 60upx;
	padding-left: 15upx;
	border-bottom: 1px solid #cccccc;
}

.item-between {
	display: flex;
	flex-wrap: wrap;
	justify-content: space-between;
}

.item-button {
	width: 100%;
	color: #fff;
	background-color: #000;
	border-radius: 0px;
	height: 80upx;
	line-height: 80upx;
}

.hover {
	opacity: 0.8;
}

.inline-button {
	width: 140upx;
	color: #fff;
	background-color: #000;
	border-radius: 5px;
	height: 60upx;
	line-height: 60upx;
	font-size: 14px;
}

uni-button:after {
	border: 0px solid #fff !important;
}

.inline-button[disabled] {
	color: #fff;
	background-color: #000;
	opacity: 0.8;
}
</style>
