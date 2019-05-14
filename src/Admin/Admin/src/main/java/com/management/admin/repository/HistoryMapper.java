/***
 * @pName Admin
 * @name HistoryMapper
 * @user HongWei
 * @date 2019/3/27
 * @desc
 */
package com.management.admin.repository;

import com.management.admin.entity.db.Collect;
import com.management.admin.entity.db.History;
import com.management.admin.entity.db.Product;
import com.management.admin.entity.resp.ProductDetail;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface HistoryMapper extends MyMapper<History> {
    @Insert("INSERT INTO tb_historys(product_id, product_name, amount, image, category_id, add_date, user_id, show_count) " +
            "VALUES(#{productId}, #{productName}, #{amount}, #{image}, #{categoryId}, NOW(), #{userId}, #{showCount})")
    int insertHistory(ProductDetail product);

    @Select("SELECT * FROM tb_historys WHERE user_id = #{userId} AND product_id = #{productId} ORDER BY add_date DESC LIMIT 1")
    History selectByUserProduct(@Param("userId") Long finalUserId,@Param("productId") Long productId);
}
