/***
 * @pName Admin
 * @name CategoryMapper
 * @user HongWei
 * @date 2019/3/9
 * @desc
 */
package com.management.admin.repository;

import com.management.admin.entity.db.Category;
import com.management.admin.entity.db.Product;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CategoryMapper extends MyMapper<Category> {
    @Select("SELECT t1.* FROM tb_categorys t1 " +
            "WHERE ${condition} ORDER BY t1.category_id DESC LIMIT #{page},${limit}")
    List<Category> selectLimit(@Param("page") Integer page, @Param("limit") String limit
            , @Param("beginTime") String beginTime
            , @Param("endTime") String endTime
            , @Param("condition") String condition);

    @Select("SELECT COUNT(t1.category_id) FROM tb_categorys t1\n" +
            "WHERE ${condition}")
    Integer selectLimitCount(@Param("isEnable") Integer isEnable
            , @Param("beginTime") String beginTime
            , @Param("endTime") String endTime
            , @Param("condition") String condition);

    @Update("UPDATE tb_categorys SET text=#{text}, parent_id=#{parentId}, image=#{image} ,class_name=#{className} WHERE category_id = #{categoryId}")
    int updateCategory(Category category);

    @Insert("INSERT INTO tb_categorys(`text`, parent_id, `image`, className) VALUES(#{text}, #{parentId}, #{image}, #{className})")
    int insertCategory(Category param);

    @Select("SELECT * FROM tb_categorys WHERE parent_id = #{categoryId}")
    List<Category> selectSubList(@Param("categoryId") Integer categoryId);

    @Select("SELECT * FROM tb_categorys ORDER BY category_id DESC")
    List<Category> selectOrderBy();
}
