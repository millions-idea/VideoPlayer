<template>
	<view>
		<view class="blank-line-20"></view>

		<view v-if="topLabel.length > 0">
			<text>{{ topLabel }}</text>
			<view class="blank-line-20"></view>
		</view>
		<view><input name="input" :placeholder="placeholder" :maxlength="maxLength" :minLength="minLength" :value="value" @input="onKeyInput" :type="type" focus /></view>
		<view class="bottom-label-container" v-if="bottomLabel.length > 0">
			<view class="blank-line-20"></view>
			<view class="bottom-label-border">
				<text class="bottom-label">{{ bottomLabel }}</text>
			</view>
		</view>
	</view>
</template>

<script>
export default {
	name: 'generics-form',
	data() {
		return {
			formName: '',
			topLabel: '',
			bottomLabel: '',
			value: '',
			placeholder: '',
			maxLength: 0,
			minLength: 0,
			title: '',
			success: '',
			type: '',
			changeValue: '',
			delta: 1
		};
	},
	computed: {},
	components: {},
	onLoad(option) {
		this.formName = option.formName == null ? '' : option.formName;
		this.topLabel = option.topLabel == null ? '' : option.topLabel;
		this.bottomLabel = option.bottomLabel == null ? '' : option.bottomLabel;
		this.value = option.value == null ? '' : option.value;
		this.placeholder = option.placeholder == null ? '' : option.placeholder;
		this.maxLength = option.maxLength == null ? 0 : option.maxLength;
		this.minLength = option.minLength == null ? 0 : option.minLength;
		this.title = option.title == null ? 'ERROR' : option.title;
		this.success = option.success == null ? '' : option.success;
		this.type = option.type == null ? 'text' : option.type;
		uni.setNavigationBarTitle({
			title: this.title
		});
		this.changeValue = this.value;
		this.delta = option.delta == null ? 1 : option.delta;
	},
	created() {
		this.appEvents.$on('close__' + this.formName, () => {
			uni.navigateBack({
				delta: this.delta
			});
		});
	},
	destroyed() {
		this.appEvents.$off('close__' + this.formName);
	},
	onNavigationBarButtonTap() {
		if (this.changeValue.length == 0) {
			uni.showToast({
				icon: 'none',
				title: '请输入内容'
			});
			return;
		}

		if (this.changeValue.length > this.maxLength) {
			uni.showToast({
				icon: 'none',
				title: '最多输入' + this.maxLength + '个字符'
			});
			return;
		}

		if (this.changeValue.length < this.minLength) {
			uni.showToast({
				icon: 'none',
				title: '应满足' + this.minLength + '个字符'
			});
			return;
		}

		this.appEvents.$emit(this.success, this.changeValue);
	},
	methods: {
		onKeyInput(event) {
			this.changeValue = event.target.value;
		}
	}
};
</script>

<style lang="less">
@import '../../static/common.less';

text {
	color: #999999;
	margin-left: 35upx;
	height: 40upx;
	line-height: 40upx;
	vertical-align: middle;
	font-size: 14px;
}

input {
	display: block;
	background-color: #fff;
	height: 75upx;
	line-height: 75upx;
	vertical-align: middle;
	padding-left: 35upx;
	font-size: 14px;
}

.bottom-label-container {
	padding: 20upx;
}

.bottom-label {
	font-size: 12px;
}

.bottom-label-border {
	border: 1px dashed #e0e0e0;
	padding: 20upx;
}
</style>
