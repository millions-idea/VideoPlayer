<template>
	<view>
		<view class="container">
			<video
				v-if="data.videoUrl != null && data.videoUrl.length > 10 && !data.watchOne"
				id="myVideo"
				class="player"
				:poster="poster"
				:src="data.videoUrl"
				@play="onPlay"
				@error="videoErrorCallback"
				enable-danmu
				controls
			></video>
			<image class="poster" v-if="data.videoUrl == null || data.videoUrl.length == 0 || data.watchOne" :src="data.image"></image>
		</view>

		<view class="group">
			<view class="blank-line-20"></view>
			<view class="item-line title">
				<text>{{ title }}</text>
				<template v-if="!isCollect">
					<i class="iconfont-video-player icon-shoucang8 my-icon-collect" @tap="collect"></i>
				</template>
				<template v-else>
					<i class="iconfont-video-player icon-shoucang7 my-icon-collect" @tap="unCollect"></i>
				</template>
			</view>
			<view class="item-line info">
				<text class="label">类目：</text>
				<text>{{ data.categoryName }}</text>
			</view>
		</view>

		<view class="blank-line-20"></view>

		<view class="white-container">
			<view class="blank-line-20"></view>

			<view class="shopping-navigation" v-if="!data.buy || (data.buy && data.howToBuy == 1)">
				<view class="shopping-item" @tap="watchOne">
					<image src="../../static/user/play_bg_1.png"></image>
					<text class="item-title">观看一次</text>
					<text class="item-amount">￥{{ data.amount }}</text>
				</view>
				<view class="shopping-item shopping-right-item" @tap="watchPermanent">
					<image src="../../static/user/play_bg_2.png"></image>
					<text class="item-title">永久观看</text>
					<text class="item-amount item-amount-white">￥{{ data.commonAmount }}</text>
				</view>
			</view>

			<view class="download"><image src="../../static/user/play_bg_3.png"></image></view>
			<view class="blank-line-20"></view>
		</view>

		<view class="blank-line-20"></view>

		<view class="white-container">
			<view class="blank-line-20"></view>
			<view class="block-title-container">
				<text class="block-title">免费观看介绍</text>
				<text class="block-title-right">DESC</text>
			</view>
			<view class="blank-line-10"></view>
			<view class="desc">
				<text>{{ data.commonDesc }}</text>
			</view>
			<view class="blank-line-20"></view>
		</view>

		<view class="blank-line-20"></view>

		<view class="white-container">
			<view class="blank-line-20"></view>
			<view class="block-title-container">
				<text class="block-title">相关推荐</text>
				<text class="block-title-right">HOT</text>
			</view>
			<view class="blank-line-10"></view>
			<scroll-view scroll-x="true" class="slider">
				<template v-for="(item, i) in data.adjoinList">
					<view @tap="onAdjoin(item)" class="item" :key="i" :style="{ marginRight: i === data.adjoinList.length - 1 ? '15px' : '0px' }">
						<view class="item_content">
							<image class="img" :src="item.image"></image>
							<view class="video-info">
								<view class="item-tips-flex">
									<view class="video-title">
										<text class="video-title-text">{{ item.productName }}</text>
									</view>
									<view class="video-play-count">
										<i class="iconfont-video-player icon-eye video-play-count-icon"></i>
										<text class="video-play-count-text">{{ item.showCount }}</text>
									</view>
								</view>
							</view>
						</view>
					</view>
				</template>
			</scroll-view>
			<view class="blank-line-20"></view>
		</view>
	</view>
</template>

<script>
import { mapState, mapActions } from 'vuex';

export default {
	data() {
		return {
			videoUrl: '',
			videoId: '',
			title: '正在加载中',
			data: {},
			isCollect: false
		};
	},
	onLoad(option) {
		this.videoId = option.videoId;
		this.title = option.title;
		this.poster = option.poster;
	},
	onShow() {
		uni.setNavigationBarTitle({
			title: this.title
		});

		this.getData();
	},
	onReady(res) {
		this.videoContext = uni.createVideoContext('myVideo');
	},
	computed: {
		...mapState(['hasLogin', 'profile'])
	},
	methods: {
		...mapActions(['setProfile', 'authOpenWindow']),
		onPlay(e) {
			//验证是否购买以及购买类型
			if (!this.data.buy) {
				this.videoContext.pause();
				uni.showToast({
					title: '您还没有购买视频资源哦',
					icon: 'none'
				});
			}

			// 确认播放
			this.$api
				.get('api/product/player', {
					productId: this.videoId
				})
				.then(res => {
					console.log(res);
					if (this.common.Response.isFaild(res.data)) {
						uni.showToast({
							title: '播放失败',
							icon: 'none'
						});
						this.videoContext.pause();
						return;
					} else if (this.common.Response.isException(res.data)) {
						uni.showToast({
							title: '播放失败',
							icon: 'none'
						});
						this.videoContext.pause();
						return;
					}
				});
		},
		videoErrorCallback(e) {
			uni.showModal({
				content: '视频解析失败',
				showCancel: false
			});
		},
		getData() {
			uni.showLoading({
				title: '请稍后'
			});

			this.$api
				.get('api/product/getProduct', {
					productId: this.videoId
				})
				.then(res => {
					uni.hideLoading();
					if (this.common.Response.isFaild(res.data)) {
						uni.showToast({
							title: '查询失败',
							icon: 'none'
						});

						setTimeout(() => {
							uni.navigateBack({
								delta: 1
							});
						}, 2000);
					} else if (this.common.Response.isException(res.data)) {
						uni.showToast({
							title: res.data.msg,
							icon: 'none'
						});

						setTimeout(() => {
							uni.navigateBack({
								delta: 1
							});
						}, 2000);
					}

					this.data = res.data.msg;

					this.videoId = this.data.productId;
					this.title = this.data.productName;
					this.poster = this.data.image;

					this.isCollect = this.data.collect;

					this.data.amount = this.data.amount.toFixed(2);
					this.data.commonAmount = this.data.commonAmount.toFixed(2);

					this.data.adjoinList.forEach(item => {
						item.amount = item.amount.toFixed(2);
						item.showCount = item.showCount > 9999 ? (item.showCount / 10000).toFixed(1) + '万' : item.showCount;
					});
				});
		},
		collect() {
			this.isCollect = true;
			this.$api
				.get('api/product/collect', {
					productId: this.videoId
				})
				.then(res => {});
		},
		unCollect() {
			this.isCollect = true;
			this.$api
				.get('api/product/unCollect', {
					productId: this.videoId
				})
				.then(res => {});
		},
		watchOne() {
			//观看一次
			if (!this.hasLogin) {
				this.common.window.toNew('user/bootstrap/login', null);
				return;
			}

			this.common.window.toNew('user/payment/payment', {
				title: this.title + '-' + '观看一次',
				amount: this.data.amount,
				id: this.videoId
			});
		},
		watchPermanent() {
			//永久观看
			if (!this.hasLogin) {
				this.common.window.toNew('user/bootstrap/login', null);
				return;
			}
			this.common.window.toNew('user/payment/payment', {
				title: this.title + '-' + '永久观看',
				amount: this.data.commonAmount,
				id: this.videoId
			});
		},
		onAdjoin(item) {
			this.common.window.toNew('index/player', {
				videoId: item.productId,
				title: item.productName,
				poster: item.image
			});
		}
	}
};
</script>

