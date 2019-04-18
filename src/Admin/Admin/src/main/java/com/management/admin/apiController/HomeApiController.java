/***
 * @pName Admin
 * @name HomeApiController
 * @user HongWei
 * @date 2019/3/9
 * @desc
 */
package com.management.admin.apiController;

import com.management.admin.biz.ICategoryService;
import com.management.admin.biz.IProductService;
import com.management.admin.entity.db.Category;
import com.management.admin.entity.db.Product;
import com.management.admin.entity.resp.Banner;
import com.management.admin.entity.resp.HomeConfig;
import com.management.admin.entity.resp.ProductView;
import com.management.admin.entity.template.*;
import com.management.admin.utils.*;
import com.management.admin.utils.http.TencentCosUtil;
import com.management.admin.utils.poster.PosterUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = {"/api/home", "/api"})
public class HomeApiController {
    @Autowired
    private ICategoryService categoryService;
    @Autowired
    private IProductService productService;

    @GetMapping("/getHomeConfig")
    public JsonResult<HomeConfig> getHomeConfig(){
        List<Banner> list = new ArrayList<>();
        list.add(new Banner(DataDictionary.Map.get("link.img.homeBanner1").getValue(), DataDictionary.Map.get("link.text.homeBanner1").getValue()));
        list.add(new Banner(DataDictionary.Map.get("link.img.homeBanner2").getValue(), DataDictionary.Map.get("link.text.homeBanner2").getValue()));
        list.add(new Banner(DataDictionary.Map.get("link.img.homeBanner3").getValue(), DataDictionary.Map.get("link.text.homeBanner3").getValue()));

        HomeConfig homeConfig = new HomeConfig();
        homeConfig.setBannerList(list);
        homeConfig.setNotice(DataDictionary.Map.get("link.text.homeNotice").getValue());


        homeConfig.setBusinessSchoolLink(DataDictionary.Map.get("link.business.school").getValue());
        homeConfig.setCommissionSystemLink(DataDictionary.Map.get("link.commission.system").getValue());
        homeConfig.setAgentHelpLink(DataDictionary.Map.get("link.agent.help").getValue());
        homeConfig.setAppDownloadLink(DataDictionary.Map.get("link.app.download").getValue());
        return new JsonResult<>().successful(homeConfig);
    }

    @GetMapping("/getProduct")
    public JsonResult<ProductView> getProduct(String productId){
        Product product =  productService.getProduct(Long.valueOf(productId));
        ProductView productView = new ProductView();
        PropertyUtil.clone(product, productView);
        productView.setProductId(product.getProductId() + "");
        if(productView != null) return new JsonResult<>().successful(productView);
        return JsonResult.failing();
    }

    /**
     * APP在线更新
     * @param version 客户端读取到的版本号信息
     * @return http://ask.dcloud.net.cn/article/35667
     */
    @GetMapping("/update")
    public AppUpdate update(String version){
        String json = DataDictionary.Map.get("app.version").getValue();
        AppVersion appVersion = JsonUtil.getModel(json, AppVersion.class);
        String[] currentVersionNums = version.split("\\.");
        String[] dbVersionNums = appVersion.getVersion().split("\\.");
        //大版本整包更新
        if(Integer.valueOf(currentVersionNums[0]) < Integer.valueOf(dbVersionNums[0])){
            return new AppUpdate(null, appVersion.getPkgUrl(),  true, appVersion.getNode(), appVersion.getShowCancel());
        }else if(!version.equalsIgnoreCase(appVersion.getVersion())){
            //小版本增量更新
            return new AppUpdate(appVersion.getWgtUrl(), null,  true, appVersion.getNode(), false);
        }
        return new AppUpdate(null, null,  false, null, false);
    }

    /**
     * 获取最近更新的视频列表 DF 2019年3月20日09:59:48
     * @param page
     * @return
     */
    @GetMapping("/getNewProductLimit")
    public JsonArrayResult<ProductView> getNewProductLimit(Integer page){
        List<ProductView> list = productService.getNewProductLimit(page);
        Integer maxPage = 0;
        if(list == null || list.size() == 0){
            //获取本年更新的视频列表
            list = productService.getNewProductLimitWithYear(page);
            maxPage = productService.getNewProductTotalPageWithYear();
        }else{
            maxPage = productService.getNewProductTotalPage();
        }
        JsonArrayResult jsonArrayResult = new JsonArrayResult(0, list);
        jsonArrayResult.setCode(200);
        jsonArrayResult.setMsg("success");
        jsonArrayResult.setCount(maxPage == null ? 1000 : maxPage);
        return jsonArrayResult;
    }

    /**
     * 获取指定分类下的视频列表 DF 2019年3月20日10:00:30
     * @param page
     * @param categoryId
     * @return
     */
    @GetMapping("/getCategoryProductLimit")
    public JsonArrayResult<ProductView> getCategoryProductLimit(Integer page, Integer categoryId){
        List<ProductView> list = productService.getCategoryProductLimit(page, categoryId);
        Integer maxPage = productService.getNewProductTotalPage();
        JsonArrayResult jsonArrayResult = new JsonArrayResult(0, list);
        jsonArrayResult.setCode(200);
        jsonArrayResult.setMsg("success");
        jsonArrayResult.setCount(maxPage == null ? 1000 : maxPage);
        return jsonArrayResult;
    }
}
