(global["webpackJsonp"]=global["webpackJsonp"]||[]).push([["components/jing-swiper/jing-swiper"],{"3cd5":function(n,t,e){"use strict";(function(n){Object.defineProperty(t,"__esModule",{value:!0}),t.default=void 0;var e={name:"jingSwiper",props:{imgList:Array,duration:0,interval:0},components:{},data:function(){return{currentIndex:0}},created:function(){this.imgList.forEach(function(n){console.log(n," at components\\jing-swiper\\jing-swiper.vue:39")})},computed:{margin:function(){return n.upx2px(50)+"px"}},methods:{handleChange:function(n){this.currentIndex=n.detail.current},onClick:function(n,t){this.$emit("click",{index:n,item:t})}}};t.default=e}).call(this,e("6e42")["default"])},"41eb":function(n,t,e){"use strict";var i=e("7761"),r=e.n(i);r.a},4265:function(n,t,e){"use strict";e.r(t);var i=e("c176"),r=e("4a7e");for(var u in r)"default"!==u&&function(n){e.d(t,n,function(){return r[n]})}(u);e("41eb");var c=e("2877"),a=Object(c["a"])(r["default"],i["a"],i["b"],!1,null,"13a6a82f",null);t["default"]=a.exports},"4a7e":function(n,t,e){"use strict";e.r(t);var i=e("3cd5"),r=e.n(i);for(var u in i)"default"!==u&&function(n){e.d(t,n,function(){return i[n]})}(u);t["default"]=r.a},7761:function(n,t,e){},c176:function(n,t,e){"use strict";var i=function(){var n=this,t=n.$createElement;n._self._c},r=[];e.d(t,"a",function(){return i}),e.d(t,"b",function(){return r})}}]);
;(global["webpackJsonp"] = global["webpackJsonp"] || []).push([
    'components/jing-swiper/jing-swiper-create-component',
    {
        'components/jing-swiper/jing-swiper-create-component':(function(module, exports, __webpack_require__){
            __webpack_require__('6e42')['createComponent'](__webpack_require__("4265"))
        })
    },
    [['components/jing-swiper/jing-swiper-create-component']]
]);                
