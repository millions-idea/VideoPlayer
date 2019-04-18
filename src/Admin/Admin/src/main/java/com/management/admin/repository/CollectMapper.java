/***
 * @pName Admin
 * @name CollectMapper
 * @user HongWei
 * @date 2019/3/22
 * @desc
 */
package com.management.admin.repository;

import com.management.admin.entity.db.Collect;
import com.management.admin.entity.resp.CollectView;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CollectMapper extends MyMapper<Collect> {
    @Select("SELECT *, t1.add_date AS collectDate FROM tb_collects t1 " +
            "LEFT JOIN tb_products t2 ON t2.product_id = t1.product_id " +
            " WHERE t1.user_id = #{userId}  ORDER BY t1.add_date DESC LIMIT ${page},${size}")
    List<CollectView> selectLimitByUid(@Param("userId") Long userId, @Param("page") Integer page,@Param("size") String size);

    @Select("SELECT COUNT(*) FROM tb_collects WHERE user_id = #{userId}")
    int selectTotalPageByUid(@Param("userId") Long userId);


    @Delete("DELETE FROM tb_collects WHERE product_id = #{productId} AND user_id = #{userId}")
    int deleteByUser(@Param("productId") Long productId,@Param("userId") Long userId);

    @Select("SELECT * FROM tb_collects WHERE product_id = #{productId} AND user_id = #{userId} LIMIT 1")
    Collect selectByUserProduct(@Param("productId") Long productId, @Param("userId") Long userId);
}
