<template>
	<view>
		<view class="container">
			<view class="item">
				<input
					class="item-input"
					maxlength="11"
					type="text"
					placeholder="请输入账号"
					value=""
					@input="changePhone"
				/>
			</view>

			<view class="item">
				<input
					class="item-input"
					maxlength="128"
					type="text"
					placeholder="请输入密码"
					value=""
					:password="true"
					@input="changePassword"
				/>
			</view>

			<view class="item"><button class="item-button" hover-class="hover" @tap="loginWithPassword">登录</button></view>

			<view class="item item-between">
				<view class="item-left"><navigator url="register">注册</navigator></view>
				<view class="item-right"><navigator url="find">忘记密码</navigator></view>
			</view>

		</view>
	</view>
</template>
<script>
import { mapState, mapActions, mapMutations } from 'vuex';
	
export default {
	data() {
		return {
			username: '',
			password: '',
		};
	},
	computed:{
		...mapState(['hasLogin', 'profile'])
	},
	onLoad(option){
		if(option.pop != null){
			uni.showToast({
				title: option.pop,
				mask: true,
				duration: 5000,
				icon: "none"
			});
		}
		if(option.token != null){
			let _self = this;
			uni.showToast({
				title: '登录成功',
				icon: 'none',
				success: ()=>{
					setTimeout(() => {
						_self.session.setValue('token', option.token);
						_self.login();
						uni.reLaunch({
							url: '../../index/index'
						});
					}, 2000)
				}
			});
						
		}
	},
	methods: {
		...mapActions(['setProfile', 'login', 'sysLogout']),
		changePhone(event) {
			this.username = event.detail.value;
		},
		changePassword(event) {
			this.password = event.detail.value;
		}, 
		loginWithPassword(){
			uni.showLoading({
				title: '请稍后',
				mask: false
			});
			
			let _self = this;
			if (this.username.length < 5 || this.username.length > 11) {
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
			
			this.$api
				.post('api/bootstrap/login', {
					username: _self.username,
					password: _self.password
				})
				.then(res => {
					uni.hideLoading();
					
					if (_self.common.Response.isFaild(res.data)) {
						uni.showToast({
							title: '登录失败',
							icon: 'none'
						});
						return;
					}else if(_self.common.Response.isException(res.data)){
						if(res.data.msg.indexOf("未绑定手机号") != -1){
							_self.session.setValue('token', res.data.msg.toString().split(":")[1]);
							uni.showToast({
								title: '首次登录用户需绑定手机号码',
								icon: 'none'
							});
							setTimeout(() => {
								uni.reLaunch({
									url: '/pages/user/bootstrap/bind'
								});
							}, 2000)
							return;
						}
						uni.showToast({
							title: res.data.msg,
							icon: 'none'
						});
						return;
					}
			
					uni.showToast({
						title: '登录成功',
						icon: 'none',
						success: ()=>{
							setTimeout(() => {
								_self.session.setValue('token', res.data.msg);
								_self.login();
								uni.reLaunch({
									url: '../../index/index'
								});
							}, 2000)
						}
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

.platform {
	width: 100%;
	display: flex;
	justify-content: center;
	flex-wrap: wrap;
}

.platform .icons {
	width: 100%;
	line-height: 150upx;
	display: flex;
	justify-content: center;
}

.platform-icon {
	font-size: 60upx;
}
</style>
