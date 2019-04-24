<template>
	<!-- #ifdef APP-PLUS -->
	<view class="status_bar">
		<view class="top_view" v-bind:style="{backgroundColor: backgroundColor}"></view>
	</view>
	<!-- #endif -->
</template>

<script>
	export default {
		props: {
			backgroundColor: "#F8F8F8"
		},
		data() {
			return {
				
			};
		}
	}
</script>

<style>
	.status_bar {
		height: var(--status-bar-height);
		width: 100%;
		background-color: #F8F8F8;
	}
	
	.top_view {
		height: var(--status-bar-height);
		width: 100%;
		position: fixed;
		background-color: #F8F8F8;
		top: 0;
		z-index: 999;
	}
</style>
