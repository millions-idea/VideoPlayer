<template>
	<view>
		<view v-if="loading">
			<!-- 加载动画页 -->
			<mht-loader :showImage="false" loadingType="jumping"></mht-loader>
		</view>
		<view v-else class="grace-waterfall">
			<view v-if="dataList.length > 0" class="list">
				<navigator @tap="onPlay(item)" class="items item" v-for="(item, index) in dataList" :key="index">
					<image class="img" :src="item.productImage"></image>
					<view class="item-tips">
						￥
						<text class="amount">{{ item.amount }}</text>
					</view>
					<view class="video-info">
						<view class="video-title">
							<text class="video-title-text">{{ item.productName }}</text>
						</view>
						<view class="video-play-count">
							<i class="iconfont-video-player icon-eye video-play-count-icon"></i>
							<text class="video-play-count-text">{{ item.showCount }}</text>
						</view>
					</view>
				</navigator>
			</view>
			<view v-if="dataList.length <= 0" class="list">
				<view class="empty-list"><stackEmpty class="items item empty" height="180" label="您还没有观看过视频哦"></stackEmpty></view>
			</view>
		</view>
		<graceLoading v-if="dataList.length > 0" :loadingType="loadingType"></graceLoading>
	</view>
</template>

<script>
var page = 0, _self;

import { mapState, mapActions } from 'vuex';
import graceLoading from '@/graceUI/components/graceLoading.vue';
import stackEmpty from '@/components/stack-empty/stack-empty.vue';
import mhtLoader from '@/components/mht-loader/mht-loader.vue';

export default {
	components: {
		graceLoading,
		stackEmpty,
		mhtLoader
	},
	data() {
		return {
			loading: false,
			loadingType: 0,
			isEnd: false,
			dataList: [],
			maxPage: 1000,
			empty: true
		};
	},
	onLoad(option) {
		_self = this;
		page = 0;
	},
	onShow() {
		this.getList();
	},
	onNavigationBarButtonTap() {
		uni.switchTab({
			url: './category'
		});
	},
	computed: {
		...mapState(['hasLogin', 'profile'])
	},
	onPullDownRefresh() {
		//监听下拉刷新动作
		console.log('onPullDownRefresh');
		// 这里获取数据
		this.getList();
	},
	//上拉加载更多
	onReachBottom: function() {
		this.getList();
	},
	onBackPress: function() {
		this.loadingType = 0;
		this.isEnd = false;
		page = 0;
	},
	methods: {
		...mapActions(['setProfile', 'authOpenWindow', 'login']),
		getList() {
			this.loading = true;
			//最后一页判断
			if (page > this.maxPage) {
				this.loading = false;
				this.isEnd = true;
				this.loadingType = 2;
				return;
			}
			this.loadingType = 1;
			//模拟api请求延迟关闭 Loading
			this.$api
				.get('api/user/getShopping', {
					page: page
				})
				.then(res => {
					this.loadingType = 3;
					this.loading = false;

					if (res.data.code == 200) {
						this.maxPage = res.data.count;

						if (res.data.data == null || res.data.data.length == 0) {
							this.loadingType = 3;
						}

						res.data.data.forEach(item => {
							item.showCount = item.showCount > 9999 ? (item.showCount / 10000).toFixed(1) + '万' : item.showCount;
						});

						this.dataList = this.dataList.concat(res.data.data);
						this.loadingType = 0;
						page++;
					}
				});
		},
		onPlay(item) {
			if (this.hasLogin) {
				this.common.window.toNew('index/player', {
					videoId: item.productId,
					title: item.productName,
					poster: item.productImage
				});
			} else {
				this.common.window.toNew('user/bootstrap/login', null);
			}
		}
	}
};
</script>

<style lang="less" scoped>
@import '../../static/common.less';

page {
	background-color: #fff !important;
}

.grace-waterfall {
	flex-flow: column !important;
}

.grace-waterfall .items {
	margin-bottom: 25px !important;
}

.grace-waterfall .list {
	width: 95%;
	margin: 0 auto !important;
}

.list {
	width: 100%;
}

.item {
	clear: both;
	width: 100%;
	overflow: hidden;
	height: 359upx;
	justify-content: center;
	justify-items: center;
	text-align: center;
	align-items: center;
	border-radius: 10upx;
	background-color: #fff;
	/* box-shadow: 0px 1px 20px 0px rgba(150, 32, 214, 0.1); */
	box-shadow: 0px 1px 10px 0px rgba(0, 0, 0, 0.1);
}

.item .img {
	width: 100%;
	height: 273upx;
	overflow: hidden;
}

.item .video-title {
	transition: all 0.1s ease-in 0s;
	width: 550upx;
	filter: saturate(1);
	overflow: hidden;
	text-overflow: ellipsis;
	white-space: nowrap;
	text-align: left;
	height: 50upx;
	line-height: 50upx;
}

.item .video-title-text {
	font-size: 16px;
	color: #434748;
}

.item .video-play-count-icon {
	margin-right: 10upx;
	color: #ababab;
}

.item .video-play-count-text {
	color: #ababab;
	font-family: @common-font-num;
}

.video-info {
	display: flex;
	justify-content: space-between; //左右布局
	padding: 15upx;
}

.item .item-tips {
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

.item .item-tips .amount {
	font-size: 22px;
}

.empty-list,
.empty-list .item {
	height: 600upx !important;
	box-shadow: 0px 0px 0px 0px rgba(0, 0, 0, 0);
}
</style>
