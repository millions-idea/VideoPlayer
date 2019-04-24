<template>
	<view>
		<template v-if="shadeLoading">
			<!-- 加载动画页 -->
			<text class="loading-tips grace-animate pulse">亲，请不要离开哦……</text>
			<mht-loader :showImage="false" loadingType="jumping"></mht-loader>
		</template>
		<template v-else>
			<view class="blank-line-50"></view>
			<view class="blank-line-20"></view>
			<text class="header">输入验证码</text>
			<view class="blank-line-10"></view>
			<view class="blank-line-20"></view>
			<text class="desc">
				短信已发送至
				<text class="phone">+86{{ this.phone }}</text>
			</text>
			<view class="blank-line-50"></view>
			<view class="sms-code-container">
				<view class="sms-code-internal-container">
					<view class="sms-code-column">
						<template v-if="isActive1">
							<input
								:id="1" 
								class="sms-code-char input-focus"
								maxlength="1"
								type="number"
								:value="codes[0]"
								@tap="showKeyboard"
							/>
						</template>
						<template v-else>
							<input
								:id="1"
								:value="codes[0]"
								@tap="showKeyboard"
								class="sms-code-char"
								maxlength="1"
								type="number" 

							/>
						</template>
					</view>
					<view class="sms-code-column">
						<template v-if="isActive2">
							<input
								:id="2"
								:value="codes[1]"
								@tap="showKeyboard"
								class="sms-code-char input-focus"
								maxlength="1"
								type="number" 
							/>
						</template>
						<template v-else>
							<input
								:id="2"
								:value="codes[1]"
								@tap="showKeyboard"
								class="sms-code-char"
								maxlength="1"
								type="number" 

							/>
						</template>
					</view>
					<view class="sms-code-column">
						<template v-if="isActive3">
							<input
								:id="3"
								:value="codes[2]"
								@tap="showKeyboard"
								class="sms-code-char input-focus"
								maxlength="1"
								type="number" 
							/>
						</template>
						<template v-else>
							<input
								:id="3"
								:value="codes[2]"
								@tap="showKeyboard"
								class="sms-code-char"
								maxlength="1"
								type="number" 

							/>
						</template>
					</view>
					<view class="sms-code-column">
						<template v-if="isActive4">
							<input
								:id="4"
								:value="codes[3]"
								@tap="showKeyboard"
								class="sms-code-char input-focus"
								maxlength="1"
								type="number" 
							/>
						</template>
						<template v-else>
							<input
								:id="4"
								:value="codes[3]"
								@tap="showKeyboard"
								class="sms-code-char"
								maxlength="1"
								type="number" 

							/>
						</template>
					</view>
					<view class="sms-code-column">
						<template v-if="isActive5">
							<input
								:id="5"
								:value="codes[4]"
								@tap="showKeyboard"
								class="sms-code-char input-focus"
								maxlength="1"
								type="number" 
							/>
						</template>
						<template v-else>
							<input
								:id="5"
								:value="codes[4]"
								@tap="showKeyboard"
								class="sms-code-char"
								maxlength="1"
								type="number" 

							/>
						</template>
					</view>
					<view class="sms-code-column">
						<template v-if="isActive6">
							<input
								:id="6"
								:value="codes[5]"
								@tap="showKeyboard"
								class="sms-code-char input-focus"
								maxlength="1"
								type="number" 
							/>
						</template>
						<template v-else>
							<input
								:id="6"
								:value="codes[5]"
								@tap="showKeyboard"
								class="sms-code-char"
								maxlength="1"
								type="number" 
							/>
						</template>
					</view>
				</view>
			</view>
			<view v-if="countTimerShow" class="blank-line-50"></view>
			<view class="blank-line-20"></view>
			<text v-if="countTimerShow" class="tips">
				<text class="tips-time">{{ countDown }}s</text>
				后可重新获取
			</text>
			<view class="blank-line-50"></view>
			<button
				v-if="reGetButtonShow"
				class="reGetButton"
				hover-class="reGetButtonHover"
				:disabled="reGetButtonDisabled"
				@click="reGetSms"
			>
				重新获取验证码
			</button>
		</template>
		
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
import '../../graceUI/animate.css';
import graceSafeyNumberKeyboard from '@/graceUI/components/graceSafeyNumberKeyboard.vue';

