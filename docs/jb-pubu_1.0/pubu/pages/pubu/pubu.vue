<!--模板-->
<template>
  <div id="bbb">
    <div id="vm" class="pubu" v-cloak>
      <ul id="show" class="yg yg_l">
        <li v-for="(item,index) in ListData" :key="index" v-if="index%2==0">
          <div style="position: relative;">
            <img :src="item.e_img" alt="">
            <div class="u_xinx">
              <img :src="item.u_img" alt="">
              <span>{{item.u_name}}</span>
            </div>
          </div>
          <p>{{item.e_intro}}</p>
        </li>
      </ul>
      <ul class="yg yg_r">
        <li v-for="(item,index) in ListData" :key="index" v-if="index%2==1" >
          <div style="position: relative;">
            <img :src="item.e_img" alt="">
            <div class="u_xinx">
              <img :src="item.u_img" alt="">
              <span>{{item.u_name}}</span>
            </div>
          </div>
          <p>{{item.e_intro}}</p>
        </li>
      </ul>
      <div style="clear: both;"></div>
    </div>
  </div>
</template>


<!--JS-->
<script>
  export default {
    data() {
      return {
        ListData:[],
        imgList:[
          {
            "u_id": "512",
            "u_name": "开发者",
            "u_img": "http://xxiaoyuan.top/static/js/upload/uploads/153923102266562.jpeg",
            "u_date": "2018-05-31 20:22:08",
            "e_id": "100",
            "e_type": "0",
            "e_title": "我眼中的顺职",
            "e_intro": "顺德职业技术",
            "e_p": "<p>2015年的夏天，我怀揣着对大学的憧憬来到顺职。我满怀欣喜地到梁銶琚礼堂报到，正式地成为了一名顺职院的学生。</p>\r\n\r\n<p>闲下来的时光我逛逛了校园，下面来给大家看看我眼中的顺职院吧。漫步湖边，我看到了桃苑最美的模样。不知道平时的你是不是跟我一样很喜欢在桃苑吃饭呢。</p>\r\n<p>最喜欢的事情，就是在夜晚漫步湖边，走累了，随处可见的石凳可以供学生们休息。偶尔运气好的，就能遇到湖边自己拉音响唱歌的小伙伴，这个时候如果你恰好有兴趣，坐着听听歌也无妨。</p>\r\n<p>我认为学校最有特色的东西，就是这个天鹅蛋石头，每次从桥边走过看到这个天鹅蛋样的巨型石头下面一群天鹅戏耍的时候，我总会想，小天鹅们会不会以为那是自己的小伙伴下的蛋呢。</p>\r\n<img src=\"./../static/img/banner/banner2.jpg\" alt=\"\">\r\n<p>从智慧门往正门看去，有没有一种宏伟壮丽，高端大气的感觉呢。</p>\r\n<p>玫瑰园的玫瑰竞相开放，争奇斗艳，盛开的玫瑰，给了我们视觉上的享受，但是，玫瑰虽美，可不能随手摘哦，据说有很多个监控的呢，摘了可是要罚钱的哦</p>\r\n<p>环湖的全景图，蓝天白云，波光浩荡，有没有想要来看一看的冲动呢</p>\r\n<img src=\"./../static/img/banner/banner3.jpg\" alt=\"\">\r\n<p>站在四合院最高楼俯视生活区，一栋栋的宿舍楼环湖而建，晚上夜深人静的时候，搬张凳子坐在阳台，看看夜景，你会发现一个不一样的顺职。\r\n快来留言你眼中最美的顺职吧！！！\r\n</p>",
            "e_img": "http://xxiaoyuan.top/static/img/banner/banner1.jpg",
            "e_user": "512",
            "e_date": "2018-05-25 00:00:00",
            "e_is_index": "1"
          },
          {
            "u_id": "512",
            "u_name": "开发者",
            "u_img": "http://xxiaoyuan.top/static/js/upload/uploads/153923102266562.jpeg",
            "u_date": "2018-05-31 20:22:08",
            "e_id": "100",
            "e_type": "0",
            "e_title": "我眼中的顺职",
            "e_intro": "顺德职业技术学院是我美丽的大学，那里春满花开，四季常绿，鸟语花香。教室里的朗朗书声，操场上的滴滴汗水，正是青春的象征",
            "e_p": "<p>2015年的夏天，我怀揣着对大学的憧憬来到顺职。我满怀欣喜地到梁銶琚礼堂报到，正式地成为了一名顺职院的学生。</p>\r\n\r\n<p>闲下来的时光我逛逛了校园，下面来给大家看看我眼中的顺职院吧。漫步湖边，我看到了桃苑最美的模样。不知道平时的你是不是跟我一样很喜欢在桃苑吃饭呢。</p>\r\n<p>最喜欢的事情，就是在夜晚漫步湖边，走累了，随处可见的石凳可以供学生们休息。偶尔运气好的，就能遇到湖边自己拉音响唱歌的小伙伴，这个时候如果你恰好有兴趣，坐着听听歌也无妨。</p>\r\n<p>我认为学校最有特色的东西，就是这个天鹅蛋石头，每次从桥边走过看到这个天鹅蛋样的巨型石头下面一群天鹅戏耍的时候，我总会想，小天鹅们会不会以为那是自己的小伙伴下的蛋呢。</p>\r\n<img src=\"./../static/img/banner/banner2.jpg\" alt=\"\">\r\n<p>从智慧门往正门看去，有没有一种宏伟壮丽，高端大气的感觉呢。</p>\r\n<p>玫瑰园的玫瑰竞相开放，争奇斗艳，盛开的玫瑰，给了我们视觉上的享受，但是，玫瑰虽美，可不能随手摘哦，据说有很多个监控的呢，摘了可是要罚钱的哦</p>\r\n<p>环湖的全景图，蓝天白云，波光浩荡，有没有想要来看一看的冲动呢</p>\r\n<img src=\"./../static/img/banner/banner3.jpg\" alt=\"\">\r\n<p>站在四合院最高楼俯视生活区，一栋栋的宿舍楼环湖而建，晚上夜深人静的时候，搬张凳子坐在阳台，看看夜景，你会发现一个不一样的顺职。\r\n快来留言你眼中最美的顺职吧！！！\r\n</p>",
            "e_img": "http://xxiaoyuan.top/static/img/banner/banner1.jpg",
            "e_user": "512",
            "e_date": "2018-05-25 00:00:00",
            "e_is_index": "1"
          },
          {
            "u_id": "512",
            "u_name": "开发者",
            "u_img": "http://xxiaoyuan.top/static/js/upload/uploads/153923102266562.jpeg",
            "u_date": "2018-05-31 20:22:08",
            "e_id": "100",
            "e_type": "0",
            "e_title": "我眼中的顺职",
            "e_intro": "顺德职业技术学院是我美丽的大学，那里春满花开，四季常绿，四季常绿，鸟语花香。教室里的朗朗书声，操场上的滴滴汗水，正是青春的象征鸟语花香。教室里的朗朗书声，操场上的滴滴汗水，正是青春的象征",
            "e_p": "<p>2015年的夏天，我怀揣着对大学的憧憬来到顺职。我满怀欣喜地到梁銶琚礼堂报到，正式地成为了一名顺职院的学生。</p>\r\n\r\n<p>闲下来的时光我逛逛了校园，下面来给大家看看我眼中的顺职院吧。漫步湖边，我看到了桃苑最美的模样。不知道平时的你是不是跟我一样很喜欢在桃苑吃饭呢。</p>\r\n<p>最喜欢的事情，就是在夜晚漫步湖边，走累了，随处可见的石凳可以供学生们休息。偶尔运气好的，就能遇到湖边自己拉音响唱歌的小伙伴，这个时候如果你恰好有兴趣，坐着听听歌也无妨。</p>\r\n<p>我认为学校最有特色的东西，就是这个天鹅蛋石头，每次从桥边走过看到这个天鹅蛋样的巨型石头下面一群天鹅戏耍的时候，我总会想，小天鹅们会不会以为那是自己的小伙伴下的蛋呢。</p>\r\n<img src=\"./../static/img/banner/banner2.jpg\" alt=\"\">\r\n<p>从智慧门往正门看去，有没有一种宏伟壮丽，高端大气的感觉呢。</p>\r\n<p>玫瑰园的玫瑰竞相开放，争奇斗艳，盛开的玫瑰，给了我们视觉上的享受，但是，玫瑰虽美，可不能随手摘哦，据说有很多个监控的呢，摘了可是要罚钱的哦</p>\r\n<p>环湖的全景图，蓝天白云，波光浩荡，有没有想要来看一看的冲动呢</p>\r\n<img src=\"./../static/img/banner/banner3.jpg\" alt=\"\">\r\n<p>站在四合院最高楼俯视生活区，一栋栋的宿舍楼环湖而建，晚上夜深人静的时候，搬张凳子坐在阳台，看看夜景，你会发现一个不一样的顺职。\r\n快来留言你眼中最美的顺职吧！！！\r\n</p>",
            "e_img": "http://xxiaoyuan.top/static/img/banner/banner1.jpg",
            "e_user": "512",
            "e_date": "2018-05-25 00:00:00",
            "e_is_index": "1"
          },
          {
            "u_id": "512",
            "u_name": "开发者",
            "u_img": "http://xxiaoyuan.top/static/js/upload/uploads/153923102266562.jpeg",
            "u_date": "2018-05-31 20:22:08",
            "e_id": "100",
            "e_type": "0",
            "e_title": "我眼中的顺职",
            "e_intro": "顺德职业技术学院是我美丽的大学，那里春满花开，四季常绿，四季常绿，鸟语花香。教室里的朗朗书声，操场上的滴滴汗水，正是青春的象征鸟语花香。教室里的朗朗书声，操场上的滴滴汗水，正是青春的象征",
            "e_p": "<p>2015年的夏天，我怀揣着对大学的憧憬来到顺职。我满怀欣喜地到梁銶琚礼堂报到，正式地成为了一名顺职院的学生。</p>\r\n\r\n<p>闲下来的时光我逛逛了校园，下面来给大家看看我眼中的顺职院吧。漫步湖边，我看到了桃苑最美的模样。不知道平时的你是不是跟我一样很喜欢在桃苑吃饭呢。</p>\r\n<p>最喜欢的事情，就是在夜晚漫步湖边，走累了，随处可见的石凳可以供学生们休息。偶尔运气好的，就能遇到湖边自己拉音响唱歌的小伙伴，这个时候如果你恰好有兴趣，坐着听听歌也无妨。</p>\r\n<p>我认为学校最有特色的东西，就是这个天鹅蛋石头，每次从桥边走过看到这个天鹅蛋样的巨型石头下面一群天鹅戏耍的时候，我总会想，小天鹅们会不会以为那是自己的小伙伴下的蛋呢。</p>\r\n<img src=\"./../static/img/banner/banner2.jpg\" alt=\"\">\r\n<p>从智慧门往正门看去，有没有一种宏伟壮丽，高端大气的感觉呢。</p>\r\n<p>玫瑰园的玫瑰竞相开放，争奇斗艳，盛开的玫瑰，给了我们视觉上的享受，但是，玫瑰虽美，可不能随手摘哦，据说有很多个监控的呢，摘了可是要罚钱的哦</p>\r\n<p>环湖的全景图，蓝天白云，波光浩荡，有没有想要来看一看的冲动呢</p>\r\n<img src=\"./../static/img/banner/banner3.jpg\" alt=\"\">\r\n<p>站在四合院最高楼俯视生活区，一栋栋的宿舍楼环湖而建，晚上夜深人静的时候，搬张凳子坐在阳台，看看夜景，你会发现一个不一样的顺职。\r\n快来留言你眼中最美的顺职吧！！！\r\n</p>",
            "e_img": "http://xxiaoyuan.top/static/img/banner/banner1.jpg",
            "e_user": "512",
            "e_date": "2018-05-25 00:00:00",
            "e_is_index": "1"
          },
          {
            "u_id": "512",
            "u_name": "开发者",
            "u_img": "http://xxiaoyuan.top/static/js/upload/uploads/153923102266562.jpeg",
            "u_date": "2018-05-31 20:22:08",
            "e_id": "100",
            "e_type": "0",
            "e_title": "我眼中的顺职",
            "e_intro": "顺德职业技术学院是我美丽的大学，那里春满花开，四季常绿，四季常绿，鸟语花香。教室里的朗朗书声，操场上的滴滴汗水，正是青春的象征鸟语花香。教室里的朗朗书声，操场上的滴滴汗水，正是青春的象征",
            "e_p": "<p>2015年的夏天，我怀揣着对大学的憧憬来到顺职。我满怀欣喜地到梁銶琚礼堂报到，正式地成为了一名顺职院的学生。</p>\r\n\r\n<p>闲下来的时光我逛逛了校园，下面来给大家看看我眼中的顺职院吧。漫步湖边，我看到了桃苑最美的模样。不知道平时的你是不是跟我一样很喜欢在桃苑吃饭呢。</p>\r\n<p>最喜欢的事情，就是在夜晚漫步湖边，走累了，随处可见的石凳可以供学生们休息。偶尔运气好的，就能遇到湖边自己拉音响唱歌的小伙伴，这个时候如果你恰好有兴趣，坐着听听歌也无妨。</p>\r\n<p>我认为学校最有特色的东西，就是这个天鹅蛋石头，每次从桥边走过看到这个天鹅蛋样的巨型石头下面一群天鹅戏耍的时候，我总会想，小天鹅们会不会以为那是自己的小伙伴下的蛋呢。</p>\r\n<img src=\"./../static/img/banner/banner2.jpg\" alt=\"\">\r\n<p>从智慧门往正门看去，有没有一种宏伟壮丽，高端大气的感觉呢。</p>\r\n<p>玫瑰园的玫瑰竞相开放，争奇斗艳，盛开的玫瑰，给了我们视觉上的享受，但是，玫瑰虽美，可不能随手摘哦，据说有很多个监控的呢，摘了可是要罚钱的哦</p>\r\n<p>环湖的全景图，蓝天白云，波光浩荡，有没有想要来看一看的冲动呢</p>\r\n<img src=\"./../static/img/banner/banner3.jpg\" alt=\"\">\r\n<p>站在四合院最高楼俯视生活区，一栋栋的宿舍楼环湖而建，晚上夜深人静的时候，搬张凳子坐在阳台，看看夜景，你会发现一个不一样的顺职。\r\n快来留言你眼中最美的顺职吧！！！\r\n</p>",
            "e_img": "http://xxiaoyuan.top/static/img/banner/banner1.jpg",
            "e_user": "512",
            "e_date": "2018-05-25 00:00:00",
            "e_is_index": "1"
          }
        ]
      };
    },
    methods:{
      getList:function(){
        //imgList API返回的参数
        this.ListData = this.imgList;
      }

    },
    onLoad(){
      this.getList();
    }
  };
