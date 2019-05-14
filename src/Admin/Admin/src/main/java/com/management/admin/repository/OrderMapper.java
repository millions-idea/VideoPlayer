/***
 * @pName Admin
 * @name OrderMapper
 * @user HongWei
 * @date 2019/3/9
 * @desc
 */
package com.management.admin.repository;

import com.alipay.api.domain.OrderDetail;
import com.management.admin.entity.db.Order;
import com.management.admin.entity.resp.OrderView;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OrderMapper extends MyMapper<Order> {

    @Insert("INSERT INTO `tb_orders` (`order_id`, `product_id`, `product_name`, `product_image`, `amount`, `add_date`, `status`, `user_id`, `channel`) " +
            "VALUES (#{orderId}, #{productId}, (SELECT product_name FROM tb_products WHERE product_id = #{productId} LIMIT 1), (SELECT image FROM tb_products WHERE product_id = #{productId} LIMIT 1)" +
            ", (SELECT amount FROM tb_products WHERE product_id = #{productId} LIMIT 1),  NOW(), 0, #{userId}, #{channel});")
    int insertOrder(Order order);

    @Insert("INSERT INTO `tb_orders` (`order_id`, `product_id`, `product_name`, `product_image`, `amount`, `add_date`, `status`, `user_id`, `channel`) " +
            "VALUES (#{orderId}, #{productId}, (SELECT product_name FROM tb_products WHERE product_id = #{productId} LIMIT 1), (SELECT image FROM tb_products WHERE product_id = #{productId} LIMIT 1)" +
            ", #{amount},  NOW(), 0, #{userId}, #{channel});")
    int insertOrderAndAmount(Order order);

    @Update("UPDATE tb_orders SET channel=#{channel},status=1,edit_date=NOW() WHERE order_id=#{orderId}")
    boolean updateById(@Param("orderId") Long orderId, @Param("channel") String channel);

    @Update("UPDATE tb_orders SET edit_date=NOW(),status=2 WHERE order_id=#{orderId} ")
    int deliver(@Param("orderId") Long orderId);

    @Select("SELECT *,t2.image AS productImage  FROM tb_orders t1 " +
            "LEFT JOIN tb_products t2 ON t1.product_id = t2.product_id " +
            "LEFT JOIN tb_cdkeys t3 ON t1.cdkey_id = t3.key_id " +
            "WHERE t1.user_id = #{userId} ORDER BY t1.add_date DESC LIMIT ${page},${size}")
    List<OrderView> selectOrderLimit(@Param("userId") Long userId, @Param("page") Integer page, @Param("size") String size);

    @Select("SELECT * FROM tb_orders WHERE user_id = #{userId} AND (status = 1 OR status = 2)")
    List<Order> selectOrders(@Param("userId") Long userId);

    @Update("UPDATE tb_order SET status = 1, edit_date=NOW() WHERE order_id =#{orderId} AND user_id = #{userId}")
    int notifyOrder(@Param("orderId") Long orderId, @Param("userId") Long userId);

    @Select("SELECT *,t2.image AS productImage  FROM tb_orders t1 " +
            "LEFT JOIN tb_products t2 ON t1.product_id = t2.product_id " +
            "WHERE t1.user_id = #{userId} AND t1.status = 2 ORDER BY t1.add_date DESC LIMIT ${page},${size}")
    List<OrderView> selectLimitByUid(@Param("userId") Long userId,@Param("page") Integer page,@Param("size") String size);

    @Select("SELECT COUNT(*) FROM tb_orders t1 " +
            "WHERE t1.user_id = #{userId}")
    int selectTotalPageByUid(@Param("userId") Long userId);

    @Select("SELECT * FROM tb_orders WHERE product_id = #{productId} AND user_id = #{userId} ORDER BY add_date DESC LIMIT 1")
    Order selectOrderByUserProduct(@Param("productId") String productId, @Param("userId") Long userId);

    @Select("SELECT COUNT(t1.order_id) FROM tb_orders t1 " +
            "LEFT JOIN tb_products t2 ON t2.product_id = t1.product_id  " +
            "LEFT JOIN tb_categorys t3 ON t3.category_id = t2.category_id  " +
            "LEFT JOIN tb_users t4 ON t4.user_id = t1.user_id  " +
            "WHERE ${condition} ")
    Integer selectLimitCount(@Param("isEnable") Integer isEnable
            , @Param("beginTime") String beginTime
            , @Param("endTime") String endTime
            , @Param("condition") String condition);

    @Select("SELECT *,t2.image AS productImage, t3.text AS categoryName FROM tb_orders t1 " +
            "LEFT JOIN tb_products t2 ON t2.product_id = t1.product_id  " +
            "LEFT JOIN tb_categorys t3 ON t3.category_id = t2.category_id  " +
            "LEFT JOIN tb_users t4 ON t4.user_id = t1.user_id  " +
            "WHERE ${condition} ORDER BY t1.add_date DESC LIMIT #{page},${limit}")
    List<OrderView> selectLimit(@Param("page") Integer page, @Param("limit") String limit
            , @Param("beginTime") String beginTime
            , @Param("endTime") String endTime
            , @Param("condition") String condition);

    @Select("SELECT *,t2.image AS productImage, t3.text AS categoryName FROM tb_orders t1 " +
            "LEFT JOIN tb_products t2 ON t2.product_id = t1.product_id  " +
            "LEFT JOIN tb_categorys t3 ON t3.category_id = t2.category_id  " +
            "LEFT JOIN tb_users t4 ON t4.user_id = t1.user_id  " +
            "WHERE t1.user_id = #{userId} ORDER BY t1.add_date DESC")
    List<OrderView> selectOrderViews(@Param("userId") Long userId);
}
