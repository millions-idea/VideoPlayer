<template>
	<view>
		<view class="container">
			<view class="item"><input class="item-input" maxlength="11" type="text" placeholder="请输入用户名" @input="changePhone" /></view>

			<view class="item"><input class="item-input" maxlength="128" type="text" placeholder="请输入密码" :password="true" @input="changePassword" /></view>

			<view class="item"><input class="item-input" maxlength="8" type="text" placeholder="请输入推广码(非必填)" @input="changePromotionCode" :value="pcode" /></view>

			<view class="item"><button class="item-button" hover-class="hover" @tap="register">注册</button></view>

			<view class="item item-between">
				<view class="item-left"><navigator url="login">登录</navigator></view>
				<view class="item-right"><navigator url="find">忘记密码</navigator></view>
			</view>
		</view>
	</view>
</template>
<script>
var countDown = 60;

export default {
	components: {},
	data() {
		return {
			countDownText: '获取',
			countDownDisabled: false,

			phone: '',
			password: '',
			smsCode: '',
			pcode: ''
		};
	},
	onLoad(option) {
		if (option.pcode != null) {
			this.pcode = option.pcode;
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
		register() {
			if (this.phone.length < 5 || this.phone.length > 11) {
				uni.showToast({
					title: '请输入5-11位用户名',
					icon: 'none'
				});
				return;
			}
			if (this.password.length < 5 || this.password.length > 128) {
				uni.showToast({
					title: '请输入至少5位密码',
					icon: 'none'
				});
				return;
			}

			let _self = this;
			this.$api
				.post('api/bootstrap/register', {
					username: _self.phone,
					password: _self.password,
					promotionCode: _self.promotionCode
				})
				.then(res => {
					if (_self.common.Response.isFaild(res.data)) {
						uni.showToast({
							title: '注册失败',
							icon: 'none'
						});
						return;
					} else if (_self.common.Response.isException(res.data)) {
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

					//_self.session.setValue('token', res.data.msg);

					uni.reLaunch({
						url: '/pages/user/bootstrap/bind'
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
