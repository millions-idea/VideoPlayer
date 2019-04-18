<template>
	<view>
		<!-- #ifdef APP-PLUS -->
		<view class="blank-line-50"></view>

		<view class="status_bar">
			<view class="top_view"></view>
			<uni-nav-bar left-icon="back" color="#808080" @click-left="backUp()"></uni-nav-bar>
		</view>
		<!-- #endif -->

		<!-- #ifdef H5/MP -->
		<uni-nav-bar left-icon="back" color="#808080" @click-left="backUp()"></uni-nav-bar>
		<!-- #endif -->

		<template v-if="true">
			<scroll-view class="grace-tab-title grace-center" scroll-x="true" id="grace-tab-title">
				<view v-for="(tab, index) in tabs" :key="index" :class="[tabCurrentIndex == index ? 'grace-tab-current' : '']" :id="'tabTag-' + index" @tap="tabChange">
					{{ tab.name }}
				</view>
			</scroll-view>

			<swiper class="grace-tab-swiper-full" :current="swiperCurrentIndex" @change="swiperChange" :style="{ height: tabHeight + 'px' }">
				<!-- 循环新闻项目 -->
				<swiper-item v-for="(list, listIndex) in dataList" :key="listIndex">
					<template v-if="tabCurrentIndex == 0 && dataList[0].length == 0">
						<stackEmpty height="180" label="没有订单"></stackEmpty>
					</template>
					<template v-else-if="tabCurrentIndex == 1 && dataList[1].length == 0">
						<stackEmpty height="180" label="没有待付款的订单"></stackEmpty>
					</template>
					<template v-else-if="tabCurrentIndex == 2 && dataList[2].length == 0">
						<stackEmpty height="180" label="没有已完成的订单"></stackEmpty>
					</template>
					<template v-else>
						<scroll-view scroll-y="true" :data-scindex="listIndex" @scrolltolower="scrollend">
							<view class="grace-list-list" style="padding:25upx; width:700upx;">
								<navigator url="/" v-for="(item, index) in list" :key="index" @tap="copyCdkey(item.cdkey)">
									<view class="grace-list-list-items">
										<view class="order">
											<view class="order-header">
												<view class="left order-info-left">
													<view class="order-info">
														<text class="label">订单编号：</text>
														<text class="order-id">{{ item.orderId }}</text>
													</view>
												</view>
												<view class="right">
													<text class="order-status">{{ item.statusText }}</text>
												</view>
											</view>
											<view class="order-detail">
												<view class="left ">
													<view class="left">
														<image
															:lazy-load="true"
															:class="['product-image', graceSkeleton == 'ing' ? 'grace-skeleton' : '']"
															:src="item.productImage"
														></image>
														<view class="profile-text-detail">
															<text class="product-title">{{ item.shortProductName }}</text>
															<text class="product-date">订单时间：{{ item.addDate }}</text>
															<text v-if="item.cdkey != null" class="product-card">
																激活码：
																<!-- #ifdef APP-PLUS -->
																<text class="product-card-code">{{ item.cdkey }}</text>
																<!-- #endif -->
																<!-- #ifdef H5 -->
																<text class="product-card-code" selectable>{{ item.cdkey }}</text>
																<!-- #endif -->
																{{ '  (' + item.channelText + ')' }}
															</text>
														</view>
													</view>
													<view class="right ">
														<text class=" order-price order-status">￥ {{ item.amount }}</text>
													</view>
												</view>
											</view>
										</view>
									</view>
								</navigator>
							</view>
							<graceLoading :loadingType="tabs[listIndex].loadingType"></graceLoading>
						</scroll-view>
					</template>
				</swiper-item>
			</swiper>
		</template>

		<!-- detail -->
		<template v-if="isShowDetail">
			<view class="detail-view">
				<view class="trade-type bottom-gray-line">
					<template v-if="type == 0">
						<text class="item-left item-label">入账金额</text>
						<text class="right amount add">{{ amount }}</text>
					</template>
					<template v-else>
						<text class="item-left item-label">出账金额</text>
						<text class="right amount reduce">{{ amount }}</text>
					</template>
				</view>
				<view class="detail bottom-gray-line">
					<view class="item">
						<text class="item-left item-label">类型</text>
						<template v-if="type == 0">
							<text class="right type">收入</text>
						</template>
						<template v-else>
							<text class="right type">支出</text>
						</template>
					</view>
					<view class="item">
						<text class="item-left item-label">时间</text>
						<text class="right type date">{{ date }}</text>
					</view>
					<view class="item">
						<text class="item-left item-label">交易单号</text>
						<text class="right type trade-id" selectable>{{ id }}</text>
					</view>
					<view class="item">
						<text class="item-left item-label">剩余零钱</text>
						<text class="right type">{{ balance }}</text>
					</view>
					<view class="item"><text class="item-left item-label">备注</text></view>
					<view class="item">
						<text class="remarks type">{{ desc }}</text>
					</view>
				</view>
			</view>
		</template>
	</view>
