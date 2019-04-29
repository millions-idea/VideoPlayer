<template>
	<view>
		<view class="blank-line-20"></view>
		<view class="flex-container">
			<view class="least-container">
				<view class="left_1">
					<view class="left_1_box"><image @tap="onClick(left_1_box)" :src="left_1_box.image" class="box_img"></image></view>
				</view>
				<view class="right_1">
					<view class="top_1"><image @tap="onClick(top_1)" :src="top_1.image" class="box_img"></image></view>
					<view class="bottom_1">
						<view class="left_box_2"><image @tap="onClick(left_box_2)" :src="left_box_2.image" class="box_img"></image></view>
						<view class="right_box_2"><image @tap="onClick(right_box_2)" :src="right_box_2.image" class="box_img"></image></view>
					</view>
				</view>
			</view>
			<view class="largest-container">
				<view class="largest-box"><image @tap="onClick(largest_box)" :src="largest_box.image" class="box_img"></image></view>
			</view>
			<view class="balance-container">
				<view class="balance-box">
					<view class="left_box_3"><image @tap="onClick(left_box_3)" :src="left_box_3.image" class="box_img"></image></view>
					<view class="right_box_3"><image @tap="onClick(right_box_3)" :src="right_box_3.image" class="box_img"></image></view>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
export default {
	data() {
		return {
			dataList: [],
			left_1_box: {},
			top_1: {},
			left_box_2: {},
			right_box_2: {},
			largest_box: {},
			left_box_3: {},
			right_box_3: {}
		};
	},
	onLoad() {
		uni.startPullDownRefresh();
	},
	//上拉加载更多
	onReachBottom: function() {
		this.getList();
	},
	onPullDownRefresh() {
		//监听下拉刷新动作
		console.log('onPullDownRefresh');
		this.getList();
	},
	methods: {
		onClick(e) {
			this.common.window.toNew('index/detail', {
				categoryId: e.categoryId,
				categoryName: e.text
			});
		},
		getList() {
			let cache = this.session.getValue('categorys');

			if (cache != null && JSON.stringify(cache).length > 5) {
				cache.forEach(item => {
					switch (item.className) {
						case 'left_1_box':
							this.left_1_box = item;
							break;
						case 'top_1':
							this.top_1 = item;
							break;
						case 'left_box_2':
							this.left_box_2 = item;
							break;
						case 'right_box_2':
							this.right_box_2 = item;
							break;
						case 'largest_box':
							this.largest_box = item;
							break;
						case 'left_box_3':
							this.left_box_3 = item;
							break;
						case 'right_box_3':
							this.right_box_3 = item;
							break;
					}
				});
			}

			this.$api.get('api/category/getAllCategory', {}).then(res => {
				uni.stopPullDownRefresh();

				let newDataList = [];

				if (this.common.Response.isFaild(res.data)) {
					let categorys = this.session.getValue('categorys');
					if (categorys != null && JSON.stringify(categorys).length > 5) {
						newDataList = categorys;
					} else {
						uni.showToast({
							title: '加载失败, 请稍后重试!',
							icon: 'none'
						});
						return;
					}
				} else if (this.common.Response.isException(res.data)) {
					let categorys = this.session.getValue('categorys');
					if (categorys != null && JSON.stringify(categorys).length > 5) {
						newDataList = categorys;
					} else {
						uni.showToast({
							title: res.data.msg,
							icon: 'none'
						});
						return;
					}
				}

				newDataList = res.data.data;

				this.dataList = newDataList;

				this.session.setValue('categorys', this.dataList);

				this.dataList.forEach(item => {
					switch (item.className) {
						case 'left_1_box':
							this.left_1_box = item;
							break;
						case 'top_1':
							this.top_1 = item;
							break;
						case 'left_box_2':
							this.left_box_2 = item;
							break;
						case 'right_box_2':
							this.right_box_2 = item;
							break;
						case 'largest_box':
							this.largest_box = item;
							break;
						case 'left_box_3':
							this.left_box_3 = item;
							break;
						case 'right_box_3':
							this.right_box_3 = item;
							break;
					}
				});
			});
		}
	}
};
</script>

<style lang="less" scoped>
@import '../../static/common.less';

page {
	background-color: #fff;
}

.flex-container {
	display: flex;
	flex-direction: row;
	flex-wrap: wrap;
	width: 100%;
	height: 100%;
}

.least-container {
	width: 95%;
	margin: 0 auto;
	min-height: 262upx;
	display: flex;
	justify-content: center;
}

.least-container .left_1 {
	height: 262upx;
	width: 50%;
	overflow: hidden;
}

.least-container .right_1 {
	height: 262upx;
	width: 50%;
	overflow: hidden;
}

.left_1_box {
	width: 95%;
	height: 248upx;
	margin: 0 auto;
	margin-top: 8upx;
	border-radius: 10upx;
	overflow: hidden;
}

.right_1 .top_1 {
	width: 95%;
	height: 120upx;
	margin: 0 auto;
	margin-top: 8upx;
	border-radius: 10upx;
	overflow: hidden;
}

.bottom_1 {
	width: 95%;
	height: 120upx;
	margin: 0 auto;
	margin-top: 8upx;
	overflow: hidden;
	display: flex;
	justify-content: space-between;
}

.left_box_2,
.right_box_2 {
	width: 49%;
	height: 120upx;
	border-radius: 10upx;
	overflow: hidden;
}

.largest-container {
	width: 95%;
	margin: 0 auto;
	min-height: 280upx;
	display: flex;
	justify-content: center;
}

.largest-box {
	width: 97%;
	height: 248upx;
	margin: 0 auto;
	margin-top: 28upx;
	border-radius: 10upx;
	overflow: hidden;
}

.balance-container {
	width: 95%;
	margin: 0 auto;
	min-height: 300upx;
	display: flex;
}

.balance-box {
	width: 97%;
	height: 248upx;
	margin: 0 auto;
	margin-top: 28upx;
	border-radius: 10upx;
	overflow: hidden;
	display: flex;
	justify-content: space-between;
}

.left_box_3,
.right_box_3 {
	width: 49.4%;
	width: 49.4%;
	height: 248upx;
	border-radius: 10upx;
	margin-top: 0upx;
	overflow: hidden;
}

.box_img {
	width: 100%;
	height: 100%;
	background-color: #FAFAFA;
	
}
</style>
