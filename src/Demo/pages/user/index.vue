<template>
	<view>
		<!-- top user preview info  :style="{height: previewHeight}"-->
		<view class="top-user-preview">
			<view class="blank-line-20"></view>
			<view class="blank-line-10"></view>

			<!-- #ifdef APP-PLUS -->
			<!-- status bar placeholder -->
			<view class="top-status-bar-placeholder"></view>
			<!-- status bar placeholder end -->
			<!-- #endif -->

			<!-- sample header -->
			<view class="sample-header">
				<view class="left" @tap="authOpenWindow('user/profile')">
					<view class="avatar-container"><lazy-image class="lazy-image default-avatar" :realSrc="this.profile.avatar" :placeholdSrc="this.avatar"></lazy-image></view>
					<text class="account  grace-animate">{{ queryNickName }}</text>
				</view>
				<view class="right">
					<i v-if="hasLogin" :class="['iconfont', 'icon-bianji1', 'header-icon', 'header-icon-settings', 'grace-animate', shakeAnimation]" @tap="shakeAndTo()"></i>
					<i v-if="hasLogin && !profile.isVip" class="iconfont icon-vip header-icon header-icon-vip " @tap="vipTip"></i>
				</view>
			</view>
			<!-- sample header end -->

			<!-- finance data -->
			<view class="blank-line-20"></view>
			<view class="blank-line-20"></view>
			<view class="finance-preview">
				<view class="item-line item-inline">
					<view class="inline-item">
						<view class="item-label"><text class="title">亚博币</text></view>
						<text class="balance" @tap="finance()">{{ wallet.balance }}</text>
					</view>

					<view class="inline-item">
						<view class="item-label"><text class="title">亚博账户</text></view>
						<text class="balance" @tap="finance()">{{ yabo.money }}</text>
					</view>

					<view class="inline-item">
						<view class="item-label"><text class="title">累计观看</text></view>
						<text class="balance" @tap="finance()">{{ wallet.playCount }}</text>
					</view>

					<view class="inline-item">
						<view class="item-label"><text class="title">累计消费</text></view>
						<text class="balance" @tap="finance()">{{ wallet.consumeCount }}</text>
					</view>
				</view>
			</view>
			<!-- finance data end -->
		</view>
		<!-- top user preview end -->

		<view class="nav-container">
			<uni-grid
				:data="[
					{ image: '../../static/user/lishi.png', text: '观看历史' },
					{ image: '../../static/user/shouchang.png', text: '我的收藏' },
					{ image: '../../static/user/goumai.png', text: '我的购买' },
					{ image: '../../static/user/web.png', text: '进入亚博' }
				]"
				show-border="false"
				column-num="4"
				@click="onClick"
			></uni-grid>
		</view>

		<view class="grace-list ">
			<navigator v-if="hasLogin" class="items" url="" @tap="updatePassword()">
				<view class="icons"><image class="icon" src="../../static/user/edit.png"></image></view>
				<view class="title">修改密码</view>
				<view class="arrow-right"></view>
			</navigator>
			<navigator class="items" url="" @tap="toYaboLink">
				<view class="icons"><image class="icon" src="../../static/user/income.png"></image></view>
				<view class="title">真人娱乐</view>
				<view class="arrow-right"></view>
			</navigator>
		</view>
	</view>
</template>

<script>
import { mapState, mapActions } from 'vuex';
import uniGrid from '@/components/uni-grid/uni-grid.user.vue';
import lazyImage from '@/components/lazy-image-user.vue';