export default {
	name: 'generics-sms',
	data() {
		return {
			formName: '',
			success: '',
			reTry: "",
			changeValue: '',
			phone: '',
			delta: 1,
			ext: {},

			graceNumberKeyboardShow: true,
			numberVal: '',
			activeIndex: 0,
			codes: ['', '', '', '', '', ''],

			shadeLoading: false,

			countTimerShow: true,
			countDown: 60,
			reGetButtonShow: false,
			reGetButtonDisabled: false,
			
		};
	},
	computed: {
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
		}
	},
	components: {
		mhtLoader,
		graceSafeyNumberKeyboard
	},
	onShow() {
		this.activeIndex = 0;
		this.graceNumberKeyboardShow = true;
		uni.hideKeyboard();
		
		this.appEvents.$on('close__sms__' + this.formName, () => {
			uni.navigateBack({
				delta: this.delta
			});
		});
	},
	onLoad(option) {
		uni.hideKeyboard();

		this.formName = option.formName == null ? '' : option.formName;
		this.phone = option.phone == null ? '' : option.phone;
		this.success = option.success == null ? '' : option.success;
		this.reTry = option.reTry == null ? '' : option.reTry;
		this.changeValue = this.value;
		this.delta = option.delta == null ? 1 : option.delta;
		this.ext = option.ext == null ? 1 : JSON.parse(option.ext);
		//倒计时
		setInterval(() => {
			if (this.countDown > 1) {
				this.countDown -= 1;
			} else {
				this.countTimerShow = false;
				this.reGetButtonShow = true;
				this.reGetButtonLoading = false;
				this.graceNumberKeyboardShow = false;
			}
		}, 1000);
	},
	onHide(){
		this.appEvents.$off('close__sms__' + this.formName);
	},
	destroyed() {
		this.appEvents.$off('close__sms__' + this.formName);
	},
	methods: {
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
			/* let numberArr = this.numberVal.split(',');
			if (numberArr.length <= 0) {
				return;
			}
			this.activeIndex = numberArr.length - 1;
			this.numberVal = this.numberVal.substring(0, this.numberVal.length - 2);
			 */
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
			this.codes.forEach((c) => code = code + c);
			if(code.length != 6) return;
			this.graceNumberKeyboardShow = false;
			this.codes = [];
			this.activeIndex = 0;
			this.numberVal = "";
			if(this.ext != null && this.ext instanceof Object){
				this.appEvents.$emit(this.success, this.phone + "," + code + "," + this.ext.password);
			}else{
				this.appEvents.$emit(this.success, this.phone + "," + code);
			}
			
		},
		reGetSms() {
			let _self = this;
			this.reGetButtonDownStyle = 'bounceIn';
			this.reGetButtonDisabled = true;
			this.appEvents.$emit(this.reTry, this.phone);
			setTimeout(() => {
				_self.reGetButtonDisabled = false;
				_self.reGetButtonShow = false;
				_self.countTimerShow = true;
				_self.countDown = 60;
				_self.graceNumberKeyboardShow = true;
			}, 1000);
		}
	}
};
</script>

<style lang="less">
@import '../../static/common.less';

page {
	background-color: #fff;
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
	font-size: 24px;
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

.tips-time {
	margin-right: 10upx !important;
	font-size: 15px !important;
	font-family: @common-font-cn !important;
}

.reGetButton {
	background-color: #0083fe;
	color: #fff;
	border-radius: 0px;
	width: 80%;
	border: 0px solid #fff !important;
}

.reGetButtonHover {
	opacity: 0.6;
	border: 0px solid #fff !important;
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

.mht-loader {
	height: 65vh;
}
</style>
