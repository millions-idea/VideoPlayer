/*!用户管理-用户列表  2018年8月27日01:05:05*/
var route = "/management/agent";
var service;
var tableIndex;
(function () {
    service = initService(route);
    // 加载数据表
    initDataTable(route + "/getLimit", function (form, table, layer, vipTable, tableIns) {

    }, function (table, res, curr, count) {
        //预览图片
        $(".face").click(function () {
            var photo = {
                title: $(this).attr("data-nick"),
                id: $(this).attr("data-id"),
                start: 0,
                data: [{
                    alt: $(this).attr("data-nick"),
                    pid: $(this).attr("data-id"),
                    src: $(this).attr("src"),
                    thumb: $(this).attr("src")
                }]
            };
            layer.photos({
                photos: photo
                ,anim: 5 //0-6的选择，指定弹出图片动画类型，默认随机（请注意，3.0之前的版本用shift参数）
            });
        })
        //导出EXEC
        $("#exec").click(function () {
            var arrs=new Array();
            $(function () {
                $.each(layui.table.cache["my-data-table"], function (index, item) {
                    var array=new Array();
                    var name =item.nickName;
                    array[0]=item.userId;
                    array[1]=name;
                    if (array[1]==""||array[1]==null){
                        array[1]="无"
                    }
                    array[2]=item.phone;
                    array[3]=item.ip;
                    if (array[3]==""||array[3]==null){
                        array[3]="无"
                    }
                    array[4]=changeDate(item.addDate);
                    if (array[4]=="NaN-NaN-NaN NaN:NaN:NaN"){
                        array[4]="无"
                    }
                    array[5]=changeDate(item.editDate);
                    if (array[5]=="NaN-NaN-NaN NaN:NaN:NaN"){
                        array[5]="无"
                    }

                    arrs[index]=array;
                });
            });
           layui.table.exportFile(['ID','昵称','手机号','IP地址','注册时间','更新时间'],arrs
            , 'xls'); //默认导出 csv，也可以为：xls

        })
        // 监听工具条
        table.on('tool(my-data-table)', function(obj){
            var data = obj.data; //获得当前行数据
            var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
            var tr = obj.tr; //获得当前行 tr 的DOM对象
            if(layEvent === 'edit'){ //重置密码
                service.edit({
                    userId: data.userId
                }, function(html){
                    if(html instanceof Object) {
                        layer.msg("获取用户资料失败");
                        return;
                    }
                    layer.open({
                        type: 1,
                        skin: 'layui-layer-rim', //加上边框
                        area: ['520px', 'auto'], //宽高
                        content:html
                    });
                });
            } else if(layEvent === 'blocking'){ //加入黑名单
                layer.confirm("封禁后用户无法再在任何终端登录",{
                    btn: ['继续','取消']
                }, function(){
                    service.blocking({
                        userId: data.userId
                    }, function(res){
                        if(res.code == 200){
                            layer.msg("操作成功");
                            location.reload();
                        }else{
                            layer.msg("操作失败");
                        }
                    });
                })

            }else if(layEvent === 'unBlocking'){ //解封
                layer.confirm("解封后用户可以正常登陆使用APP",{
                    btn: ['继续','取消']
                }, function(){
                    service.unBlocking({
                        userId: data.userId
                    }, function(res){
                        if(res.code == 200){
                            layer.msg("操作成功");
                            location.reload();
                        }else{
                            layer.msg("操作失败");
                        }
                    });
                })

            }


        });
    });
})()

/**
 * 加载模块
 * @param r
 * @returns
 */
function initService(r) {
    return {
        /**
         * 编辑用户 DF 2018年12月16日19:22:24
         * @param param
         * @param callback
         */
        edit: function (param, callback) {
            $.get(r + "/edit", param, function (data) {
                callback(data);
            });
        }
        ,blocking: function (param, callback) {
            $.post(r + "/blocking", param, function (data) {
                callback(data);
            });
        }
        ,unBlocking: function (param, callback) {
            $.post(r + "/unBlocking", param, function (data) {
                callback(data);
            });
        }
    }
}

/**
 * 加载数据表
 * @param url
 * @param callback
 * @param loadDone
 */
