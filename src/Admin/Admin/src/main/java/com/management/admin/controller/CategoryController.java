/***
 * @pName Admin
 * @name UserController
 * @user HongWei
 * @date 2018/12/16
 * @desc
 */
package com.management.admin.controller;

import com.management.admin.biz.ICategoryService;
import com.management.admin.entity.db.Category;
import com.management.admin.entity.template.JsonArrayResult;
import com.management.admin.entity.template.JsonResult;
import com.management.admin.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/management/category")
public class CategoryController {
    @Autowired
    private ICategoryService categoryService;

    @GetMapping("/index")
    public String index() {
        return "category/index";
    }

    @GetMapping("/getLimit")
    @ResponseBody
    public JsonArrayResult<Category> getLimit(Integer page, String limit, String condition, Integer type, Integer state, String beginTime, String endTime) {
        Integer count = 0;
        List<Category> list = categoryService.getLimit(page, limit, condition, state, beginTime, endTime);
        JsonArrayResult jsonArrayResult = new JsonArrayResult(0, list);
        if (StringUtil.isBlank(condition)
                && StringUtil.isBlank(beginTime)
                && StringUtil.isBlank(endTime)
                && (state == null || state == 0)) {
            count = categoryService.getCount();
        } else {
            count = categoryService.getLimitCount(condition, state, beginTime, endTime);
        }
        jsonArrayResult.setCount(count);
        return jsonArrayResult;
    }

    @GetMapping("edit")
    public String edit(String categoryId, final Model model) {
        Category category = categoryService.getCategory(Long.valueOf(categoryId));
        List<Category> categorys = categoryService.getAllCategory();
        model.addAttribute("model", category);
        model.addAttribute("categorys", categorys);
        return "category/edit";
    }

    @GetMapping("add")
    public String add(final Model model) {
        List<Category> categorys = categoryService.getAllCategory();
        model.addAttribute("categorys", categorys);
        return "category/add";
    }

    @PostMapping("/editProfile")
    @ResponseBody
    public JsonResult editProfile(Category param){
        boolean result = categoryService.editProfile(param);
        if(result) return JsonResult.successful();
        return JsonResult.failing();
    }


    @PostMapping("/addProfile")
    @ResponseBody
    public JsonResult addProfile(Category param){
        boolean result = categoryService.addProfile(param);
        if(result) return JsonResult.successful();
        return JsonResult.failing();
    }


    @PostMapping("/deleteCategory")
    @ResponseBody
    public JsonResult deleteCategory(Category param){
        boolean result = categoryService.deleteCategory(param);
        if(result) return JsonResult.successful();
        return JsonResult.failing();
    }


}
