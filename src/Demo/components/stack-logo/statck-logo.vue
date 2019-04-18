<template>
	<view>
		<image :style="{height: height}" src="../../../static/brand_logo.png" mode="aspectFit" class="brand-logo"></image>
	</view>
</template>

<script>
	export default{
		name: "brand-logo",
		props: {
			height: String
		},
		data(){
			return {
				
			}
		}
	}
</script>

<style>
	view{
		width: 100%;
		text-align: center;
	}
	
	
	.brand-logo{
		height: 180upx;
	}
</style>
