/***
 * @pName Admin
 * @name IProductService
 * @user HongWei
 * @date 2019/3/9
 * @desc
 */
package com.management.admin.biz;

import com.management.admin.entity.db.Product;
import com.management.admin.entity.param.ProductParam;
import com.management.admin.entity.resp.ProductDetail;
import com.management.admin.entity.resp.ProductView;

import java.util.List;

public interface IProductService {
    List<ProductView> getProductLimit(Integer categoryId, Integer page);

    Integer getProductTotalPage(Integer categoryId);

    Product getProduct(Long productId);

    boolean changePoster(Long productId, String url);

    boolean putaway(ProductParam param);

    boolean soldOut(ProductParam param);

    boolean rePublish(ProductParam param);

    Integer getLimitCount(String condition, Integer state, String beginTime, String endTime);

    Integer getCount();

    List<ProductDetail> getLimit(Integer page, String limit, String condition, Integer state, String beginTime, String endTime);

    boolean addProduct(ProductParam param);

    Product getSaleProduct(Long productId);

    boolean deleteProduct(ProductParam param);

    List<ProductView> getNewProductLimit(Integer page);

    Integer getNewProductTotalPage();

    List<ProductView> getCategoryProductLimit(Integer page, Integer categoryId);

    List<ProductView> getPlayHistory(Long userId, Integer page);

    Integer getPlayHistoryTotalPage(Long userId);

    List<ProductView> getNewProductLimitWithYear(Integer page);

    Integer getNewProductTotalPageWithYear();

    ProductDetail getProductDetail(Long productId, Long userId);

    boolean collect(Long productId, Long userId);

    boolean unCollect(Long productId, Long userId);

    boolean player(Long userId, Long aLong);
}
