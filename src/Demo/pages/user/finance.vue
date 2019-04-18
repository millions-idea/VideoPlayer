<template>
	<view>
		<view class="grace-list ">
			<navigator class="items" url="./recharge">
				<view class="icons"><image class="icon" src="../../../static/user/chongzhi.png"></image></view>
				<view class="title">充值</view>
				<view class="arrow-right"></view>
			</navigator>
			<navigator class="items" url="./withdraw">
				<view class="icons"><image class="icon" src="../../../static/user/tixian.png"></image></view>
				<view class="title">
					提现
					<text>手续费2.00元</text>
				</view>
				<view class="arrow-right"></view>
			</navigator>
		</view>
	</view>
</template>

<script></script>

<style lang="less" scoped>
@import '../../static/common.less';

@icon-size: 30upx;

.grace-list {
	padding: 0 0;
}

.light-grace-list {
	border-top: 1px solid #f3f3f3 !important;
	border-bottom: 1px solid #f3f3f3 !important;
}

.grace-list .items .icons {
	width: @icon-size !important;
	height: @icon-size !important;
	line-height: @icon-size !important;
}

.grace-list .items .icons uni-image {
	width: @icon-size !important;
	height: @icon-size !important;
}

/* #ifdef APP-PLUS */
@app-icon-size: 40upx;

.grace-list .items .icons {
	width: @app-icon-size !important;
	height: @app-icon-size !important;
	line-height: @app-icon-size !important;
}

.grace-list .items .icons uni-image {
	width: @app-icon-size !important;
	height: @app-icon-size !important;
}
/* #endif */

.grace-list .items .title {
	font-size: 15px;
	font-family: @normal-text-font;
	line-height: @icon-size;
	height: @icon-size;
}

.grace-list .items .arrow-right {
}

.grace-common-mt {
	margin-top: 10px;
}

.items text {
	font-family: @normal-text-font;
}

.green {
	color: #0ea295 !important;
	margin-right: 15upx;
}

.first-grace-common-mt {
	margin-top: 0upx !important;
}

.grace-list .items .title {
	border-bottom: 1px solid #f3f3f3;
}
</style>
