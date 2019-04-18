/***
 * @pName Admin
 * @name OrderController
 * @user HongWei
 * @date 2019/4/7
 * @desc
 */
package com.management.admin.controller;

import com.management.admin.biz.IOrderService;
import com.management.admin.entity.db.Order;
import com.management.admin.entity.resp.OrderView;
import com.management.admin.entity.resp.ProductDetail;
import com.management.admin.entity.resp.ProductView;
import com.management.admin.entity.template.JsonArrayResult;
import com.management.admin.utils.PropertyUtil;
import com.management.admin.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/management/order")
public class OrderController {

    @Autowired
    private IOrderService orderService;

    @GetMapping("/index")
    public String index(){
        return "order/index";
    }

    @GetMapping("/getLimit")
    @ResponseBody
    public JsonArrayResult<OrderView> getLimit(Integer page, String limit, String condition, Integer type, Integer state, String beginTime, String endTime) {
        Integer count = 0;
        List<OrderView> list = orderService.getLimit(page, limit, condition, state, beginTime, endTime);
        list.forEach(item -> {
            item.setProductId(item.getProductId() + "");
            switch (item.getStatus()){
                case 0:
                    item.setStatusText("未上架");
                    break;
                case 1:
                    item.setStatusText("已上架");
                    break;
                case 2:
                    item.setStatusText("已下架");
                    break;
            }
        });
        JsonArrayResult jsonArrayResult = new JsonArrayResult(0, list);
        if (StringUtil.isBlank(condition)
                && StringUtil.isBlank(beginTime)
                && StringUtil.isBlank(endTime)
                && (state == null || state == 0)) {
            count = orderService.getCount();
        } else {
            count = orderService.getLimitCount(condition, state, beginTime, endTime);
        }
        jsonArrayResult.setCount(count);
        return jsonArrayResult;
    }

}
