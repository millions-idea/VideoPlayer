/***
 * @pName Admin
 * @name ProductApiController
 * @user HongWei
 * @date 2019/3/27
 * @desc
 */
package com.management.admin.apiController;

import com.management.admin.biz.IProductService;
import com.management.admin.entity.resp.ProductDetail;
import com.management.admin.entity.template.JsonResult;
import com.management.admin.entity.template.SessionModel;
import com.management.admin.utils.web.SessionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("api/product")
public class ProductApiController {
    @Autowired
    private IProductService productService;

    @GetMapping("/getProduct")
    public JsonResult getProduct(HttpServletRequest request, String productId){
        ProductDetail detail =  null;
        boolean isException = false;

        try {
            SessionModel session = SessionUtil.getSession(request);
            detail = productService.getProductDetail(Long.valueOf(productId), session.getUserId());
        }catch (Exception e){
            isException = true;
        }

        if(isException){
            detail = productService.getProductDetail(Long.valueOf(productId), 0L);
        }

        return new JsonResult().successful(detail);
    }

    @GetMapping("/collect")
    public JsonResult collect(HttpServletRequest request , String productId){
        SessionModel session = SessionUtil.getSession(request);
        boolean result = productService.collect(Long.valueOf(productId), session.getUserId());
        if(result){
            return JsonResult.successful();
        }
        return JsonResult.failing();
    }

    @GetMapping("/unCollect")
    public JsonResult unCollect(HttpServletRequest request , String productId){
        SessionModel session = SessionUtil.getSession(request);
        boolean result = productService.unCollect(Long.valueOf(productId), session.getUserId());
        if(result){
            return JsonResult.successful();
        }
        return JsonResult.failing();
    }

    @GetMapping("/player")
    public JsonResult player(HttpServletRequest request, String productId){
        SessionModel session = SessionUtil.getSession(request);
        boolean result = productService.player(session.getUserId(), Long.valueOf(productId));
        if(result) return JsonResult.successful();
        return JsonResult.failing();
    }
}
