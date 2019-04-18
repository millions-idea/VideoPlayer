<template>
	<view>
		<view class="content">
			<view class="blank-line-20"></view>

			<!-- #ifdef APP-PLUS -->
			<!-- status bar placeholder -->
			<view class="top-status-bar-placeholder"></view>
			<!-- status bar placeholder end -->
			<!-- #endif -->

			<view class="header">
				<jingSwiper @click="onSwiperClick" :imgList="imgList" :interval="5000" :duration="1000"></jingSwiper>
				<view class="blank-line-10"></view>
			</view>

			<view>
				<view class="blank-line-20"></view>
				<view class="block-title-container">
					<text class="block-title">最近更新</text>
					<text class="block-title-right">NEW</text>
				</view>
				<view class="blank-line-10"></view>
			</view>
		</view>

		<view class="grace-waterfall">
			<view v-if="dataList.length > 0" class="list">
				<navigator @tap="onPlay(item)" class="items item" v-for="(item, index) in dataList" :key="index">
					<view class="fullImg"><image class="img" :src="item.image"></image></view>
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
				<view class="empty-list"><stackEmpty class="items item empty" height="180" label="最近没有更新喔"></stackEmpty></view>
			</view>
		</view>
		<graceLoading v-if="dataList.length > 0" :loadingType="loadingType"></graceLoading>
	</view>
</template>

<script>
var page = 0,
	_self;

import { mapState, mapActions } from 'vuex';
import jingSwiper from '@/components/jing-swiper/jing-swiper.vue';
import graceLoading from '@/graceUI/components/graceLoading.vue';
import stackEmpty from '@/components/stack-empty/stack-empty.vue';

