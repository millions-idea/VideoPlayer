<script>
import constant from '@/common/constant'

export default {
	onLaunch: function() {
		console.log('App Launch');
	},
	onShow: function() {
		console.log('App Show'); 
		
	},
	onHide: function() {
		console.log('App Hide');
	},
	//vuex页面刷新后数据丢失解决方案,监听webview刷新事件
	mounted: function() {
		// #ifdef H5
		window.addEventListener('unload', this.saveState);
		// #endif
	},
	onUnload: function() {
		console.log('App unload');
		// #ifdef APP-PLUS
		this.saveState();
		// #endif
	},
	methods: {
		saveState() {
			try {
				uni.setStorageSync('state', JSON.stringify(this.$store.state));
			} catch (e) {}
		}
	}
};
</script>

<style lang="less">
/*每个页面公共css */
@import './graceUI/graceUI.css';

@import './static/common.less';

page {
	background-color: #fff;//f5
}
</style>