function initDataTable(url, callback, loadDone) {
    var $queryButton = $("#my-data-table-query"),
        $queryCondition = $("#my-data-table-condition"),
        $tradeTypeInput = $("select[name='trade_type']"),
        $tradeDateBeginInput = $("input[name='trade_date_begin']"),
        $tradeDateEndInput = $("input[name='trade_date_end']");

    var cols = getTableColumns();

    // 注册查询事件
    $queryButton.click(function () {
        $queryButton.attr("disabled",true);
        var condition = $queryCondition.val();
        if(condition.indexOf("+") != -1) condition = condition.replace("+", "[add]");
        if(condition.indexOf("-") != -1) condition = condition.replace("-", "[reduce]");
        var param =  "?condition=" + encodeURI(condition);
        /*param += "&state=" + $tradeTypeInput.val();*/
        param += "&beginTime=" + $tradeDateBeginInput.val();
        param += "&endTime=" + $tradeDateEndInput.val();

        loadTable(tableIndex,"my-data-table", "#my-data-table", cols, url + param, function (res, curr, count) {
            $queryButton.removeAttr("disabled");
        });
    })

    layui.use(['table', 'form', 'layer', 'vip_table', 'layedit', 'tree','element'], function () {
        // 操作对象
        var form = layui.form
            , table = layui.table
            , layer = layui.layer
            , vipTable = layui.vip_table
            , $ = layui.jquery
            , layedit = layui.layedit
            , element = layui.element;

        // 表格渲染
        tableIndex = table.render({
            elem: '#my-data-table'                  //指定原始表格元素选择器（推荐id选择器）
            , height: 720    //容器高度
            , cols: cols
            , id: 'my-data-table'
            , url: url
            , method: 'get'
            , page: true
            , limits: [30, 60, 90, 150, 300]
            , limit: 30 //默认采用30
            , loading: true
            , even: true
            , done: function (res, curr, count) {
                loadDone(table, res, curr, count);
            }
        });

        // 刷新
        $('#btn-refresh-my-data-table').on('click', function () {
            tableIndex.reload();
        });

        // you code ...
        callback(form, table, layer, vipTable, tableIndex);
    });
}

/**
 * 获取表格列属性
 * @returns {*[]}
 */
function getTableColumns() {
    return [[
        {type: "numbers", fixed: 'left'}
        , {field: 'userId', title: 'ID', width: 80, sort: true}
        , {field: 'photo', title: '头像', width: 150, templet: function(d){
                var part = 'data-id="' + d.userId + '" data-nick="' + d.nickName + '"';
                return '<img ' + part + '  width="27px" class="face" src="' + d.photo + '" />';
            }}
        , {field: 'nickName', title: '昵称', width: 150}
        , {field: 'phone', title: '手机号', width: 150}
        , {field: 'level', title: '代理类型', width: 150, sort: true, templet: function (d) {
                var levelText =  "-";
                switch (d.level){
                    case 0:
                        levelText = "普通用户";
                        break;
                    case 1:
                        levelText = "VIP用户";
                        break;
                    case 2:
                        levelText = "初级代理";
                        break;
                    case 3:
                        levelText = "区域代理";
                        break;
                    case 4:
                        levelText = "高级代理";
                        break;
                    case 5:
                        levelText = "事业股东";
                        break;
                }
                return levelText;
            }}
        , {field: 'balance', title: '账户佣金', width: 150}
        , {field: 'childrenCount', title: '绑定用户数', width: 150}
        , {field: 'promotionCode', title: '推广码', width: 150}
        , {field: 'addDate', title: '注册时间', width: 150, sort: true, templet: function (d) {
                return d.addDate == null ? '' : utils.date.timestampConvert(d.addDate);
            }}
        , {field: 'editDate', title: '更新时间', width: 150, sort: true, templet: function (d) {
                return d.editDate == null ? '' : utils.date.timestampConvert(d.editDate);
            }}
        , {field: 'isDelete', title: '状态', width: 180, sort: true, templet: function (d) {
                if(d.isDelete != null && d.isDelete == 1) return "冻结";
                return "正常";
            }}
        , {fixed: 'right',title: '操作', width: 220, align: 'center', templet: function(d){
                var html = "";
                html += '<a name="item-edit" class="layui-btn layui-btn layui-btn-xs" lay-event="edit">编辑</a>';
                if(d.isDelete != null && d.isDelete == 1){
                    html += '<a name="item-edit"  class="layui-btn layui-btn-primary layui-btn-xs" lay-event="unBlocking">解封</a>';
                }else{
                    html += '<a name="item-edit"  class="layui-btn layui-btn layui-btn-sm layui-btn-danger" lay-event="blocking">封禁</a>';
                }
                return html;
            }}
    ]];
}

/**
 * 加载表格数据
 * @param tableIns
 * @param id
 * @param elem
 * @param cols
 * @param url
 * @param loadDone
 */
function loadTable(index,id,elem,cols,url,loadDone) {
    index.reload({
        elem: elem
        , height: 720    //容器高度
        , cols: cols
        , id: id
        , url: url
        , method: 'get'
        , page: true
        , limits: [30, 60, 90, 150, 300]
        , limit: 30 //默认采用30
        , loading: true
        , even: true
        , done: function (res, curr, count) {
            resetPager();
            loadDone(res, curr, count);
        }
    });
}

function resetPager() {
    $(".layui-table-body.layui-table-main").each(function (i, o) {
        $(o).height(640);
    });
}

/**
 * 将毫秒级时间戳转换为标准时间显示
 * @param {Object} value
 */
function changeDate(value){
    var date = new Date();
    date.setTime(value);
    var y = date.getFullYear();
    var m = date.getMonth() + 1;
    m = m < 10 ? ('0' + m) : m;
    var d = date.getDate();
    d = d < 10 ? ('0' + d) : d;
    var h = date.getHours();
    h = h < 10 ? ('0' + h) : h;
    var minute = date.getMinutes();
    var second = date.getSeconds();
    minute = minute < 10 ? ('0' + minute) : minute;
    second = second < 10 ? ('0' + second) : second;
    return y + '-' + m + '-' + d+' '+h+':'+minute+':'+second;
}