<style lang="less" scoped>
@import '../../static/common.less';

.container {
	display: flex;
	min-height: 500upx;
	background-color: #000;
	align-items: center;
	justify-content: center;
	overflow: hidden;
}

.poster {
	margin: 0 auto;
	width: 100%;
	height: 150px;
	overflow: hidden;
}

.player {
	width: 100%;
	height: 500upx;
}

.group {
	background-color: #fff;
	min-height: 95px;
	width: 100%;
}

.group .item-line {
	width: 100%;
	display: flex;
	padding: 10upx;
	padding-left: 30upx;
	font-size: 13px;
}

.title {
	font-family: @common-font-zh-bold;
	font-size: 22px !important;
}

.shopping-navigation {
	display: flex;
	width: 100%;
	min-height: 100px;
	justify-content: space-between;
	padding: 20upx;
}

.shopping-item {
	width: 46%;
	height: 100px;
	overflow: hidden;
	border-radius: 5px;
}

.shopping-item image {
	width: 100%;
	height: 100px;
	overflow: hidden;
}

.shopping-item .item-title {
	position: relative;
	left: 12px;
	top: -85px;
	z-index: 10;
	color: #fff;
	font-size: 22px;
	font-weight: bold;
	font-family: @common-font-zh-bold;
}

.shopping-item .item-amount {
	position: relative;
	left: -69px;
	top: -55px;
	z-index: 10;
	color: #ffeb3b;
	font-size: 22px;
	font-weight: bold;
	font-family: 'Roboto';
}

.item-amount-white {
	color: #ffe1c4 !important;
}

.shopping-right-item {
	margin-right: 40upx;
}

.download {
	background-color: #ff000;
	display: flex;
	width: 95%;
	min-height: 100px;
	margin: 0 auto;
	justify-content: space-between;
}

.download image {
	width: 100%;
	height: 100px;
	overflow: hidden;
}

.desc {
	width: 95%;
	margin: 0 auto;
}

.desc text {
	width: 100%;
	display: flex;
	padding: 20upx;
}

.block-title {
	font-size: 18px !important;
}

.white-container {
	background-color: #fff;
}

.slider {
	white-space: nowrap;
	width: 100%;
	background-color: white;

	.item {
		display: inline-block;
		margin-left: 15px;
		margin-top: 13px;
		margin-bottom: 13px;
		width: 60%;
		height: 309upx;
		border-radius: 10px;
		box-shadow: 0px 1px 10px 0px rgba(0, 0, 0, 0.1);
		overflow: hidden;

		.item_content {
			display: flex;
			flex-direction: row;
		}
	}
}

.img {
	width: 100%;
	height: 125px;
	overflow: hidden;
}

.video-title {
	transition: all 0.1s ease-in 0s;
	width: 55%;
	filter: saturate(1);
	overflow: hidden;
	text-overflow: ellipsis;
	white-space: nowrap;
	text-align: left;
	height: 50upx;
	line-height: 50upx;
}

.video-title-text {
	height: 50upx;
	line-height: 50upx;
	font-size: 16px;
	color: #434748;
}

.video-play-count-icon {
	height: 50upx;
	line-height: 50upx;
	margin-right: 10upx;
	color: #ababab;
}

.video-play-count-text {
	color: #ababab;
	font-family: @common-font-num;
}

.video-info {
	width: 55%;
	background-color: rgba(255, 255, 255, 0);
	position: absolute;
	padding: 15upx;
	bottom: 18px;
}

.item-tips-flex {
	display: flex;
	justify-content: space-between; //左右布局
}

.item-tips {
	position: absolute;
	top: 0;
	left: 0;
	background-color: #e2b06c;
	width: 100upx;
	height: 50upx;
	line-height: 50upx;
	padding: 2px;
	color: #492515;
	font-family: @common-font-num;
	font-weight: bold;
	border-bottom-right-radius: 15upx;
	opacity: 0.8;
}

.item-tips .amount {
	display: inline-block;
	height: 50upx;
	line-height: 50upx;
	font-size: 22px;
}

.my-icon-collect {
	position: absolute;
	right: 10px;
	font-size: 22px;
}
</style>
