/***
 * @pName Admin
 * @name CategoryApiController
 * @user HongWei
 * @date 2019/3/20
 * @desc
 */
package com.management.admin.apiController;

import com.management.admin.biz.ICategoryService;
import com.management.admin.entity.db.Category;
import com.management.admin.entity.template.JsonArrayResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryApiController {
    @Autowired
    private ICategoryService categoryService;

    @GetMapping("/getAllCategory")
    public JsonArrayResult<Category> getAllCategory(){
        List<Category> categorys = categoryService.getAllCategory();
        return new JsonArrayResult<>(categorys);
    }
}
