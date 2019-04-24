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

		<!-- <view class="grace-padding top-group-info">
			<view class="grace-h4 grace-blod grace-center">我的账单</view>
			<view class="grace-box-banner" style="margin:30rpx 0;">
				<view class="garce-items" style="width:33%;">
					<view class="line1 amount">
						100001
						<text>元</text>
					</view>
					<view class="line2">我的资产</view>
				</view>
				<view class="garce-items" style="width:33%;">
					<view class="line1 amount">
						500
						<text>元</text>
					</view>
					<view class="line2">收入(月)</view>
				</view>
				<view class="garce-items" style="width:33%;">
					<view class="line1 amount">
						2000
						<text>元</text>
					</view>
					<view class="line2">支出(月)</view>
				</view>
			</view>
		</view> -->
		<template v-if="true">
			<scroll-view class="grace-tab-title grace-center" scroll-x="true" id="grace-tab-title">
				<view
					v-for="(tab, index) in tabs"
					:key="index"
					:class="[tabCurrentIndex == index ? 'grace-tab-current' : '']"
					:id="'tabTag-' + index"
					@tap="tabChange"
				>
					{{ tab.name }}
				</view>
			</scroll-view>

			<swiper
				class="grace-tab-swiper-full"
				:current="swiperCurrentIndex"
				@change="swiperChange"
				:style="{ height: tabHeight + 'px' }"
			>
				<!-- 循环新闻项目 -->
				<swiper-item v-for="(list, listIndex) in dataList" :key="listIndex">
					<template v-if="dataList.length == 0">
						<stackEmpty height="180" label="暂无数据"></stackEmpty>
					</template>
					<template v-else-if="tabCurrentIndex == 0 && dataList[0].length == 0">
						<stackEmpty height="180" label="暂无交易流水"></stackEmpty>
					</template>
					<template v-else-if="tabCurrentIndex == 1 && dataList[1].length == 0">
						<stackEmpty height="180" label="暂无提现记录"></stackEmpty>
					</template>
					<template v-else>
						<scroll-view
							scroll-y="true"
							:data-scindex="listIndex"
							@scrolltolower="scrollend"
						>
							<view class="grace-list-list" style="padding:25upx; width:700upx;">
								<navigator
									@tap="showDetail(item)"
									url=""
									v-for="(item, index) in list"
									:key="index"
								>
									<view class="grace-list-list-items">
										<view class="left">
											<text class="desc">{{ item.shortRemark == null ? item.tradeName : item.shortRemark }}</text>
											<text class="date">{{ item.addTime }}</text>
										</view>
										<view class="right">
											<template v-if="item.tradeType == 10 || item.tradeType == 6">
												<view>
													<text class="add">+ {{ item.amount }}</text>
												</view>
											</template>
											<template v-else>
												<view>
													<text class="reduce">- {{ item.amount }}</text>
												</view>
											</template>
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
					<template v-if="type == 10 || type == 6">
						<text class="item-left item-label">入账金额</text>
						<text class="right amount add">{{ amount }}</text>
					</template>
					<template v-else>
						<text class="item-left item-label">出账金额</text>
						<text class="right amount reduce">{{ amount }}</text>
					</template>
				</view>
				<view class="detail ">
					<view class="item">
						<text class="item-left item-label">类型</text>
						<template v-if="type == 10 || type == 6">
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
					<!-- <view class="item">
						<text class="item-left item-label">剩余零钱</text>
						<text class="right type">{{ balance }}</text>
					</view> -->
					<view class="item"><text class="item-left item-label">备注</text></view>
					<view class="item">
						<text class="remarks type">{{ desc }}</text>
					</view>
					 <template v-if="state != null && state >= 0">
					 	<view class="item"><text class="item-left item-label">到账状态</text></view>
						<template  v-if="state == 0" >
							<view class="item">
								<text class="right type trade-id" >待审核</text>
							</view>
						</template>
					 	<template  v-if="state == 1" >
					 		<view class="item">
					 			<text class="right type trade-id" >已通过</text>
					 		</view>
					 	</template>
						<template  v-if="state == 2" >
							<template  v-if="remark == null" >
								<view class="item">
									<text class="right type trade-id" >被驳回</text>
								</view>
							</template>
							<template v-else>
								<view class="item">
									<text class="right type trade-id" >被驳回，原因：{{withdrawRemark}}</text>
								</view>
							</template> 
						</template>
					 </template>
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
export default {
	data() {
		return {
			tabCurrentIndex: 0,
			swiperCurrentIndex: 0,
			tabHeight: 300,
			tabs: [
				//标签名称 , 分类 id , 加载更多, 加载的页码
				{ name: '交易流水', cid: 0, id: 'liushui', loadingType: 0, page: 1 },
				{ name: '提现申请', cid: 1, id: 'tixian', loadingType: 0, page: 1 }
			],
			maxPage: 0,
			dataList: [],

			desc: '',
			date: '',
			type: 0,
			amount: '',
			id: '',
			balance: '',
			isShowDetail: false,
			state: 0,
			remark: "",
			withdrawRemark:"",

			scrollTop: 0
		};
	},
	onLoad: function() {
		_self = this;
		//查询名单
		this.getGroupList(0, -1, null);
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
			this.getGroupList(this.tabs[index].cid, _self.tabs[index].page, (list) => {
				_self.dataList[index] = _self.dataList[index].concat(list[index]);
				//分页
				_self.tabs[index].page++;
				_self.tabs[index].loadingType = 0; //恢复加载状态
			});
		},
		showDetail(obj) {
			(this.id = obj.payId),
				(this.desc = obj.remark == null ? obj.tradeName : obj.remark),
				(this.date = obj.addTime),
				(this.type = obj.tradeType),
				(this.amount = obj.amount);
				(this.state = obj.state);
				(this.remark = obj.remark);
				(this.withdrawRemark = obj.withdrawRemark);
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
		getGroupList(categoryId, page, callback){
			let _self = this;
			let param = {
				categoryId: categoryId
			};
			if(page != -1){
				param = {
					categoryId: categoryId,
					page: page
				};
			}
			
			uni.showLoading({
				title: '请稍后',
				mask: false
			});
			
			this.$api.get("api/bill/getGroupList", param).then((res) => {
				if(_self.common.Response.isFaild(res.data)){
					uni.showToast({
						title: "获取数据失败",
						icon: "none"
					})
					return;
				}else if(_self.common.Response.isException(res.data)){
					uni.showToast({
						title: res.data.msg,
						icon: "none"
					})
					return;
				}
				_self.maxPage = res.data.count;
				uni.hideLoading();
				if(callback != null){
					callback(res.data.data);
				}else{
					_self.dataList = res.data.data;
				}
			});
			setTimeout(()=>{
				uni.hideLoading();
			}, 5000)
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
@import '../../../static/common.less';
page, .grace-list-list{
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
	top: 120upx !important;
	left: 0px;
	width: 100%;
	height: 100%;
}

/* #ifdef H5 */
.detail-view {
	top: 60upx !important;
}
/* #endif */

/* #ifdef APP-PLUS */
.detail-view {
	top: 120upx !important;
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
	height: 400upx;
	line-height: 400upx;
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
</style>
