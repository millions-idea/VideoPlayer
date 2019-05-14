(global["webpackJsonp"]=global["webpackJsonp"]||[]).push([["components/jing-swiper/jing-swiper"],{"2e98":function(n,t,e){},"3cd5":function(n,t,e){"use strict";(function(n){Object.defineProperty(t,"__esModule",{value:!0}),t.default=void 0;var e={name:"jingSwiper",props:{imgList:Array,duration:0,interval:0},components:{},data:function(){return{currentIndex:0}},created:function(){this.imgList.forEach(function(n){console.log(n," at components\\jing-swiper\\jing-swiper.vue:37")})},computed:{margin:function(){return n.upx2px(50)+"px"}},methods:{handleChange:function(n){this.currentIndex=n.detail.current},openBrowser:function(n){this.$emit("click",n)}}};t.default=e}).call(this,e("6e42")["default"])},4265:function(n,t,e){"use strict";e.r(t);var r=e("b65f"),i=e("4a7e");for(var u in i)"default"!==u&&function(n){e.d(t,n,function(){return i[n]})}(u);e("f509");var c=e("2877"),o=Object(c["a"])(i["default"],r["a"],r["b"],!1,null,"69b54c17",null);t["default"]=o.exports},"4a7e":function(n,t,e){"use strict";e.r(t);var r=e("3cd5"),i=e.n(r);for(var u in r)"default"!==u&&function(n){e.d(t,n,function(){return r[n]})}(u);t["default"]=i.a},b65f:function(n,t,e){"use strict";var r=function(){var n=this,t=n.$createElement;n._self._c},i=[];e.d(t,"a",function(){return r}),e.d(t,"b",function(){return i})},f509:function(n,t,e){"use strict";var r=e("2e98"),i=e.n(r);i.a}}]);
;(global["webpackJsonp"] = global["webpackJsonp"] || []).push([
    'components/jing-swiper/jing-swiper-create-component',
    {
        'components/jing-swiper/jing-swiper-create-component':(function(module, exports, __webpack_require__){
            __webpack_require__('6e42')['createComponent'](__webpack_require__("4265"))
        })
    },
    [['components/jing-swiper/jing-swiper-create-component']]
]);                
