<template>
	<view>
		<view class="grace-list">
			<navigator class="items avatar-upload" @tap="uploadAvatar">
				<view class="title">
					<image class="avatar" :src="profile.avatar"></image>
					<text :class="['foot', 'gray']">上传头像</text>
				</view>
				<view class="arrow-right"></view>
			</navigator>
			<navigator class="items" @tap="formOpenWindow('nickName')">
				<view class="title">
					<text class="head">名字</text>
					<text :class="['foot', 'black']">{{ profile.nickName }}</text>
				</view>
				<view class="arrow-right"></view>
			</navigator>
			<navigator class="items" @tap="formOpenWindow('phone')">
				<view class="title">
					<text class="head">手机号</text>
					<text :class="getPhoneClassName">{{ phone }}</text>
				</view>
				<view class="arrow-right"></view>
			</navigator>
			<navigator class="items" @tap="formOpenWindow('payment')">
				<view class="title">
					<text class="head">支付密码</text>
					<text :class="getPaymentClassName">{{ payment }}</text>
				</view>
				<view class="arrow-right"></view>
			</navigator>
			<navigator class="items" @tap="updatePassword()">
				<view class="title">
					<text class="head">登录密码</text>
					<text class="foot gray">去修改</text>
				</view>
				<view class="arrow-right"></view>
			</navigator>
			<navigator class="items" @tap="formOpenWindow('team')">
				<view class="title">
					<text class="head">我的团队</text>
					<text class="foot gray">看看你推荐了多少会员吧</text>
				</view>
			</navigator>
			<navigator class="items" @tap="formOpenWindow('bill')">
				<view class="title">
					<text class="head">佣金明细</text>
					<text class="foot gray">看看你今天赚了多少钱吧</text>
				</view>
			</navigator>
			<navigator class="items" @tap="logout">
				<view class="title"><text class="head">注销登录</text></view>
			</navigator>
		</view>
	</view>
</template>

<script>
import { mapState, mapActions, mapMutations } from 'vuex';
import http from '../../common/vmeitime-http/interface.js';

