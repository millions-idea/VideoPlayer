<template>
	<view class="emptyView">
		<view class="emptyImg">
			<image class="icon" :style="{height: height + 'px'}" src="/static/data/empty.png" mode="aspectFit"></image>
			<text class="label">{{label}}</text>
		</view>
	</view>
</template>

<script>
	export default{
		name: "stackEmpty",
		props: {
			height: String,
			label: String
		},
		data(){
			return {
				
			}
		}
	}
</script>

<style lang="less" scoped>
	@import "../../static/common.less";
	
	.emptyView{
		width: 100%;
		text-align: center;
		z-index: 9999 !important;
	}
	
	.emptyImg{
		width: 100%;
		position: absolute;
		top: 20%;
	}
	
	
	.icon{
		height: 380upx;
	}
	
	.label{
		display: flex;
		width: 100%;
		font-family: @common-font-zh;
		// color: #d3d3d3;
		color: #b9b9b9;
		justify-content: center;
		font-size: 35upx;
	}
	
	/* page{
		background-color: #fff;
	} */
</style>