</script>


<!--css样式-->
<style lang="scss" scoped>
  [v-cloak]{
    display: none!important;
  }
  *{
    padding: 0;
    margin: 0;
    list-style-type: none;
  }
  body{
    width: 95%;
    margin: 10px auto;
  }
  .pubu{
    width: calc(100% - 16px);
    margin-left: 8px;
  }
  .yg img{
    width:100%;
    border-radius: 8px 8px 0 0;
  }
  .yg p{
    width: 100%;
    font-size: 0.75rem;
    padding:3px;
    overflow: hidden;
    text-overflow:ellipsis;
    white-space: nowrap;
    text-overflow: ellipsis;
    display: -webkit-box;
    -webkit-line-clamp: 3;
    -webkit-box-orient: vertical;
    overflow: hidden;
    color: #999;
  }
  .yg_l , .yg_r{
    width: calc(50% - 5px);
  }
  .yg li{
    background: #fff;
    border-radius: 8px;
    margin-bottom: 10px;
    box-shadow: 0 0 5px #999;
  }
  .yg_l{
    float: left;
  }
  .yg_r{
    float: right;
  }
  .u_xinx{
    position: absolute;
    bottom: 4px;
  }
  .u_xinx img{
    margin-left: 5px;
    background: rgba(0,0,0,.2);
    box-shadow: 0 1px 3px rgba(0,0,0,.5);
    width: 26px;
    height: 26px;
    border-radius: 50%;
    display: inline-block;
  }
  .u_xinx span{
    position: relative;
    text-shadow: 0 1px 1px #333;
    top: -8px;
    color: #fff;
    font-size: 0.6rem;
  }
</style>