</template>
<script>
var _self;
//对应下面3个标签的新闻内容数据
import graceLoading from '@/graceUI/components/graceLoading.vue';
import stackEmpty from '@/components/stack-empty/stack-empty.vue';
import uniNavBar from '@/components/uni-nav-bar/uni-nav-bar.vue';

export default {
	data() {
		return {
			tabCurrentIndex: 0,
			swiperCurrentIndex: 0,
			tabHeight: 300,
			tabs: [
				//标签名称 , 分类 id , 加载更多, 加载的页码
				{ name: '全部', id: 'all', loadingType: 0, page: 1 },
				{ name: '待付款', id: 'wait', loadingType: 0, page: 1 },
				{ name: '已完成', id: 'finish', loadingType: 0, page: 1 }
			],
			dataList: [[], [], []],
			maxPage: 0,

			title: '',
			date: '',
			type: 0,
			amount: '',
			id: '',
			balance: '',
			isShowDetail: false,

			scrollTop: 0,
			graceSkeleton: 'ing'
		};
	},
	onLoad: function() {
		_self = this;
		this.getList(0, null);
		setTimeout(() => {
			_self.graceSkeleton = 'end';
		}, 5000);
	},
	onReady: function() {
		//获取屏幕高度及比例
		var winInfo = uni.getSystemInfo({
			success: function(res) {
				var windowHeight = res.windowHeight;
				//获取头部标题高度
				var dom = uni.createSelectorQuery().select('#grace-tab-title');
				dom.fields({ size: true }, res2 => {
					if (!res2) {
						return;
					}
					//计算得出滚动区域的高度
					_self.tabHeight = windowHeight - res2.height;
				}).exec();

				dom = uni.createSelectorQuery().select('.uni-navbar');
				dom.fields({ size: true }, res2 => {
					if (!res2) {
						return;
					}
					_self.tabHeight = _self.tabHeight - (res2.height + 20);
				}).exec();
			}
		});
	},
	watch: {
		isShowDetail(newValue) {
			if (newValue) {
				uni.pageScrollTo({
					scrollTop: 0,
					duration: 0
				});
			}
		}
	},
	onPageScroll(e) {
		if (this.isShowDetail) {
			uni.pageScrollTo({
				scrollTop: 0,
				duration: 300
			});
		}
	},
	onBackPress() {
		if (this.isShowDetail) {
			this.isShowDetail = false;
			return true;
		}
		return false;
	},
	methods: {
		copyCdkey(key) {
			// #ifdef APP-PLUS
			uni.setClipboardData({
				data: key,
				success: function() {
					uni.showToast({
						title: '复制成功',
						icon: 'none'
					});
				}
			});
			// #endif
		},
		tabChange: function(e) {
			var index = e.target.id.replace('tabTag-', '');
			this.swiperCurrentIndex = index;
			this.tabCurrentIndex = index;
		},
		swiperChange: function(e) {
			var index = e.detail.current;
			this.tabCurrentIndex = index;
			console.log('当前tabCurrentIndex' + this.tabCurrentIndex);
		},
		//每个选项滚动到底部
		scrollend: function(e) {
			//获取是哪个选项滚动到底？
			var index = e.currentTarget.dataset.scindex;
			console.log(index);
			//可以利用 tabs 携带的分类id 与服务器交互请求对应分类的数据
			console.log(this.tabs[index].id);
			//加载更多的演示
			//判断当前是否正在加载
			if (this.tabs[index].loadingType === 1) {
				return false;
			}
			//判断是否是最后一页
			console.log(this.tabs[index].page);
			if (this.tabs[index].page > this.maxPage) {
				this.tabs[index].loadingType = 2; //全部
				return;
			}
			this.tabs[index].loadingType = 1; //加载中
			//模拟延迟
			this.getList(_self.tabs[index].page, list => {
				let all = list;
				let wait = list.filter(item => item.status === 0);
				let finish = list.filter(item => item.status === 2);
				if (index == 0) {
					_self.dataList[index] = _self.dataList[index].concat(all);
				} else if (index == 1) {
					_self.dataList[index] = _self.dataList[index].concat(wait);
				} else if (index == 2) {
					_self.dataList[index] = _self.dataList[index].concat(finish);
				}
				//分页
				_self.tabs[index].page++;
				_self.tabs[index].loadingType = 0; //恢复加载状态
			});
		},
		showDetail(obj) {
			(this.id = obj.id), (this.desc = obj.desc), (this.date = obj.date), (this.type = obj.type), (this.amount = obj.amount), (this.balance = obj.balance);
			this.isShowDetail = true;
		},
		backUp() {
			if (this.isShowDetail) {
				this.isShowDetail = false;
			} else {
				uni.navigateBack({
					delta: 1
				});
			}
		},
		getList(page, callback) {
			let _self = this;
			let param = {};
			if (page != -1) {
				param = {
					page: page
				};
			}
			uni.showLoading({
				title: '请稍后',
				mask: false
			});

			this.$api.get('api/order/getOrders', param).then(res => {
				if (_self.common.Response.isFaild(res.data)) {
					uni.showToast({
						title: '获取数据失败',
						icon: 'none'
					});
					return;
				} else if (_self.common.Response.isException(res.data)) {
					uni.showToast({
						title: res.data.msg,
						icon: 'none'
					});
					return;
				}
				_self.maxPage = res.data.count;
				uni.hideLoading();

				res.data.data.forEach(item => {
					if (item.productName.length > 13) {
						item.shortProductName = _self.common.String.textLimit(item.productName, 13);
					} else {
						item.shortProductName = item.productName;
					}

					item.statusText = '-';
					switch (item.status) {
						case 0:
							item.statusText = '待付款';
							break;
						case 1:
							item.statusText = '待发货';
							break;
						case 2:
							item.statusText = '已完成';
							break;
						default:
							item.statusText = '已冻结';
							break;
					}
				});

				if (callback != null) {
					callback(res.data.data);
				} else {
					let all = res.data.data;
					let wait = res.data.data.filter(item => item.status === 0);
					let finish = res.data.data.filter(item => item.status === 2);
					let arr = [[], [], []];
					arr[0] = _self.dataList[0].concat(all);
					arr[1] = _self.dataList[1].concat(wait);
					arr[2] = _self.dataList[2].concat(finish);
					_self.dataList = arr;
				}
			});
			setTimeout(() => {
				uni.hideLoading();
			}, 5000);
		}
	},
	components: {
		graceLoading,
		stackEmpty,
		uniNavBar
	}
};
</script>
<style lang="less" scoped>
@import '../../static/common.less';
page,
.grace-list-list {
	background-color: #ffffff !important;
}

