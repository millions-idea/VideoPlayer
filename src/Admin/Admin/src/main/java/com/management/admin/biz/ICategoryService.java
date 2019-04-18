/***
 * @pName Admin
 * @name ICategoryService
 * @user HongWei
 * @date 2019/3/9
 * @desc
 */
package com.management.admin.biz;

import com.management.admin.entity.db.Category;
import com.management.admin.entity.param.ProductParam;

import java.util.List;

public interface ICategoryService {
    List<Category> getAllCategory();

    List<Category> getLimit(Integer page, String limit, String condition, Integer state, String beginTime, String endTime);

    Integer getCount();

    Integer getLimitCount(String condition, Integer state, String beginTime, String endTime);

    Category getCategory(Long categoryId);

    boolean editProfile(Category param);

    boolean addProfile(Category param);

    boolean deleteCategory(Category param);
}
