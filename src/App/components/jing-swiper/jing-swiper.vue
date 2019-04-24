<template>
	<view class="my-plan-style">
		<!-- 3D轮播 -->
		<view class="my-swiper">
			<swiper class="imageContainer" @change="handleChange" :interval="interval" :duration="duration" :previous-margin="margin" :next-margin="margin" circular  autoplay>
				<block v-for="(item,index) in imgList" :key="index">
					<swiper-item class="swiperitem"  @tap="onClick(index, item)">
						<!-- <lazy-image class="lazy-image itemImg" :class="currentIndex == index ? 'swiperactive': ''" mode="scaleToFill"
								:realSrc="item.img" placeholdSrc="/static/data/banner_loading.png"></lazy-image> -->
						<image class="itemImg" :class="currentIndex == index ? 'swiperactive': ''" mode="scaleToFill"
								:src="item.img"></image>
						<!-- <template v-if="currentIndex == index">
							<view class="desc"><text class="font">{{item.desc}}</text></view>
						</template> -->
					</swiper-item>
				</block>
			</swiper>
		</view>
	</view>
</template>
<script>

	export default {
		name: "jingSwiper",
		props:{
			imgList: Array,
			duration: 0,
			interval: 0
		},
		components:{
		},
		data(){
			return { 
				currentIndex: 0
			}
		},
		created() {
			this.imgList.forEach(img => {
				console.log(img);
			})
		},
		computed:{
			margin(){
				return uni.upx2px(50) + "px";
			}
		},
		methods:{
			handleChange(event){
				this.currentIndex = event.detail.current;
			},
			onClick(index, item){
				this.$emit('click', {
					index: index,
					item: item
				})
			}
		}
	}	
</script>
<style lang="scss" scoped>
	// 3D轮播样式 
	.imageContainer {
		width: 100%;
		/* height: 500rpx; */
		/* background: #000; */
		height: 353upx !important;
		background-color: rgba(255, 255, 255, 0) !important;

	}
	
	.swiperitem {
		/* height: 500rpx; */
		height: 280upx;//255
		padding: 0upx 20upx;
		box-sizing: border-box;
		position: relative;
		overflow: hidden;
		background-color: rgba(255, 255, 255, 0) !important;

		.swiperText {
			display: flex;
			flex-direction: column;
			align-items: center;
			position: absolute;
			top: 56upx;
			left: 51upx;
			z-index: 998;
			width:162upx;
			height:163upx;
			background:rgba(255,255,255,1);
			border-radius:8upx;
			padding:10upx;
			.name {
				font-size:26upx;
				font-weight:500;
				color:rgba(253,57,91,1);
				line-height:37upx;
				margin-bottom: 10upx;
			}
			.zq,.cz {
				font-size:20upx;
				color:rgba(253,57,91,1);
				line-height:35upx;
			}
			.addNl {
				width:120upx;
				height:26upx;
				background:rgba(253,57,91,1);
				border-radius:13upx;
				font-size:20upx;
				font-weight:500;
				color:rgba(255,255,255,1);
				text-align: center;
				line-height: 26upx;
				margin-top: 10upx;
			}
			
		}
	}
	
	.itemImg {
		position: relative;
		width: 100%;
		/* height: 380rpx; */
		height: 255upx;
		border-radius: 15upx;
		z-index: 5;
		opacity: .9;
		top: 10%;
		background-color: #FAFAFA !important;
		box-shadow: 0px 1px 10px 0px rgba(0, 0, 0, 0.1);
	}
	
	.swiperactive {
		width: 102%;
		opacity: 1;
		z-index: 10;
		margin-left: -10upx;
		/* height: 430rpx; */
		height: 320upx;
		top: 5%;
		transition: all .2s ease-in 0s;
	}
	
	.zhankai{
		text-align: center;
		.iconfont{
			margin-left: 10upx;
		}
	}
	
	.desc{
		transition: all .1s ease-in 0s;

		position: relative;
		bottom: 16px;
		z-index: 9999;
		display: inline-flex;
		width: 100% !important;
		background-color: rgba(0, 0, 0, 0.27843137254901963);
		color: #000;
		border-bottom-right-radius: 15upx;
		border-bottom-left-radius: 15upx;
		height: 40upx;
		line-height: 40upx;
		
		.font{
			color:#fff;
			padding-left: 20upx;
			font-size: 12px;
		}
	}
	
	/* #ifdef APP-PLUS */
	.desc{
		bottom: 36upx;
	}
	/* #endif */
	
	/* #ifdef H5/MP */
	.desc{
		bottom: 32upx;
	}
	/* #endif */
</style>