.grace-tab-current {
	border-bottom: 2px solid #2296ea !important;
	color: #309deb;
}

.grace-tab-title {
	white-space: nowrap;
	text-align: center;
	background: #fff;
}

.grace-box-banner {
	/* background: #f3fbff; */
}

.amount {
	font-family: @common-font-num;
	color: #2296ea;
	font-size: 30px;
}

.grace-list-list-items {
	border-bottom: 1px solid #e6e6e6;
	width: 100%;
	height: 250upx;
	line-height: 250upx;
	display: flex;
}

.left {
	float: left;
}

.right {
	position: absolute;
	right: 30upx;
	text-align: right;
	justify-content: right;
	justify-items: right;
}

.left .desc,
.left .date {
	display: flex;
}

.left .desc {
	color: #000;
	font-size: 15px;
	height: 25px;
	font-weight: bold;
}
.left .date {
	color: #767676;
	font-size: 12px;
}

.right .price,
.right .price {
	font-size: 20px;
	font-family: @common-font-cn;
}

.right .price {
	color: #f11426;
}

.top-group-info {
	position: fixed;
	top: 0px;
}

page {
	font-family: @common-font-cn;
}

.detail-view {
	background-color: #fff;
	z-index: 9999;
	position: absolute;
	top: 60px;
	left: 0px;
	width: 100%;
	height: 100%;
}