export default {
	components: {
		jingSwiper,
		graceLoading,
		stackEmpty
	},
	data() {
		return {
			loading: false,
			loadingType: 0,
			isEnd: false,
			dataList: [],
			cid: 0,
			maxPage: 1000,
			empty: true,

			imgList: [
				{
					img: 'https://stack-1251694474.cos.ap-guangzhou.myqcloud.com/aishi/banner1.png',
					desc: ''
				},
				{
					img: 'https://stack-1251694474.cos.ap-guangzhou.myqcloud.com/aishi/banner2.png',
					desc: ''
				},
				{
					img: 'https://stack-1251694474.cos.ap-guangzhou.myqcloud.com/aishi/banner3.png',
					desc: ''
				}
			]
		};
	},
	onLoad() {
		_self = this;
		page = 0;
	},
	onShow() {
		this.getList();
	},
	computed: {
		...mapState(['hasLogin', 'profile'])
	},
	onPullDownRefresh() {
		//监听下拉刷新动作
		console.log('onPullDownRefresh');
		// 这里获取数据
		this.getBanner();
		this.getProfile();
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
		...mapActions(['setProfile', 'authOpenWindow', 'login', 'sysLogout']),

		getBanner() {
			let bannerList = this.session.getValue('homeBanner');
			if (bannerList != null && JSON.stringify(bannerList).length > 5) {
				this.imgList = bannerList;
			}
			this.$api.get('api/bootstrap/getBannerList', {}).then(res => {
				if (!this.common.Response.isFaild(res.data) && !this.common.Response.isException(res.data)) {
					this.imgList = res.data.data;
					this.session.setValue('homeBanner', this.imgList);
				}
			});
		},

		getList() {
			//最后一页判断
			if (page > this.maxPage) {
				this.isEnd = true;
				this.loadingType = 2;
				uni.stopPullDownRefresh();
				return;
			}
			this.loadingType = 1;
			//模拟api请求延迟关闭 Loading
			let _self = this;
			this.$api
				.get('api/home/getNewProductLimit', {
					page: page
				})
				.then(res => {
					uni.stopPullDownRefresh();

					_self.loadingType = 3;

					if (res.data.code == 200) {
						_self.maxPage = res.data.count;

						if (res.data.data == null || res.data.data.length == 0) {
							_self.loadingType = 3;
						}

						res.data.data.forEach(item => {
							item.amount = item.amount.toFixed(2);
							item.showCount = item.showCount > 9999 ? (item.showCount / 10000).toFixed(1) + '万' : item.showCount;
						});

						_self.dataList = _self.dataList.concat(res.data.data);
						_self.loadingType = 0;
						page++;
					}
				});
		},
		getProfile() {
			this.session.clearSession();
			this.session.clearState();
			let session = this.session.getSession();
			if (session == null) {
				console.log('加载用户信息');
				let _self = this;
				this.$api
					.post('api/user/getProfile', {})
					.then(res => {
						if (_self.common.Response.isFaild(res.data)) {
							return;
						} else if (_self.common.Response.isException(res.data)) {
							if (res.data.msg == '请先登录') return;
							uni.showToast({
								title: res.data.msg,
								icon: 'none'
							});
							return;
						}

						_self.session.setSession(res.data.msg);
						_self.login();
						_self.setProfile(res.data.msg);
						return;
					})
					.catch(err => {
						return;
					});
			} else {
				console.log('更新用户信息');
				_self.setProfile(session);
			}
		},
		onSwiperClick(item) {
			//判断是否内部链接还是外部链接
			if (item.desc.indexOf('http') != -1) {
				this.common.window.toNew('generics-webview/generics-webview', {
					url: item.desc
				});
			} else {
				if (this.hasLogin) {
					this.common.window.toNew('index/player', {
						videoId: item.desc
					});
				} else {
					this.common.window.toNew('user/bootstrap/login', null);
				}
			}
		},
		onPlay(item) {
			if (this.hasLogin) {
				this.common.window.toNew('index/player', {
					videoId: item.productId,
					title: item.productName,
					poster: item.image
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
.content {
	border-bottom-left-radius: 10px;
	border-bottom-right-radius: 10px;
}

.uni-grid-item,
.uni-grid-item:after {
	z-index: 0 !important;
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
}

.item .fullImg {
	background-size: 100% !important;
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

/* preview */

@import '../../graceUI/animate.css';

.pop-container {
	height: 400px;
	border-radius: 5px;
	background-color: #fff;
	overflow: hidden;
}

.pop-container .title {
	width: 100%;
	height: 80upx;
	line-height: 80upx;
	background-color: #fafafa;
	color: #000;
	text-align: center;
	font-family: @common-font-zh;
}

.pop-container .nickname {
	width: 100%;
	height: 50upx;
	line-height: 50upx;
	color: #000;
	text-align: center;
	font-family: @common-font-zh;
}

.pop-container .phone {
	width: 100%;
	height: 40upx;
	line-height: 40upx;
	color: #000;
	text-align: center;
	font-family: @common-font-zh;
}

.pop-container .info {
	margin-top: 30upx;
	height: 100upx;
	line-height: 100upx;
	display: flex;
	justify-content: center;
	text-align: center;
	align-items: center;
	width: 100%;
}

.pop-container .card {
	max-height: 200px;
	width: 100%;
	display: flex;
	justify-content: center;
	text-align: center;
	margin-top: 25upx;
	border-radius: 15upx;
}

.pop-container .card-img {
	width: 200px;
	height: 200px;
	border-radius: 15upx;
}

.top-user-preview {
	width: 100%;
	height: 300upx;
	background-color: #fb7039;
	background-image: url('../../static/user/user-preview-bg.png') !important;
	background-size: 100% 300upx;
	position: relative;
	top: 0px;
}

/* #ifdef APP-PLUS */
.top-user-preview {
	height: 300upx;
}
/* #endif */

.top-status-bar-placeholder {
	min-height: var(--status-bar-height);
	background-color: rgba(255, 255, 255, 0);
}

.sample-header {
	width: 90%;
	min-height: 80upx;
	margin: 0 auto;
}

.sample-header .left,
.sample-header .right {
	overflow: hidden;
}

.sample-header .left {
	float: left;
}

.sample-header .right {
	float: right;
}

.default-avatar {
	float: left;
	width: 80upx;
	height: 80upx;
	position: absolute;
	left: 0px;
	top: 0px;
	overflow: hidden !important;
	border-radius: 60upx !important;
}

.avatar-container {
	position: relative;
	display: inline-flex;
	float: left;
	width: 80upx;
	height: 80upx;
	overflow: hidden !important;
	border-radius: 60upx !important;
}

.account {
	float: left;
	display: inline-flex;
	margin-left: 25upx;
	line-height: 80upx;
	height: 80upx;
	font-size: 30upx;
	color: @dark-text-color;
	font-family: @normal-number-font;
}

.header-icon {
	display: inline-flex;
	float: right;
	font-size: 40upx;
	line-height: 80upx;
	color: @dark-text-color;
	margin-left: 20upx;
}

.header-icon-vip {
	font-size: 50upx !important;
}

.header-icon-settings {
	font-size: 50upx !important;
}

.parent-user {
	line-height: 80upx;
	color: #ffe8e0;
	font-size: 13px;
	text-decoration: underline;
}

/* finance */
.finance-preview {
	width: 100%;
}

.finance-preview .item-line {
	width: 90%;
	height: 120upx;
	margin: 0 auto;
	white-space: nowrap;
}

.item-line .title {
	height: 24upx;
	line-height: 24upx;
}

.finance-preview .item-label {
	font-family: @normal-text-font;
	color: rgba(255, 255, 255, 0.6);
	font-size: 24upx;
}

.lock-eye {
	display: inline-flex;
	margin-left: 30upx;
	color: @dark-text-color;
	font-size: 40upx !important;
	position: relative;
	top: 5upx;
}

.balance {
	color: rgb(255, 255, 255);
	font-family: @common-font-num;
	font-size: 70upx;
}

.item-inline {
	width: 90%;
	margin: 0 auto;
}

.inline-item .item-label {
	width: 100%;
	height: 36upx;
}

.inline-item .item-label .title {
	display: flex;
	flex-wrap: wrap;
	width: 100%;
	height: 36upx;
}

.inline-item .balance {
	display: flex;
	flex-wrap: wrap;
	width: 100%;
	font-size: 40upx;
	line-height: 84upx;
}

.inline-item {
	clear: both;
	display: inline-block;
	width: 28.4%;
	min-height: 120upx;
}

/* finance end */

/* navigation */

.container {
	min-height: 150upx;
	max-height: 150upx;
	height: 150upx;
	background-color: #fff;
}
</style>
