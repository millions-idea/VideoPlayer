(global["webpackJsonp"]=global["webpackJsonp"]||[]).push([["pages/user/history"],{"4da7":function(t,n,e){"use strict";var o=function(){var t=this,n=t.$createElement;t._self._c},a=[];e.d(n,"a",function(){return o}),e.d(n,"b",function(){return a})},"5c76":function(t,n,e){},"9eda":function(t,n,e){"use strict";e.r(n);var o=e("4da7"),a=e("cbf1");for(var i in a)"default"!==i&&function(t){e.d(n,t,function(){return a[t]})}(i);e("dc70");var r=e("2877"),u=Object(r["a"])(a["default"],o["a"],o["b"],!1,null,"10dd2556",null);n["default"]=u.exports},a2be:function(t,n,e){"use strict";(function(t){Object.defineProperty(n,"__esModule",{value:!0}),n.default=void 0;var o=e("2f62");function a(t){for(var n=1;n<arguments.length;n++){var e=null!=arguments[n]?arguments[n]:{},o=Object.keys(e);"function"===typeof Object.getOwnPropertySymbols&&(o=o.concat(Object.getOwnPropertySymbols(e).filter(function(t){return Object.getOwnPropertyDescriptor(e,t).enumerable}))),o.forEach(function(n){i(t,n,e[n])})}return t}function i(t,n,e){return n in t?Object.defineProperty(t,n,{value:e,enumerable:!0,configurable:!0,writable:!0}):t[n]=e,t}var r=0,u=function(){return e.e("graceUI/components/graceLoading").then(e.bind(null,"101f"))},c=function(){return e.e("components/stack-empty/stack-empty").then(e.bind(null,"a56a"))},s=function(){return e.e("components/mht-loader/mht-loader").then(e.bind(null,"e0d5"))},d={components:{graceLoading:u,stackEmpty:c,mhtLoader:s},data:function(){return{loading:!1,loadingType:0,isEnd:!1,dataList:[],maxPage:1e3,empty:!0}},onLoad:function(t){this,r=0},onShow:function(){this.getList()},onNavigationBarButtonTap:function(){t.switchTab({url:"./category"})},computed:a({},(0,o.mapState)(["hasLogin","profile"])),onPullDownRefresh:function(){console.log("onPullDownRefresh"," at pages\\user\\history.vue:76"),this.getList()},onReachBottom:function(){this.getList()},onBackPress:function(){this.loadingType=0,this.isEnd=!1,r=0},methods:a({},(0,o.mapActions)(["setProfile","authOpenWindow","login"]),{getList:function(){var t=this;if(this.loading=!0,r>this.maxPage)return this.loading=!1,this.isEnd=!0,void(this.loadingType=2);this.loadingType=1;var n=this;this.$api.get("api/user/getPlayHistory",{page:r}).then(function(e){n.loadingType=3,t.loading=!1,200==e.data.code&&(n.maxPage=e.data.count,null!=e.data.data&&0!=e.data.data.length||(n.loadingType=3),e.data.data.forEach(function(t){t.amount=t.amount.toFixed(2),t.showCount=t.showCount>9999?(t.showCount/1e4).toFixed(1)+"万":t.showCount}),n.dataList=n.dataList.concat(e.data.data),n.loadingType=0,r++)})},onPlay:function(t){this.hasLogin?this.common.window.toNew("index/player",{videoId:t.productId,title:t.productName,poster:t.image}):this.common.window.toNew("user/bootstrap/login",null)}})};n.default=d}).call(this,e("6e42")["default"])},cbf1:function(t,n,e){"use strict";e.r(n);var o=e("a2be"),a=e.n(o);for(var i in o)"default"!==i&&function(t){e.d(n,t,function(){return o[t]})}(i);n["default"]=a.a},dc70:function(t,n,e){"use strict";var o=e("5c76"),a=e.n(o);a.a}},[["9eeb","common/runtime","common/vendor"]]]);