export default {
	components: {
		uniGrid: uniGrid,
		lazyImage: lazyImage
	},
	data() {
		return {
			wallet: {
				balance: '0.00',
				account: '0.00',
				playCount: '0',
				consumeCount: '0.00'
			},
			yabo: {
				money: '0.00'
			},

			/* preview */
			isShow: false,
			avatar: '../../static/user/default-avatar.png',
			balance: '0.00',
			account: '0.00',
			playCount: '0',
			consumeCount: '0.00',
			yaboMoney: '0.00',
			shakeAnimation: '',
			show: false
		};
	},
	created() {
		this.appEvents.$on('onPasswordDone', this.onPasswordDone);
	},
	destroyed() {
		this.appEvents.$off('onPasswordDone');
	},
	computed: {
		...mapState(['hasLogin', 'profile']),
		isLogin() {
			return this.hasLogin;
		},
		previewHeight() {
			if (!this.hasLogin) {
				return uni.upx2px((750 * 77) / 414) + 'px';
			}
		},
		queryNickName() {
			if (this.profile.account == null || this.profile.account.length < 1) {
				return '请您先登录';
			}
			return this.profile.nickName;
		}
	},
	methods: {
		...mapActions(['setProfile', 'authOpenWindow']),
		toAgentLink() {
			this.common.window.toNew('article/list', null);
		},
		toYaboLink() {
			this.common.window.toNew('generics-webview/generics-webview', {
				url: this.profile.yaboUrl
			});
		},
		queryAsserts() {
			let _self = this;
			this.$api.get('api/user/getAssetSample', {}).then(res => {
				uni.stopPullDownRefresh(); //停止下拉刷新

				if (res.data.code == 200) {
					_self.wallet.balance = res.data.msg.balance;
					_self.wallet.account = res.data.msg.account;
					_self.wallet.playCount = res.data.msg.playCount;
					_self.wallet.consumeCount = res.data.msg.consumeCount;
				}
			});

			if (!typeof this.profile.yabo == undefined) {
				_self.yabo.money = this.profile.yabo.data.money;
			} else {
				_self.yabo.money = '0.00';
			}
		},
		refreshInfo() {
			this.session.clearSession();
			this.session.clearState();
			let session = this.session.getSession();
			if (session == null) {
				console.log('加载用户信息');
				let _self = this;
				this.$api
					.post('api/user/getProfile', {})
					.then(res => {
						uni.stopPullDownRefresh(); //停止下拉刷新
						if (_self.common.Response.isFaild(res.data)) {
							return;
						} else if (_self.common.Response.isException(res.data)) {
							if (res.data.msg.indexOf('冻结') != -1 || res.data.msg.indexOf('null') != -1) {
								if (res.data.msg.indexOf('null') != -1) {
									res.data.msg = '请重新登录';
								}
								uni.showModal({
									title: '强制退出通知',
									content: res.data.msg,
									showCancel: false,
									cancelText: '',
									confirmText: '',
									success: res => {
										this.session.clear();
										this.session.removeValue('token');
										this.sysLogout();
										setTimeout(() => {
											uni.hideLoading();
											uni.reLaunch({
												url: './bootstrap/login'
											});
										}, 1000);
									},
									fail: () => {},
									complete: () => {}
								});
							}
							return;
						}

						_self.session.setSession(res.data.msg);
						_self.setProfile(res.data.msg);
						return;
					})
					.catch(err => {
						return;
					});
			} else {
				uni.stopPullDownRefresh(); //停止下拉刷新

				console.log('更新用户信息');
				_self.setProfile(session);
			}

			this.queryAsserts();
		},
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
		/* preview */
		toggleBalance() {
			if (!this.isShow) {
				this.isShow = true;
				this.balance = this.wallet.balance;
				this.account = this.wallet.account;
				this.playCount = this.wallet.playCount;
				this.consumeCount = this.wallet.consumeCount;
				this.wallet.balance = '....';
				this.wallet.account = '....';
				this.wallet.playCount = '....';
				this.wallet.consumeCount = '....';
			} else {
				this.isShow = false;
				this.wallet.balance = this.balance;
				this.wallet.account = this.account;
				this.wallet.playCount = this.playCount;
				this.wallet.consumeCount = this.consumeCount;
			}
		},
		shakeAndTo() {
			this.shakeAnimation = 'bounceIn';
			setTimeout(() => {
				this.shakeAnimation = '';
				this.common.window.toNew('user/profile', null);
			}, 100);
		},
		buyVip() {
			if (!this.hasLogin) {
				this.common.window.toNew('user/bootstrap/login', null);
				return;
			}
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
				this.common.window.toNew('user/order/payment', res.data.msg);
			});
		},

		vipTip() {
			uni.showToast({
				title: '尊敬的VIP会员您好!',
				icon: 'none',
				duration: 3000
			});
		},

		showBanner: function() {
			this.show = true;
		},
		closeBanner: function() {
			this.show = false;
		},
		tap: function() {},
		finance() {
			this.authOpenWindow('user/finance');
		},
		/* navigation */
		onClick(e) {
			switch (e.index) {
				case 0:
					this.authOpenWindow('user/history');
					break;
				case 1:
					this.authOpenWindow('user/collect');
					break;
				case 2:
					this.authOpenWindow('user/shopping');
					break;
				case 3:
					this.common.window.toNew('generics-webview/generics-webview', {
						url: this.profile.yaboUrl
					});
					break;
			}
		}
	},
	onLoad() {
		this.refreshInfo();
	},
	onShow() {
		uni.startPullDownRefresh();
	},

	onPullDownRefresh() {
		//监听下拉刷新动作
		console.log('onPullDownRefresh');
		// 这里获取数据
		this.refreshInfo();
	}
};
</script>

<style lang="less">
@import '../../static/common.less';

@icon-size: 30upx;

.grace-list {
	padding: 0 0;
}

.light-grace-list {
	border-top: 1px solid #f3f3f3 !important;
	border-bottom: 1px solid #f3f3f3 !important;
}

.grace-list .items .icons {
	width: @icon-size !important;
	height: @icon-size !important;
	line-height: @icon-size !important;
}

.grace-list .items .icons uni-image {
	width: @icon-size !important;
	height: @icon-size !important;
}

/* #ifdef APP-PLUS */
@app-icon-size: 40upx;

.grace-list .items .icons {
	width: @app-icon-size !important;
	height: @app-icon-size !important;
	line-height: @app-icon-size !important;
}

.grace-list .items .icons uni-image {
	width: @app-icon-size !important;
	height: @app-icon-size !important;
}
/* #endif */

.grace-list .items .title {
	font-size: 14px;
	font-family: @normal-text-font;
	line-height: @icon-size;
	height: @icon-size;
}

