/***
 * @pName Admin
 * @name CategoryServiceImpl
 * @user HongWei
 * @date 2019/3/9
 * @desc
 */
package com.management.admin.biz.impl;

import com.management.admin.biz.ICategoryService;
import com.management.admin.entity.db.Category;
import com.management.admin.entity.db.Product;
import com.management.admin.entity.param.ProductParam;
import com.management.admin.exception.InfoException;
import com.management.admin.repository.CategoryMapper;
import com.management.admin.repository.utils.ConditionUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements ICategoryService {
    private final CategoryMapper categoryMapper;

    public CategoryServiceImpl(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    @Override
    public List<Category> getAllCategory() {
        return categoryMapper.selectOrderBy();
    }

    @Override
    public List<Category> getLimit(Integer page, String limit, String condition, Integer state, String beginTime, String endTime) {
        // 计算分页位置
        page = ConditionUtil.extractPageIndex(page, limit);
        String where = extractLimitWhere(condition, state, beginTime, endTime);
        List<Category> list = categoryMapper.selectLimit(page, limit, beginTime, endTime, where);
        return list;
    }

    @Override
    public Integer getCount() {
        return categoryMapper.selectCount(new Category());
    }

    @Override
    public Integer getLimitCount(String condition, Integer state, String beginTime, String endTime) {
        String where = extractLimitWhere(condition, state, beginTime, endTime);
        return categoryMapper.selectLimitCount(state, beginTime, endTime, where);
    }

    @Override
    public Category getCategory(Long categoryId) {
        return categoryMapper.selectByPrimaryKey(categoryId);
    }

    @Override
    public boolean editProfile(Category param) {
        return categoryMapper.updateCategory(param) > 0;
    }

    @Override
    public boolean addProfile(Category param) {
        return categoryMapper.insertCategory(param) > 0;
    }

    @Override
    public boolean deleteCategory(Category param) {
        List<Category> categories =  categoryMapper.selectSubList(param.getCategoryId());
        if(categories !=null && categories.size() > 0) throw new InfoException("只允许删除父级且没有跟任何子类目建立关系的类目");
        return categoryMapper.deleteByPrimaryKey(param) > 0;
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
            where += " AND (" + ConditionUtil.like("category_id", condition, true, "t1");
            if (condition.split("-").length == 2){
                where += " OR " + ConditionUtil.like("add_date", condition, true, "t1");
                where += " OR " + ConditionUtil.like("edit_date", condition, true, "t1");
            }
            where += " OR " + ConditionUtil.like("promotion_code", condition, true, "t1") + ")";
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
