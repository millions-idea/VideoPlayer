layui.use(['form'], function(){
    var form = layui.form;
    form.render();
});

$(function () {
    initDataTable("./getUserOrders", function (form, table, layer, vipTable, tableIns) {

    }, function (table, res, curr, count) {

    });
})

/**
 * 加载数据表
 * @param url
 * @param callback
 * @param loadDone
 */
function initDataTable(url, callback, loadDone) {
    var $queryButton = $("#children-data-table-query"),
        $queryCondition = $("#children-data-table-condition"),
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

        loadTable(tableIndex,"children-data-table", "#children-data-table", cols, url + param, function (res, curr, count) {
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
            elem: '#children-data-table'                  //指定原始表格元素选择器（推荐id选择器）
            , height: 320    //容器高度
            , cols: cols
            , id: 'children-data-table'
            , url: url + "?userId=" + $("#userId").val()
            , method: 'get'
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
        , {field: 'orderId', title: 'ID', width: 80, sort: true}
        , {field: 'account', title: '用户名', width: 120, sort: true}
        , {field: 'productId', title: '商品ID', width: 120, sort: true}
        , {field: 'productImage', title: '预览图', width: 150, templet: function(d){
                var part = 'data-id="' + d.productId + '" data-productName="' + d.productName + '"';
                return '<img ' + part + '  width="27px" class="face" src="' + d.productImage + '" />';
            }}
        , {field: 'productName', title: '商品名称', width: 150}
        , {field: 'amount', title: '价格', width: 150}
        , {field: 'categoryName', title: '视频分类', width: 150}
        , {field: 'addDate', title: '创建时间', width: 180, sort: true, templet: function (d) {
                return d.addDate == null ? '' : utils.date.timestampConvert(d.addDate);
            }}
        , {field: 'editDate', title: '更新时间', width: 180, sort: true, templet: function (d) {
                return d.editDate == null ? '' : utils.date.timestampConvert(d.editDate);
            }}
        , {field: 'statusText', title: '状态', width: 80}
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
        , height: 320    //容器高度
        , cols: cols
        , id: id
        , url: url + "?userId=" + $("#userId").val()
        , method: 'get'
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
        $(o).height(320);
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