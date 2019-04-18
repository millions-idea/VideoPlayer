/***
 * @pName Admin
 * @name IOrderService
 * @user HongWei
 * @date 2019/3/9
 * @desc
 */
package com.management.admin.biz;

import com.management.admin.entity.db.Order;
import com.management.admin.entity.resp.OrderView;
import com.management.admin.entity.resp.ProductDetail;

import java.util.List;

public interface IOrderService {
    /**
     * 创建订单
     * @return
     */
    Long createOrder(Long userId, Long productId, String channel, Double aDouble);

    Order getOrder(Long orderId);

    boolean finishOrder(Long orderId, String channel);

    void deliver(Long orderId);

    List<OrderView> getOrderLimit(Long userId, Integer page);

    Integer getOrderTotalPage(Long userId);

    void notifyOrder(Long orderId, Long userId);

    List<Order> getOrderList(Long userId);

    List<OrderView> getLimit(Long userId, Integer page);

    Integer getTotalPage(Long userId);

    List<OrderView> getLimit(Integer page, String limit, String condition, Integer state, String beginTime, String endTime);

    Integer getCount();

    Integer getLimitCount(String condition, Integer state, String beginTime, String endTime);
}
