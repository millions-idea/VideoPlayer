/***
 * @pName Admin
 * @name UserController
 * @user HongWei
 * @date 2018/12/16
 * @desc
 */
package com.management.admin.controller;

import com.management.admin.biz.ICategoryService;
import com.management.admin.biz.IProductService;
import com.management.admin.entity.db.Category;
import com.management.admin.entity.db.Product;
import com.management.admin.entity.param.ProductParam;
import com.management.admin.entity.resp.ProductDetail;
import com.management.admin.entity.resp.ProductView;
import com.management.admin.entity.template.Constant;
import com.management.admin.entity.template.JsonArrayResult;
import com.management.admin.entity.template.JsonResult;
import com.management.admin.utils.*;
import com.management.admin.utils.http.TencentCosUtil;
import com.management.admin.utils.poster.PosterUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/management/product")
public class ProductController {
    @Autowired
    private IProductService productService;

    @Autowired
    private ICategoryService categoryService;

    @GetMapping("/index")
    public String index() {
        return "product/index";
    }

    @GetMapping("/publish")
    public String publish(final Model model) {
        List<Category> categorys = categoryService.getAllCategory();
        model.addAttribute("categorys", categorys);
        return "product/publish";
    }

    @GetMapping("/getLimit")
    @ResponseBody
    public JsonArrayResult<ProductView> getLimit(Integer page, String limit, String condition, Integer type, Integer state, String beginTime, String endTime) {
        Integer count = 0;
        List<ProductDetail> list = productService.getLimit(page, limit, condition, state, beginTime, endTime);
        List<ProductView> viewList = new ArrayList<>();
        list.forEach(item -> {
            ProductView view = new ProductView();
            PropertyUtil.clone(item, view);
            view.setProductId(item.getProductId() + "");
            switch (item.getStatus()){
                case 0:
                    view.setStatusText("未上架");
                    break;
                case 1:
                    view.setStatusText("已上架");
                    break;
                case 2:
                    view.setStatusText("已下架");
                    break;
            }
            viewList.add(view);
        });
        JsonArrayResult jsonArrayResult = new JsonArrayResult(0, viewList);
        if (StringUtil.isBlank(condition)
                && StringUtil.isBlank(beginTime)
                && StringUtil.isBlank(endTime)
                && (state == null || state == 0)) {
            count = productService.getCount();
        } else {
            count = productService.getLimitCount(condition, state, beginTime, endTime);
        }
        jsonArrayResult.setCount(count);
        return jsonArrayResult;
    }

    @GetMapping("edit")
    public String edit(String productId, final Model model) {
        Product product = productService.getSaleProduct(Long.valueOf(productId));
        ProductView view = new ProductView();
        PropertyUtil.clone(product,  view);
        view.setProductId(product.getProductId() + "");
        List<Category> categorys = categoryService.getAllCategory();
        model.addAttribute("editDate",view.getEditDate());
        model.addAttribute("addDate",view.getAddDate());
        model.addAttribute("model", view);
        model.addAttribute("categorys", categorys);
        return "product/edit";
    }

    @PostMapping("/rePublish")
    @ResponseBody
    public JsonResult rePublish(ProductParam param ){
        boolean result = productService.rePublish(param);
        if(result) return JsonResult.successful();
        return JsonResult.failing();
    }

    @PostMapping("/putaway")
    @ResponseBody
    public JsonResult putaway(ProductParam param){
        boolean result = productService.putaway(param);
        if(result) return JsonResult.successful();
        return JsonResult.failing();
    }

    @PostMapping("/soldOut")
    @ResponseBody
    public JsonResult soldOut(ProductParam param){
        boolean result = productService.soldOut(param);
        if(result) return JsonResult.successful();
        return JsonResult.failing();
    }

    @PostMapping("/deleteProduct")
    @ResponseBody
    public JsonResult deleteProduct(ProductParam param){
        boolean result = productService.deleteProduct(param);
        if(result) return JsonResult.successful();
        return JsonResult.failing();
    }

    @PostMapping("/create")
    @ResponseBody
    public JsonResult create(ProductParam param){
        boolean result = productService.addProduct(param);
        if(result) return JsonResult.successful();
        return JsonResult.failing();
    }


}
