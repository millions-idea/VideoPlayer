/***
 * @pName Admin
 * @name ProductMapper
 * @user HongWei
 * @date 2019/3/9
 * @desc
 */
package com.management.admin.repository;

import com.management.admin.entity.db.Product;
import com.management.admin.entity.param.ProductParam;
import com.management.admin.entity.resp.ProductDetail;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ProductMapper extends MyMapper<Product> {
    @Select("SELECT * FROM tb_products WHERE category_id = #{categoryId}  AND is_show = 0 AND status = 1 ORDER BY edit_date DESC, add_date DESC LIMIT ${page},${size} ")
    List<Product> selectProductLimit(@Param("categoryId") Integer categoryId, @Param("page") Integer page, @Param("size") String size);

    @Select("SELECT * FROM tb_products WHERE category_id = #{categoryId} AND is_show = 0")
    List<Product> selectProduct(@Param("categoryId") Integer categoryId);

    @Select("SELECT * FROM tb_products WHERE product_id = #{productId} AND status = 1")
    Product selectById(@Param("productId") Long productId);

    @Update("UPDATE tb_products SET sale_count = sale_count + 1 WHERE product_id = #{productId}")
    int addSaleNum(@Param("productId") Long productId);

    @Update("UPDATE tb_products SET poster = #{url} WHERE product_id =#{productId}")
    int changePoster(@Param("productId") Long productId, @Param("url") String url);


    @Update("UPDATE tb_products SET status = #{state} WHERE product_id = #{productId}")
    int updateSale(@Param("productId") String productId, @Param("state") int state);

    @Update("UPDATE tb_products SET product_name = #{productName} " +
            ", amount = #{amount} " +
            ", image = #{image} " +
            ", category_id = #{categoryId} " +
            ", edit_date = NOW() " +
            ", status = 0 " +
            ", show_count = #{showCount} " +
            ", video_url = #{videoUrl} " +
            ", top = #{top} " +
            " WHERE product_id = #{productId} ")
    boolean updateProduct(ProductParam param);

    @Select("SELECT *,t2.text AS categoryName FROM tb_products t1 " +
            "LEFT JOIN tb_categorys t2 ON t2.category_id = t1.category_id  " +
            "WHERE ${condition} AND is_show = 0 ORDER BY t1.add_date DESC LIMIT #{page},${limit}")
    List<ProductDetail> selectLimit(@Param("page") Integer page, @Param("limit") String limit
            , @Param("beginTime") String beginTime
            , @Param("endTime") String endTime
            , @Param("condition") String condition);

    @Select("SELECT COUNT(t1.product_id) FROM tb_products t1\n" +
            "WHERE ${condition}  AND is_show = 0 ")
    Integer selectLimitCount(@Param("isEnable") Integer isEnable
            , @Param("beginTime") String beginTime
            , @Param("endTime") String endTime
            , @Param("condition") String condition);

    @Insert("INSERT INTO tb_products(product_id, product_name, amount, image, category_id, add_date, show_count, video_url) " +
            "VALUES(#{productId}, #{productName}, #{amount}, #{image}, #{categoryId}, NOW(), #{showCount}, #{videoUrl})")
    int insertProduct(Product product);

    @Select("SELECT * FROM tb_products WHERE product_id = #{productId}")
    Product selectSaleProduct(@Param("productId") Long productId);

    @Delete("DELETE FROM tb_products WHERE product_id = #{productId}")
    int deleteProduct(@Param("productId") Long productId);

    @Select("SELECT * FROM tb_products WHERE (YEARWEEK( date_format(add_date,'%Y-%m-%d')) = YEARWEEK(NOW()) OR top=1)  AND is_show = 0 AND status = 1 ORDER BY edit_date DESC, add_date DESC LIMIT ${page},${size} ")
    List<Product> selectNewProductLimit(@Param("page") Integer page, @Param("size") String size);

    @Select("SELECT * FROM tb_products WHERE (YEARWEEK( date_format(add_date,'%Y-%m-%d')) = YEARWEEK( NOW() ) OR top=1)  AND `status` = 1 AND is_show = 0")
    List<Product> getNewProductTotalPage();

    @Select("SELECT * FROM tb_products WHERE category_id = #{categoryId} AND is_show = 0 AND status = 1 ORDER BY edit_date DESC, add_date DESC LIMIT ${page},${size} ")
    List<Product> getCategoryProductLimit(@Param("page") Integer page,@Param("size") String size,@Param("categoryId") Integer categoryId);

    @Select("SELECT * FROM tb_historys WHERE user_id = #{userId}  ORDER BY add_date DESC LIMIT ${page},${size} ")
    List<Product> getPlayHistoryLimit(@Param("userId") Long userId, @Param("page") Integer page,@Param("size") String size);

    @Select("SELECT COUNT(*) FROM tb_historys WHERE user_id = #{userId}")
    int getPlayHistoryTotalPage(@Param("userId") Long userId);

    @Select("SELECT * FROM tb_products WHERE (YEAR(add_date)=YEAR(NOW()) OR top=1)  AND is_show = 0 AND status = 1 ORDER BY edit_date DESC, add_date DESC LIMIT ${page},${size} ")
    List<Product> selectNewProductLimitWithYear(@Param("page") Integer page,@Param("size") String size);

    @Select("SELECT * FROM tb_products WHERE (YEAR(add_date)=YEAR(NOW()) OR top=1)  AND `status` = 1 AND is_show = 0")
    List<Product> getNewProductTotalPageWithYear();

    @Select("SELECT *,t2.text AS categoryName FROM tb_products t1 " +
            "LEFT JOIN tb_categorys t2 ON t1.category_id = t2.category_id " +
            "WHERE t1.product_id = #{productId} LIMIT 1")
    ProductDetail selectProductDetail(@Param("productId") Long productId);

    @Select("SELECT * FROM tb_products WHERE category_id = #{categoryId} LIMIT 5")
    List<ProductDetail> selectListByCategoryId(@Param("categoryId") Integer categoryId);

    @Update("UPDATE tb_products SET show_count = show_count + 1 WHERE product_id = #{productId}")
    int addShowCount(@Param("productId") Long productId);
}
