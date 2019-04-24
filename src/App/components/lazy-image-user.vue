<template>
	<view class="lazy-image">
		<image class="real-image" @load="onLoaded" :src="realSrc" mode="widthFix"></image>
		<view :class="loaded?'loaded':''">
			<image :src="placeholdSrc" mode="widthFix"></image>
		</view>
	</view>
</template>

<script>
	export default {
		props:{
			placeholdSrc:{
				type:String,
				default:""
			},
			realSrc:{
				type:String,
				default:""
			}
		},
		data(){
			return {
				loaded:false
			}
		},
		methods:{
			onLoaded(){
				this.loaded = true
			}
		}
	}
</script>

<style lang="scss" scoped>
	.lazy-image{
		height: 100%;
		width: 100%;
		flex: 1;
		display: flex;
		flex-direction: column;
		align-items: center;
		position: relative;
		overflow: hidden;
	}
	image{
		width: 100%;
		border-radius: 60upx !important;
	}
	view{
		background-color: #eee;
		position: absolute;
		z-index: 2;
		top: 0;
		left: 0;
		height: 100%;
		width: 100%;
		flex: 1;
		display: flex;
		flex-direction: column;
		align-items: center;
		transition: opacity 0.4s linear;
		overflow: hidden;
		border-radius: 60upx !important;
	}
	.loaded{
		opacity: 0;
		overflow: hidden;
	}
</style>
