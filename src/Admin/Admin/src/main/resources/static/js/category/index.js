var route = "/management/category";
var service;
var tableIndex;
(function () {
    service = initService(route);
    // 加载数据表
    initDataTable(route + "/getLimit", function (form, table, layer, vipTable, tableIns) {
        $("#add").click(function(){
            service.addCategory({}, function(html){
                if(html instanceof Object) {
                    layer.msg("获取资料失败");
                    return;
                }
                layer.open({
                    type: 1,
                    skin: 'layui-layer-rim', //加上边框
                    area: ['520px', 'auto'], //宽高
                    content:html
                });
            });
        })
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
        // 监听工具条
        table.on('tool(my-data-table)', function(obj){
            var data = obj.data; //获得当前行数据
            var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
            var tr = obj.tr; //获得当前行 tr 的DOM对象
            if(layEvent === 'edit'){
                service.edit({
                    categoryId: data.categoryId
                }, function(html){
                    if(html instanceof Object) {
                        layer.msg("获取资料失败");
                        return;
                    }
                    layer.open({
                        type: 1,
                        skin: 'layui-layer-rim', //加上边框
                        area: ['520px', 'auto'], //宽高
                        content:html
                    });
                });
            } else if(layEvent === 'delete'){
                layer.confirm("只允许删除父级且没有跟任何子类目建立关系的类目，您确定要删除吗？",{
                    btn: ['继续','取消']
                }, function(){
                    service.deleteCategory({
                        categoryId: data.categoryId
                    }, function(res){
                        if(res.code == 200){
                            layer.msg("操作成功");
                            location.reload();
                            return;
                        }else{
                            layer.msg("操作失败");
                            return;
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
        edit: function (param, callback) {
            $.get(r + "/edit", param, function (data) {
                callback(data);
            });
        },
        addCategory: function(param, callback){
            $.get(r + "/add", param, function (data) {
                callback(data);
            });
        },
        deleteCategory: function(param, callback){
            $.post(r + "/deleteCategory", param, function (data) {
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
        , {field: 'categoryId', title: 'ID', width: 80, sort: true}
        , {field: 'image', title: 'ICON图标', width: 150, templet: function(d){
                var part = 'data-id="' + d.categoryId + '" data-categoryName="' + d.categoryName + '"';
                return '<img ' + part + '  width="27px" class="face" src="' + d.image + '" />';
            }}
        , {field: 'text', title: '名称', width: 150}
        , {field: 'parentId', title: '父类目', width: 150}
        , {fixed: 'right',title: '操作', width: 240 , align: 'center', templet: function(d){
                var html = "";
                html += '<a name="item-edit" class="layui-btn layui-btn layui-btn-xs" lay-event="edit">编辑</a>';
                html += '<a name="item-edit"  class="layui-btn layui-btn layui-btn-sm layui-btn-danger" lay-event="delete">删除</a>';
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

