<template>
	<view>
		<view class="notice">
			<uni-notice-bar 
				v-if="!this.profile.exitsPayment"
				show-icon="true" 
				single="true" 
				text="为保障资金安全, 请您设置支付密码, 在本页设置后可直接消费 !">
			</uni-notice-bar>
		</view>
		<view class="blank-line-50"></view>
		<view class="blank-line-20"></view>
		<text class="header">{{getTitle}}</text>
		<view class="blank-line-10"></view>
		<view class="blank-line-20"></view>
		<text class="desc">双重密码保护资金财产安全</text>
		<view class="blank-line-50"></view>
		<view class="sms-code-container">
			<view class="sms-code-internal-container">
				<view class="sms-code-column">
					<template v-if="isActive1">
						<input
							:id="1"
							class="sms-code-char input-focus"
							:disabled="true"
							:value="codes[0]"
							maxlength="1"
							@tap="showKeyboard"
						/>
					</template>
					<template v-else>
						<input
							:id="1"
							class="sms-code-char"
							:disabled="true"
							:value="codes[0]"
							maxlength="1"
							@tap="showKeyboard"

						/>
					</template>
				</view>
				<view class="sms-code-column">
					<template v-if="isActive2">
						<input
							class="sms-code-char input-focus"
							:disabled="true"
							:value="codes[1]"
							maxlength="1"
							@tap="showKeyboard"

						/>
					</template>
					<template v-else>
						<input
							class="sms-code-char"
							:disabled="true"
							:value="codes[1]"
							maxlength="1"
							@tap="showKeyboard"
						/>
					</template>
				</view>
				<view class="sms-code-column">
					<template v-if="isActive3">
						<input
							class="sms-code-char input-focus"
							:disabled="true"
							:value="codes[2]"
							maxlength="1"
							@tap="showKeyboard"
						/>
					</template>
					<template v-else>
						<input
							class="sms-code-char"
							:disabled="true"
							:value="codes[2]"
							maxlength="1"
							@tap="showKeyboard"
						/>
					</template>
				</view>
				<view class="sms-code-column">
					<template v-if="isActive4">
						<input
							class="sms-code-char input-focus"
							:disabled="true"
							:value="codes[3]"
							maxlength="1"
							@tap="showKeyboard"
						/>
					</template>
					<template v-else>
						<input
							:id="1"
							class="sms-code-char"
							:disabled="true"
							:value="codes[3]"
							maxlength="1"
							@tap="showKeyboard"
						/>
					</template>
				</view>
				<view class="sms-code-column">
					<template v-if="isActive5">
						<input
							class="sms-code-char input-focus"
							:disabled="true"
							:value="codes[4]"
							maxlength="1"
							@tap="showKeyboard"
						/>
					</template>
					<template v-else>
						<input
							class="sms-code-char"
							:disabled="true"
							:value="codes[4]"
							maxlength="1"
							@tap="showKeyboard"
						/>
					</template>
				</view>
				<view class="sms-code-column">
					<template v-if="isActive6">
						<input
							class="sms-code-char input-focus"
							:disabled="true"
							:value="codes[5]"
							maxlength="1"
							@tap="showKeyboard"
						/>
					</template>
					<template v-else>
						<input
							class="sms-code-char"
							:disabled="true"
							:value="codes[5]"
							maxlength="1"
							@tap="showKeyboard"
						/>
					</template>
				</view>
			</view>
		</view>
		<!-- 数字键盘组件 -->
		<graceSafeyNumberKeyboard
			:show="graceNumberKeyboardShow"
			v-on:keyboardDone="keyboardDone"
			v-on:keyboardInput="keyboardInput"
			v-on:keyboardDelete="keyboardDelete"
		></graceSafeyNumberKeyboard>
	</view>
</template>

<script>
import mhtLoader from '@/components/mht-loader/mht-loader.vue';
import graceSafeyNumberKeyboard from '@/graceUI/components/graceSafeyNumberKeyboard.vue';
import {mapState,mapActions,mapMutations} from 'vuex'
import uniNoticeBar from '@/components/uni-notice-bar/uni-notice-bar.vue';

