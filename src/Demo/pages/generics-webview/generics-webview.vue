<template>
	<view><web-view :webview-styles="webviewStyles" :src="src"></web-view></view>
</template>

<script>
export default {
	onLoad(option) {
		this.src = option.url == null ? '' : option.url;
		uni.setNavigationBarTitle({
			title: option.title == null ? '请稍后' : option.title
		});
	},
	data() {
		return {
			src: '',
			webviewStyles: {
				progress: {
					color: '#0F8DE8'
				}
			}
		};
	}
};
</script>

<style></style>