.grace-list .items .arrow-right {
}

.grace-common-mt {
	margin-top: 10px;
}

.items text {
	font-family: @normal-text-font;
}

.green {
	color: #0ea295 !important;
	margin-right: 15upx;
}

.first-grace-common-mt {
	margin-top: 0upx !important;
}

.grace-list .items .title {
	border-bottom: 1px solid #f3f3f3;
}

.red-point {
	float: right;
	display: inline-flex;
	width: 16upx;
	height: 16upx;
	border-radius: 80upx;
	position: relative;
	top: 8upx;
	background-color: #fe3d3d;
}

@import '../../graceUI/animate.css';

.pop-container {
	height: 400px;
	border-radius: 5px;
	background-color: #fff;
	overflow: hidden;
}

.pop-container .title {
	width: 100%;
	height: 80upx;
	line-height: 80upx;
	background-color: #fafafa;
	color: #000;
	text-align: center;
	font-family: @common-font-zh;
}

.pop-container .nickname {
	width: 100%;
	height: 50upx;
	line-height: 50upx;
	color: #000;
	text-align: center;
	font-family: @common-font-zh;
}

.pop-container .phone {
	width: 100%;
	height: 40upx;
	line-height: 40upx;
	color: #000;
	text-align: center;
	font-family: @common-font-zh;
}

.pop-container .info {
	margin-top: 30upx;
	height: 100upx;
	line-height: 100upx;
	display: flex;
	justify-content: center;
	text-align: center;
	align-items: center;
	width: 100%;
}

.pop-container .card {
	max-height: 200px;
	width: 100%;
	display: flex;
	justify-content: center;
	text-align: center;
	margin-top: 25upx;
	border-radius: 15upx;
}

.pop-container .card-img {
	width: 200px;
	height: 200px;
	border-radius: 15upx;
}

.top-user-preview {
	width: 100%;
	height: 300upx;
	background-color: #fb7039;
	background-image: url('../../static/user/user-preview-bg.png') !important;
	background-size: 100% 300upx;
	position: relative;
	top: 0px;
	overflow: hidden;
}

/* #ifdef APP-PLUS */
.top-user-preview {
	height: 360upx;
	background-size: 100% 360upx;
}
/* #endif */

.top-status-bar-placeholder {
	min-height: var(--status-bar-height);
	background-color: rgba(255, 255, 255, 0);
}

.sample-header {
	width: 90%;
	min-height: 80upx;
	margin: 0 auto;
}

.sample-header .left,
.sample-header .right {
	overflow: hidden;
}

.sample-header .left {
	float: left;
}

.sample-header .right {
	float: right;
}

.default-avatar {
	float: left;
	width: 80upx;
	height: 80upx;
	position: absolute;
	left: 0px;
	top: 0px;
	overflow: hidden !important;
	border-radius: 60upx !important;
}

.avatar-container {
	position: relative;
	display: inline-flex;
	float: left;
	width: 80upx;
	height: 80upx;
	overflow: hidden !important;
	border-radius: 60upx !important;
}

.account {
	float: left;
	display: inline-flex;
	margin-left: 25upx;
	line-height: 80upx;
	height: 80upx;
	font-size: 30upx;
	color: @dark-text-color;
	font-family: @normal-number-font;
}

.header-icon {
	display: inline-flex;
	float: right;
	font-size: 40upx;
	line-height: 80upx;
	color: @dark-text-color;
	margin-left: 20upx;
}

.header-icon-vip {
	font-size: 50upx !important;
}

.header-icon-settings {
	font-size: 50upx !important;
}

.parent-user {
	line-height: 80upx;
	color: #ffe8e0;
	font-size: 13px;
	text-decoration: underline;
}

/* finance */
.finance-preview {
	width: 100%;
}

.finance-preview .item-line {
	width: 90%;
	height: 120upx;
	margin: 0 auto;
	white-space: nowrap;
}

.item-line .title {
	height: 24upx;
	line-height: 24upx;
}

.finance-preview .item-label {
	font-family: @normal-text-font;
	color: rgba(255, 255, 255, 0.6);
	font-size: 24upx;
}

.lock-eye {
	display: inline-flex;
	margin-left: 30upx;
	color: @dark-text-color;
	font-size: 40upx !important;
	position: relative;
	top: 5upx;
}

.balance {
	color: rgb(255, 255, 255);
	font-family: @common-font-num;
	font-size: 70upx;
}

.item-inline {
	width: 90%;
	margin: 0 auto;
}

.inline-item .item-label {
	width: 100%;
	height: 36upx;
}

.inline-item .item-label .title {
	display: flex;
	flex-wrap: wrap;
	width: 100%;
	height: 36upx;
}

.inline-item .balance {
	display: flex;
	flex-wrap: wrap;
	width: 100%;
	font-size: 40upx;
	line-height: 84upx;
}

.inline-item {
	clear: both;
	display: inline-block;
	width: 28.4%;
	min-height: 120upx;
}

/* finance end */

/* navigation */

.nav-container {
	min-height: 150upx;
	max-height: 150upx;
	height: 150upx;
	background-color: #fff !important;
}
</style>