export default {
	name: 'generics-sms',
	data() {
		return {
			title: "",
			graceNumberKeyboardShow: true,
			numberVal: '',
			activeIndex: 0,
			codes: ['', '', '', '', '', ''],
			password: "",
			callback: ""
		};
	},
	computed: {
		...mapState(["hasLogin", "profile"]),
		isActive1(){
			return this.activeIndex == 0;
		},
		isActive2(){
			return this.activeIndex == 1;
		},
		isActive3(){
			return this.activeIndex == 2;
		},
		isActive4(){
			return this.activeIndex == 3;
		},
		isActive5(){
			return this.activeIndex == 4;
		},
		isActive6(){
			return this.activeIndex == 5;
		},
		getTitle(){
			if(this.title == null || this.title.length == 0) {
				return "设置支付密码";
			}
			return this.title;
		}
	},
	components: {
		mhtLoader,
		graceSafeyNumberKeyboard,
		uniNoticeBar
	},
	onLoad(option) {
		this.title = option.title;
		this.callback = option.callback;
		uni.hideKeyboard();
	},
	onShow(){
		this.activeIndex = 0;
		this.graceNumberKeyboardShow = true;
		uni.hideKeyboard();
	},
	created() {
		this.appEvents.$on('close__payment', () => {
			uni.navigateBack({
				delta: this.delta
			});
		});
		this.appEvents.$on('close__Password', () => {
			uni.navigateBack({
				delta: this.delta
			});
		});
		this.appEvents.$on("onSMSpaymentDone", this.onSMSpaymentDone);
		this.appEvents.$on("onReSendSmsCode", this.onReSendSmsCode); 
		
	},
	destroyed() {
		this.appEvents.$off('close__payment');
		this.appEvents.$off('onSMSpaymentDone');
		this.appEvents.$off('onReSendSmsCode');
		
		
		if(this.callback != null && this.callback.length > 0){
			this.appEvents.$off(this.callback);
		}
	},
	methods: {
		...mapActions(['setProfile', 'login']),
		showKeyboard() {
			//打开数字键盘
			this.graceNumberKeyboardShow = true;
			uni.hideKeyboard();
		},
		// 监听输入
		keyboardInput(e) {
			let numberVal = this.numberVal + '' + e + ',';

			let numberArr = numberVal.split(',');

			if (numberVal.length >= 12) {
				let blankCount = 0;
				this.codes.forEach(c => {
					if(c.length == 0 || c == ""){
						blankCount++;
					}
				})
				if(blankCount != 0){
					this.codes[this.codes.length - 1] = e;
				}
				if(this.codes.length < 6){
					this.codes.push(e);
				}
				this.keyboardDone();
				return;
			}

			this.numberVal = numberVal;

			numberArr.forEach((n, i) => {
				if (n.length == 1) {
					this.codes[i] = n;
				}
			});

			if (numberArr.length > 1 && numberArr.length <= 6) {
				this.activeIndex = numberArr.length - 1;
			}
			
			this.activeIndex--;
		},
		// 监听删除
		keyboardDelete() {
			if(this.activeIndex < 0){
				this.activeIndex = 0;
				return;
			}
			let numberArr = this.numberVal.split(',');
			if (numberArr.length <= 0) {
				return;
			}
			this.activeIndex = numberArr.length - 1;
			this.numberVal = this.numberVal.substring(0, this.numberVal.length - 2);
			this.codes[this.activeIndex - 1] = "";
			if(this.activeIndex == 1){
				this.activeIndex = this.activeIndex - 1;
			}else if(this.activeIndex > 0){
				this.activeIndex = this.activeIndex - 2;
			}
		},
		// 完成事件
		keyboardDone() {
			let code = "";
			let _self = this;
			this.codes.forEach((c) => code = code + c);
			if(code.length != 6) return;
			
			uni.showLoading({
				title: '请稍后',
				icon: "none"
			});
			
			this.graceNumberKeyboardShow = false;
			this.codes = [];
			this.activeIndex = 0;
			this.numberVal = "";
			this.password = code;
			
			if(this.callback != null && this.callback.length > 0){
				uni.hideLoading();
				this.appEvents.$emit(this.callback, this.password);
				return;
			}else{
				this.$api
					.get('api/user/sendPaymentSmsCode', {
						phone: this.profile.phone
					})
					.then(res => {
						uni.hideLoading();

						if(this.common.Response.isFaild(res.data)){
							uni.showToast({
								title: '获取验证码失败',
								icon: 'none'
							});
							return;
						}else if(this.common.Response.isException(res.data)){
							uni.showToast({
								title: res.data.msg,
								icon: 'none'
							});
							return;
						}
						
						//发送短信验证
						this.common.window.toNew("generics-sms/generics-sms", {
							formName: "payment",
							phone: this.profile.phone,
							success: "onSMSpaymentDone",
							reTry: "onReSendSmsCode"
						})
					});
				return;
			}
			
			
			
		},
		onReSendSmsCode(data){
			this.$api
				.get('api/user/sendPaymentSmsCode', {
					phone: this.profile.phone
				})
				.then(res => {
					
					if(this.common.Response.isFaild(res.data)){
						uni.showToast({
							title: '获取验证码失败',
							icon: 'none'
						});
						return;
					}else if(this.common.Response.isException(res.data)){
						uni.showToast({
							title: res.data.msg,
							icon: 'none'
						});
						return;
					}
					 
				});
		},
		onSMSpaymentDone(data){
			let arr = data.split(",");
			let _self = this;
			this.$api
				.post('api/user/changePaymentPassword', {
					smsCode: arr[1],
					password: this.password
				})
				.then(res => {
					
					if(_self.common.Response.isFaild(res.data)){
						uni.showToast({
							title: '获取验证码失败',
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
						icon: 'none',
						title: '密码修改成功'
					});
					
					
					setTimeout(()=>{
						uni.navigateBack({
							delta: 2
						})
					},2000)
				});
			
		},
		onPassSMSVerifyDone(code){
			uni.showToast({
				icon : "none",
				title : "绑定成功"
			});
			setTimeout(()=>{
				uni.navigateBack({
					delta: 2
				})
			},2000)
		}, 
			 
	}
};
</script>

<style lang="less" scoped>
@import '../../../static/common.less';
@import '../../../graceUI/animate.css';

page {
	background-color: #fff;
}

uni-button:after {
	border: 0px solid #fff !important;
}

.loading-tips {
	display: flex;
	position: relative;
	width: 100%;
	justify-content: center;
	top: 330upx;
	color: #949494;
}

.header {
	display: flex;
	flex-direction: row;
	width: 100%;
	font-family: @common-font-zh;
	font-size: 26px;
	justify-content: center;
}

.desc {
	display: flex;
	flex-direction: row;
	width: 100%;
	font-family: @common-font-zh !important;
	font-size: 16px;
	justify-content: center;
	color: #00a161;
}

.phone {
	margin-left: 10upx;
	font-size: 18px;
	font-family: @common-font-cn !important;
}

.sms-code-container {
	width: 100%;
}

.sms-code-internal-container {
	width: 80%;
	margin: 0 auto;
	padding-left: 40upx;
}

.sms-code-column {
	justify-content: center;
	display: inline-flex;
	flex-wrap: nowrap;
	align-items: center;
	width: 10%;
	height: 30upx;
	margin-right: 20px;
}

input {
	font-family: @common-font-cn !important;
	font-size: 25px !important;
	text-align: center;
	border-bottom: 4upx solid #757575;
	padding: 15upx;
}

.input-focus {
	border-bottom: 4upx solid #0f88ea !important;
}

.tips {
	display: flex;
	flex-direction: row;
	width: 100%;
	font-family: @common-font-zh !important;
	font-size: 15px;
	justify-content: center;
	color: #757575;
}
</style>