export default {
	components: {
		http
	},
	onLoad() {
		this.queryPhone();
		this.queryPayment();
		this.queryWechat();
	},
	computed: {
		...mapState(['hasLogin', 'profile']),
		getPhoneClassName() {
			return 'foot ' + this.phoneClassName;
		},
		getPaymentClassName() {
			return 'foot ' + this.paymentClassName;
		},
		getWechatClassName() {
			return 'foot ' + this.wechatClassName;
		}
	},
	methods: {
		...mapActions(['setProfile', 'login', 'sysLogout']),
		updatePassword() {
			this.common.window.toNew('generics-form/generics-form', {
				formName: 'Password',
				title: '修改登录密码',
				topLabel: '需要接收短信验证码进行身份验证',
				bottomLabel: '为确保您账户的安全及正常使用，依《网络安全法》相关要求，6月1日起会员账户需绑定手机。如您还未绑定，请尽快完成，感谢您的理解及支持！ ',
				placeholder: '请输入新的登录密码',
				maxLength: 128,
				minLength: 6,
				success: 'onPasswordDone',
				type: 'password'
			});
		},
		queryPhone() {
			if (this.profile.phone == null || this.profile.phone.length == 0) {
				this.phoneClassName = 'gray';
				this.phone = '点击绑定';
			} else {
				this.phoneClassName = 'black';
				this.phone = this.profile.phone;
			}
		},
		queryPayment() {
			if (!this.profile.exitsPayment) {
				this.paymentClassName = 'gray';
				this.payment = '未设置';
			} else {
				this.paymentClassName = 'green';
				this.payment = '账户安全保障中';
			}
		},
		queryWechat() {
			if (this.profile.wechatBusinessCard == null || this.profile.wechatBusinessCard.length == 0) {
				this.wechatClassName = 'gray';
				this.wechat = '未绑定';
			} else {
				this.wechatClassName = 'gray';
				this.wechat = '点击查看';
			}
		},
		formOpenWindow(name) {
			let formComponentName = 'generics-form/generics-form';
			if (name == 'nickName') {
				this.common.window.toNew(formComponentName, {
					formName: 'nickName',
					title: '修改名字',
					topLabel: '2-24个字符，严禁出现违规内容，违者封号',
					value: this.profile.nickName,
					maxLength: 24,
					minLength: 2,
					success: 'onNickNameChange'
				});
			} else if (name == 'phone') {
				this.common.window.toNew(formComponentName, {
					formName: 'phone',
					title: '绑定手机号',
					topLabel: '需要接收短信验证码进行身份验证',
					bottomLabel: '为确保您账户的安全及正常使用，依《网络安全法》相关要求，6月1日起会员账户需绑定手机。如您还未绑定，请尽快完成，感谢您的理解及支持！ ',
					value: this.profile.phone,
					maxLength: 11,
					minLength: 11,
					success: 'onPhoneChange',
					type: 'number'
				});
			} else if (name == 'payment') {
				let title = '设置支付密码';
				if (this.profile.exitsPayment) {
					title = '修改支付密码';
				}
				this.common.window.toNew('user/payment', {
					formName: 'payment',
					title: title
				});
			} else if (name == 'team') {
				this.common.window.toNew('user/team', null);
			} else if (name == 'bill') {
				this.common.window.toNew('user/bill', null);
			}
		},
		onSmsPasswordDone(data) {
			let arr = data.split(',');

			//phone,code
			this.$api
				.post('api/user/changePassword', {
					smsCode: arr[1],
					password: arr[2]
				})
				.then(res => {
					if (this.common.Response.isFaild(res.data)) {
						uni.showToast({
							title: '获取验证码失败',
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

					uni.showToast({
						icon: 'none',
						title: '登录密码修改成功'
					});

					setTimeout(() => {
						this.logout();
					}, 1000);
				});
		},
		onReSendPasswordSmsCode(data) {
			let _self = this;
			this.$api
				.get('api/user/sendPasswordSmsCode', {
					phone: data
				})
				.then(res => {
					if (res.data.code != 200) {
						uni.showToast({
							title: '获取验证码失败',
							icon: 'none'
						});
						setTimeout(() => {
							_self.appEvents.$emit('close__sms__' + 'Password');
						}, 2000);
						return;
					}
				});
		},
		onPasswordDone(data) {
			//发送短信验证

			if (this.tapHz == 1) {
				uni.showToast({
					title: '操作过于频繁',
					icon: 'none'
				});
				setTimeout(() => {
					this.tapHz = 0;
				}, 10000);
				return;
			}

			uni.showLoading({
				title: '请稍后',
				icon: 'none'
			});
			this.tapHz = 1;

			this.$api
				.get('api/user/sendPasswordSmsCode', {
					phone: this.profile.phone
				})
				.then(res => {
					uni.hideLoading();
					this.tapHz = 0;

					if (this.common.Response.isFaild(res.data)) {
						uni.showToast({
							title: '获取验证码失败',
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

					//发送短信验证
					this.common.window.toNew('generics-sms/generics-sms', {
						formName: 'Password',
						phone: this.profile.phone,
						ext: JSON.stringify({
							password: data
						}),
						success: 'onSmsPasswordDone',
						reTry: 'onReSendPasswordSmsCode'
					});
				});
		},
		onNickNameChange(data) {
			let _self = this;
			uni.showLoading({
				title: '请稍后'
			});

			this.$api
				.post('api/user/editNickName', {
					nickName: data
				})
				.then(res => {
					uni.hideLoading();

					if (_self.common.Response.isFaild(res.data)) {
						uni.showToast({
							icon: 'none',
							title: '修改昵称失败'
						});
						return;
					} else if (_self.common.Response.isException(res.data)) {
						uni.showToast({
							icon: 'none',
							title: res.msg
						});
						return;
					}

					_self.profile.nickName = data;
					_self.appEvents.$emit('close__' + 'nickName');
					setTimeout(() => {
						uni.showToast({
							icon: 'none',
							title: '名字修改成功'
						});
					}, 1000);
				});
		},
		onPhoneChange(data) {
			let _self = this;
			if (data.length != 11) {
				uni.showToast({
					title: '请输入11位手机号码',
					icon: 'none'
				});
				return;
			}

			this.$api
				.get('api/user/sendChangePhoneSmsCode', {
					phone: data
				})
				.then(res => {
					if (_self.common.Response.isFaild(res.data)) {
						uni.showToast({
							title: '获取失败',
							icon: 'none'
						});
						return;
					}

					this.common.window.toNew('generics-sms/generics-sms', {
						formName: 'bindPhone',
						phone: data,
						success: 'onSMSbindPhoneDone',
						reTry: 'onReSendSmsCode'
					});
				});
		},
		onReSendSmsCode(data) {
			let _self = this;
			this.$api
				.get('api/user/sendChangePhoneSmsCode', {
					phone: data
				})
				.then(res => {
					if (res.data.code != 200) {
						uni.showToast({
							title: '获取失败',
							icon: 'none'
						});
						setTimeout(() => {
							_self.appEvents.$emit('close__sms__' + 'bindPhone');
						}, 2000);
						return;
					}
				});
		},
		onSMSbindPhoneDone(data) {
			let arr = data.split(',');

			let _self = this;
			this.$api
				.post('api/user/changePhone', {
					phone: arr[0],
					smsCode: arr[1]
				})
				.then(res => {
					if (_self.common.Response.isFaild(res.data)) {
						uni.showToast({
							title: '获取失败',
							icon: 'none'
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

					uni.showToast({
						icon: 'none',
						title: '绑定成功'
					});

					setTimeout(() => {
						_self.profile.phone = arr[0];
						_self.profile.account = res.data.msg.account;
						_self.session.setValue('token', res.data.msg.token);
						_self.appEvents.$emit('close__sms__' + 'bindPhone');
						_self.appEvents.$emit('close__' + 'phone');
					}, 3000);
				});
		},
		uploadAvatar() {
			let _self = this;
			uni.chooseImage({
				success: chooseImageRes => {
					const tempFilePaths = chooseImageRes.tempFilePaths;
					uni.uploadFile({
						url: http.config.baseUrl + 'api/user/uploadAvatar',
						filePath: tempFilePaths[0],
						name: 'file',
						formData: {
							token: _self.session.getValue('token')
						},
						success: uploadFileRes => {
							let data = JSON.parse(uploadFileRes.data);
							_self.profile.avatar = data.msg;
						}
					});
				}
			});
		},
		logout() {
			uni.showLoading({
				title: '请稍后',
				mask: true
			});
			this.session.clear();
			this.session.removeValue('token');
			this.hasLogin = false;
			this.sysLogout();
			setTimeout(() => {
				uni.hideLoading();
				uni.reLaunch({
					url: '/pages/user/bootstrap/login'
				});
			}, 2000);
		}
	},
	created() {
		this.appEvents.$on('onNickNameChange', this.onNickNameChange);
		this.appEvents.$on('onPhoneChange', this.onPhoneChange);
		this.appEvents.$on('onBindPhoneSmsChange', this.onBindPhoneSmsChange);
		this.appEvents.$on('onSMSbindPhoneDone', this.onSMSbindPhoneDone);
		this.appEvents.$on('onReSendSmsCode', this.onReSendSmsCode);
		this.appEvents.$on('onPasswordDone', this.onPasswordDone);
		this.appEvents.$on('onSmsPasswordDone', this.onSmsPasswordDone);
		this.appEvents.$on('onReSendPasswordSmsCode', this.onReSendPasswordSmsCode);
	},
	destroyed() {
		this.appEvents.$off('onNickNameChange');
		this.appEvents.$off('onPhoneChange');
		this.appEvents.$off('onBindPhoneSmsChange');
		this.appEvents.$off('onSMSbindPhoneDone');
		this.appEvents.$off('onReSendSmsCode');
		this.appEvents.$off('onPasswordDone');
		this.appEvents.$off('onSmsPasswordDone');
		this.appEvents.$off('onReSendPasswordSmsCode');
	},
	data() {
		return {
			avatar: '../../static/user/default-avatar.png',
			phoneClassName: '',
			paymentClassName: '',
			wechatClassName: '',
			phone: '',
			payment: '',
			wechat: '',
			tapHz: 0
		};
	}
};
</script>

<style lang="less" scoped>
.grace-list .items .title .foot {
	float: none !important;
	margin-left: 100upx;
	font-size: 13px !important;
	color: #000000;
}

.grace-list .items .title {
	margin-left: 0px;
	color: #9a9a9a !important;
	font-size: 14px;
	border-bottom: 1px solid #f3f3f3 !important;
}

.black {
	color: #000000 !important;
}

.gray {
	color: #9a9a9a !important;
}

.green {
	color: #0ea295 !important;
}

.grace-list .items .title .head {
	width: 90upx;
	float: left;
}

.icon-qrcode {
	font-size: 19px;
	font-style: normal;
	height: 56px;
	line-height: 56px;
	text-align: center;
	width: 30px;
	position: absolute;
	z-index: 1;
	right: 0;
	top: 0;
	color: #b2b2b6;
}

.avatar-upload {
	height: 150upx;
	overflow: hidden;
	line-height: 150upx;
}

.avatar-upload .title {
	height: 80upx;
	line-height: 80upx;
	float: right;
}

.avatar-upload .foot {
	margin-left: 90upx !important;
}

.avatar {
	width: 120upx;
	height: 120upx;
	border-radius: 120upx;
	position: relative;
	top: -15px;
	left: 10px;
	float: left;
}
</style>
