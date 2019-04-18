/***
 * @pName Admin
 * @name OrderServiceImpl
 * @user HongWei
 * @date 2019/3/9
 * @desc
 */
package com.management.admin.biz.impl;

import com.alipay.api.domain.OrderDetail;
import com.management.admin.biz.IOrderService;
import com.management.admin.entity.db.Order;
import com.management.admin.entity.db.Product;
import com.management.admin.entity.resp.OrderView;
import com.management.admin.entity.resp.ProductDetail;
import com.management.admin.entity.template.DataDictionary;
import com.management.admin.exception.InfoException;
import com.management.admin.repository.OrderMapper;
import com.management.admin.repository.ProductMapper;
import com.management.admin.repository.utils.ConditionUtil;
import com.management.admin.utils.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderServiceImpl implements IOrderService {
    private final OrderMapper orderMapper;
    private final ProductMapper productMapper;

    @Autowired
    public OrderServiceImpl(OrderMapper orderMapper, ProductMapper productMapper) {
        this.orderMapper = orderMapper;
        this.productMapper = productMapper;
    }

    /**
     * 创建订单
     *
     * @param userId
     * @param productId
     * @param channel
     * @return
     */
    @Override
    public Long createOrder(Long userId, Long productId, String channel, Double amount) {
        Order order = new Order();
        try {
            order.setOrderId(IdWorker.getFlowIdWorkerInstance().nextId());
            order.setProductId(productId);
            order.setUserId(userId);
            order.setChannel(channel);

            Double permanent = Double.valueOf(DataDictionary.Map.get("product.price.permanent").getValue());

            if(amount.equals(permanent)){
                order.setAmount(permanent);
                int count = orderMapper.insertOrderAndAmount(order);
                if(count > 0 ) return order.getOrderId();
            }

            int count = orderMapper.insertOrder(order);
            if(count > 0 ) return order.getOrderId();
        } catch (Exception e) {

        }
        return null;
    }

    @Override
    public Order getOrder(Long orderId) {
        return orderMapper.selectByPrimaryKey(orderId);
    }

    @Override
    public boolean finishOrder(Long orderId, String channel) {
        return orderMapper.updateById(orderId, channel);
    }

    @Override
    @Transactional
    public void deliver(Long orderId) {
        Order order = orderMapper.selectByPrimaryKey(orderId);

        if(order == null) throw new InfoException("订单不存在");

        int count = orderMapper.deliver(orderId);

        if(count == 0) throw new InfoException("发货失败");
    }

    @Override
    public List<OrderView> getOrderLimit(Long userId, Integer page) {
        String size =  "10";
        if(page == null) page = 0;
        List<OrderView> list = orderMapper.selectOrderLimit(userId, page, size);
        return list;
    }

    @Override
    public Integer getOrderTotalPage(Long userId) {
        List<Order> list = orderMapper.selectOrders(userId);
        int size = list.size() / 10;
        return  size == 0 ? 1000 : size;
    }

    @Override
    public void notifyOrder(Long orderId, Long userId) {
        int count = orderMapper.notifyOrder(orderId, userId);
        if(count <= 0) throw new InfoException("刷新订单状态失败");
    }

    @Override
    public List<Order> getOrderList(Long userId) {
        return orderMapper.selectOrders(userId);
    }

    @Override
    public List<OrderView> getLimit(Long userId, Integer page) {
        String size =  "10";
        if(page == null) page = 0;
        List<OrderView> list = orderMapper.selectLimitByUid(userId, page, size);
        return list;
    }

    @Override
    public Integer getTotalPage(Long userId) {
        int count = orderMapper.selectTotalPageByUid(userId);
        Integer size = count / 10;
        if(count < 10) size = 1;
        return size < 0 ? 1000 : size;
    }

    @Override
    public List<OrderView> getLimit(Integer page, String limit, String condition, Integer state, String beginTime, String endTime) {
        // 计算分页位置
        page = ConditionUtil.extractPageIndex(page, limit);
        String where = extractLimitWhere(condition, state, beginTime, endTime);
        List<OrderView> list = orderMapper.selectLimit(page, limit, beginTime, endTime, where);
        return list;
    }

    @Override
    public Integer getLimitCount(String condition, Integer state, String beginTime, String endTime) {
        String where = extractLimitWhere(condition, state, beginTime, endTime);
        return orderMapper.selectLimitCount(state, beginTime, endTime, where);
    }

    @Override
    public Integer getCount() {
        return orderMapper.selectCount(new Order());

    }

    /**
     * 提取分页条件
     * @return
     */
    private String extractLimitWhere(String condition, Integer state, String beginTime, String endTime) {
        // 查询模糊条件
        String where = " 1=1";
        if(condition != null) {
            condition = condition.trim();
            where += " AND (" + ConditionUtil.like("order_id", condition, true, "t1");
            if (condition.split("-").length == 2){
                where += " OR " + ConditionUtil.like("add_date", condition, true, "t1");
                where += " OR " + ConditionUtil.like("edit_date", condition, true, "t1");
            }
            where += " OR " + ConditionUtil.like("account", condition, true, "t4") + ")";
        }

        // 取两个日期之间或查询指定日期
        where = extractBetweenTime(beginTime, state, endTime, where);
        return where.trim();
    }

    /**
     * 提取两个日期之间的条件
     * @return
     */
    private String extractBetweenTime(String beginTime,Integer state, String endTime, String where) {
        if ((beginTime != null && beginTime.contains("-")) &&
                endTime != null && endTime.contains("-")){
            where += " AND t1.add_date BETWEEN #{beginTime} AND #{endTime}";
        }else if (beginTime != null && beginTime.contains("-")){
            where += " AND t1.add_date BETWEEN #{beginTime} AND #{endTime}";
        }else if (endTime != null && endTime.contains("-")){
            where += " AND t1.add_date BETWEEN #{beginTime} AND #{endTime}";
        }
        return where;
    }

}