/* #ifdef H5 */
.detail-view {
	top: 60px;
}
/* #endif */

/* #ifdef APP-PLUS */
.detail-view {
	top: 60px;
}
/* #endif */

.bottom-gray-line {
	border-bottom: 1px solid #c9c7c4;
}

.item-left {
	position: absolute;
	left: 20upx !important;

	justify-content: left !important;
	text-align: left !important;
	justify-items: left !important;
}

.detail .right {
	position: absolute;
	right: 20upx;
}

.trade-type {
	height: 100upx;
	line-height: 100upx;
	padding: 20upx;
}

.amount {
	font-size: 35px;
	color: #000;
	font-family: @common-font-num;
}

.detail {
	height: 350upx;
	line-height: 350upx;
	padding: 20upx;
	padding-top: 50upx;
}

.item {
	display: flex;
	height: 50upx;
	line-height: 50upx;
	overflow: hidden;
	margin-bottom: 10upx;
}

.remarks-item {
	display: flex;
	height: 150upx;
	line-height: 150upx;
	overflow: hidden;
	margin-bottom: 10upx;
}

.item-label {
	color: #888888;
	position: absolute;
	left: 20upx;
}

.date {
	font-size: 18px;
}

.trade-id {
	font-size: 12px;
}

.add {
	color: #03bc01;
}

.reduce {
	color: #000;
}

.remarks {
	display: flex;
	font-size: 12px;
	color: #c7c7c7;
	justify-content: right;
	text-align: right;
	justify-items: right;
	position: absolute;
	right: 20upx;
}

.uni-navbar-shadow {
	box-shadow: 0 0px 0px #ccc;
}

.uni-icon .uni-icon-back,
.uni-navbar .uni-view {
	color: #a5a5a5 !important;
}

.status_bar {
	height: var(--status-bar-height);
	width: 100%;
	background-color: #fff;
}
.top_view {
	height: var(--status-bar-height);
	width: 100%;
	position: fixed;
	background-color: #fff;
	top: 0;
	z-index: 999;
}

.label {
	font-size: 15px;
	color: #888888;
}

.order {
	width: 100%;
}

.order-header {
	min-width: 100%;
	height: 60upx;
	line-height: 60upx;
	overflow: hidden;
}

.order-detail {
	min-width: 100%;
	height: 150upx;
	line-height: 150upx;
}

.product-image {
	float: left;
	max-width: 130upx;
	max-height: 130upx;
	overflow: hidden;
	border-radius: 5upx;
	box-shadow: 0upx 0upx 12upx #d8d8d8;
}

.profile-text-detail {
	width: 100%;
}

.product-title {
	padding-left: 20upx;
	width: 65%;
	float: left;
	height: 50upx;
	line-height: 50upx;
	font-size: 18px;
}

.product-date {
	padding-left: 20upx;
	width: 65%;
	float: left;
	height: 50upx;
	line-height: 50upx;
	font-size: 13px;
	color: #929292;
}

.product-card {
	padding-left: 20upx;
	margin-top: 20upx;
	width: 65%;
	float: left;
	height: 50upx;
	line-height: 50upx;
	font-size: 13px;
	color: #929292;
}

.product-card-code {
	border: 1px dashed #0f8de8;
	padding: 10upx;
	background-color: #e8f2fb;
	color: #2497ea;
	font-size: 15px;
}

.order-price {
	color: #ff3e3e;
	font-family: @common-font-num;
	font-size: 20px;
}

.order-info-left {
	text-align: left;
	justify-content: left;
	justify-items: left;
	padding-left: 0 !important;
}

.order-info {
	width: 100%;
	text-align: left;
	justify-content: left;
	justify-items: left;
	padding-left: 0 !important;
}

/* #ifdef APP-PLUS */
.order-id {
	margin-left: -10px;
}
/* #endif */

.order-info text {
	text-align: left;
	justify-content: left;
	justify-items: left;
	padding-left: 0 !important;
	display: inline-flex;
	width: 35%;
}
</style>
