<template>
	<view>
		<mSearch :show="false" @search="search($event, 1)" placeholder="请输入手机号搜索"></mSearch>

		<view class="grace-padding top-group-info">
			<view class="grace-box-banner" style="margin:30rpx 0;">
				<view class="garce-items" style="width:33%;">
					<view class="line1 amount">
						{{ directMemberCount }}
						<text>人</text>
					</view>
					<view class="line2">直接销售</view>
				</view>
				<view class="garce-items" style="width:33%;">
					<view class="line1 amount">
						{{ smallMarketMemberCount }}
						<text>人</text>
					</view>
					<view class="line2">小市场</view>
				</view>
				<view class="garce-items" style="width:33%;">
					<view class="line1 amount">
						{{ channelMemberCount }}
						<text>人</text>
					</view>
					<view class="line2">渠道销售</view>
				</view>
			</view>
		</view>

		<template v-if="true">
			<view class="scroll-container">
				<swiper class="grace-tab-swiper-full" :current="swiperCurrentIndex" @change="swiperChange" :style="{ height: tabHeight + 'px' }">
					<!-- 循环新闻项目 -->
					<swiper-item v-for="(list, listIndex) in dataList" :key="listIndex">
						<template v-if="dataList.length == 0 || dataList[0].length == 0">
							<stackEmpty height="180" label="暂无数据"></stackEmpty>
						</template>
						<template v-else>
							<scroll-view scroll-y="true" :data-scindex="listIndex" @scrolltolower="scrollend">
								<view class="grace-list-list" style="padding:25upx; width:700upx;">
									<navigator url="" v-for="(item, index) in list" :key="index">
										<view class="grace-list-list-items">
											<view class="left">
												<text class="desc" selectable>{{ item.phone }}</text>
												<text class="date">{{ item.addDate }}</text>
											</view>
											<view class="right">
												<view>
													<text class="member-grade">直营销售{{ item.directSaleCount }}张</text>
													<text class="member-grade">渠道销售{{ item.channelSaleCount }}张</text>
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
			</view>
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
import graceLoading from '@/graceUI/components/graceLoading.vue';
import stackEmpty from '@/components/stack-empty/stack-empty.vue';
import uniNavBar from '@/components/uni-nav-bar/uni-nav-bar.vue';
import mSearch from '@/components/mehaotian-search/mehaotian-search.vue';

export default {
	data() {
		return {
			directMemberCount: 0,
			smallMarketMemberCount: 0,
			channelMemberCount: 0,

			tabCurrentIndex: 0,
			swiperCurrentIndex: 0,
			tabHeight: 300,
			tabs: [
				//标签名称 , 分类 id , 加载更多, 加载的页码
				{ name: '我的团队成员', id: 'members', loadingType: 0, page: 1 }
			],

			maxPage: 1000,
			dataList: [],

			desc: '',
			date: '',
			type: 0,
			amount: '',
			id: '',
			balance: '',
			isShowDetail: false,

			scrollTop: 0,
			val1: ''
		};
	},
	onLoad: function() {
		_self = this;
		this.dataList.forEach(d => {
			d.forEach(item => {
				if (item.desc.length > 12) {
					item.shortDesc = this.common.String.textLimit(item.desc, 12);
				} else {
					item.shortDesc = item.desc;
				}
			});
		});

		//查询统计信息
		this.getTeamPreview();

		//查询成员名单
		this.getTeamMembers(-1, null, null);
	},
	onReady: function() {
		//获取屏幕高度及比例
		var winInfo = uni.getSystemInfo({
			success: function(res) {
				var windowHeight = res.windowHeight;
				_self.tabHeight = windowHeight - 180;
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
		search(e, val) {
			console.log(e, val);
			this.getTeamMembers(-1, e, null);
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

			this.getTeamMembers(_self.tabs[index].page, null, list => {
				_self.dataList[index] = _self.dataList[index].concat(list[index]);
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
		getTeamPreview() {
			let _self = this;
			this.$api.get('api/team/getTeamPreview', {}).then(res => {
				if (_self.common.Response.isFaild(res.data)) {
					uni.showToast({
						title: '获取统计信息失败',
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

				_self.directMemberCount = res.data.msg.directSaleCount;
				_self.smallMarketMemberCount = res.data.msg.samllMarketCount;
				_self.channelMemberCount = res.data.msg.channelSaleCount;
			});
		},
		getTeamMembers(page, phone, callback) {
			let _self = this;
			let param = {};
			if (page != -1) {
				param = {
					page: page
				};
			}
			if (phone != null) {
				param = {
					phone: phone
				};
			}

			uni.showLoading({
				title: '请稍后',
				mask: false
			});

			this.$api.get('api/team/getTeamMembers', param).then(res => {
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
				if (callback != null) {
					callback(res.data.data);
				} else {
					_self.dataList = res.data.data;
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
		uniNavBar,
		mSearch
	}
};
</script>
<style lang="less" scoped>
@import '../../static/common.less';

page {
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
	height: 150upx;
	line-height: 150upx;
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

.right .add,
.right .reduce {
	font-size: 20px;
	font-family: @common-font-cn;
}

.right .add {
	color: #03bc01;
}

.right .reduce {
	color: #000;
}

.top-group-info {
	background-color: #fff !important;
	z-index: 9999;
	border-top: 0px solid #f7f7f7;
	border-bottom: 1px solid #f7f7f7;
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
.member-grade {
	font-size: 16px;
	margin-left: 20upx;

	border: 1px dashed #0f8de8;
	padding: 10upx;
	background-color: #e8f2fb;
	color: #2497ea;
	font-size: 15px;
}

.scroll-container {
	background-color: #fff !important;
}
</style>
