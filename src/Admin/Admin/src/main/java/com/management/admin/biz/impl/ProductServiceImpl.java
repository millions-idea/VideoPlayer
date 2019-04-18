/***
 * @pName Admin
 * @name ProductServiceImpl
 * @user HongWei
 * @date 2019/3/9
 * @desc
 */
package com.management.admin.biz.impl;

import com.management.admin.biz.IProductService;
import com.management.admin.entity.db.Collect;
import com.management.admin.entity.db.History;
import com.management.admin.entity.db.Order;
import com.management.admin.entity.db.Product;
import com.management.admin.entity.param.ProductParam;
import com.management.admin.entity.resp.ProductDetail;
import com.management.admin.entity.resp.ProductView;
import com.management.admin.entity.template.Constant;
import com.management.admin.entity.template.DataDictionary;
import com.management.admin.exception.InfoException;
import com.management.admin.repository.CollectMapper;
import com.management.admin.repository.HistoryMapper;
import com.management.admin.repository.OrderMapper;
import com.management.admin.repository.ProductMapper;
import com.management.admin.repository.utils.ConditionUtil;
import com.management.admin.utils.*;
import com.management.admin.utils.http.TencentCosUtil;
import com.management.admin.utils.poster.PosterUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class ProductServiceImpl implements IProductService {
    private final ProductMapper productMapper;
    private final OrderMapper orderMapper;
    private final CollectMapper collectMapper;
    private final HistoryMapper historyMapper;

    @Autowired
    public ProductServiceImpl(ProductMapper productMapper, OrderMapper orderMapper, CollectMapper collectMapper, HistoryMapper historyMapper) {
        this.productMapper = productMapper;
        this.orderMapper = orderMapper;
        this.collectMapper = collectMapper;
        this.historyMapper = historyMapper;
    }

    @Override
    public List<ProductView> getProductLimit(Integer categoryId, Integer page) {
        String size =  "10";
        if(page == null) page = 0;
        List<Product> list = productMapper.selectProductLimit(categoryId, page, size);
        List<ProductView> viewList = new ArrayList<>();
        list.forEach(item -> {
            ProductView view = new ProductView();
            PropertyUtil.clone(item, view);
            view.setProductId(item.getProductId() + "");
            viewList.add(view);
        });
        return viewList;
    }

    @Override
    public Integer getProductTotalPage(Integer categoryId) {
        List<Product> list = productMapper.selectProduct(categoryId);
        Integer size = list.size() / 10;
        return size < 0 ? 1000 : size;
    }

    @Override
    public Product getProduct(Long productId) {
        return productMapper.selectById(productId);
    }

    @Override
    public boolean changePoster(Long userId, String url) {
        return productMapper.changePoster(userId, url) > 0;
    }

    @Override
    public boolean putaway(ProductParam param) {
        return productMapper.updateSale(param.getProductId(), 1) > 0;
    }

    @Override
    public boolean soldOut(ProductParam param) {
        return productMapper.updateSale(param.getProductId(), 2) > 0;
    }

    @Override
    public boolean rePublish(ProductParam param) {
        return productMapper.updateProduct(param);
    }

    @Override
    public Integer getLimitCount(String condition, Integer state, String beginTime, String endTime) {
        String where = extractLimitWhere(condition, state, beginTime, endTime);
        return productMapper.selectLimitCount(state, beginTime, endTime, where);
    }

    @Override
    public Integer getCount() {
        return productMapper.selectCount(new Product());

    }

    @Override
    public List<ProductDetail> getLimit(Integer page, String limit, String condition, Integer state, String beginTime, String endTime) {
        // 计算分页位置
        page = ConditionUtil.extractPageIndex(page, limit);
        String where = extractLimitWhere(condition, state, beginTime, endTime);
        List<ProductDetail> list = productMapper.selectLimit(page, limit, beginTime, endTime, where);
        return list;
    }

    @Override
    @Transactional
    public boolean addProduct(ProductParam param) {
        if(param.getImage() == null || param.getImage().length() <= 1) throw new InfoException("请上传商品主图");
        if(param.getShowCount() == null) param.setShowCount(0);
        Product product = new Product();
        PropertyUtil.clone(param, product);
        try {
            long id = IdWorker.getFlowIdWorkerInstance().nextId();
            product.setProductId(id);
            product.setStatus(0);
            boolean result = productMapper.insertProduct(product) > 0;
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Product getSaleProduct(Long productId) {
        return productMapper.selectSaleProduct(productId);
    }


    @Override
    public boolean deleteProduct(ProductParam param) {
        boolean result =  productMapper.deleteProduct(Long.valueOf(param.getProductId())) > 0;
        return result;
    }

    @Override
    public List<ProductView> getNewProductLimit(Integer page) {
        String size =  "10";
        if(page == null) page = 0;
        List<Product> list = productMapper.selectNewProductLimit( page, size);
        List<ProductView> viewList = new ArrayList<>();
        list.forEach(item -> {
            ProductView view = new ProductView();
            PropertyUtil.clone(item, view);
            view.setProductId(item.getProductId() + "");
            viewList.add(view);
        });
        return viewList;
    }

    @Override
    public Integer getNewProductTotalPage() {
        List<Product> list = productMapper.getNewProductTotalPage();
        Integer size = list.size() / 10;
        return size < 0 ? 1000 : size;
    }

    @Override
    public List<ProductView> getCategoryProductLimit(Integer page, Integer categoryId) {
        String size =  "10";
        if(page == null) page = 0;
        List<Product> list = productMapper.getCategoryProductLimit( page, size, categoryId);
        List<ProductView> viewList = new ArrayList<>();
        list.forEach(item -> {
            ProductView view = new ProductView();
            PropertyUtil.clone(item, view);
            view.setProductId(item.getProductId() + "");
            viewList.add(view);
        });
        return viewList;
    }

    @Override
    public List<ProductView> getPlayHistory(Long userId, Integer page) {
        String size =  "10";
        if(page == null) page = 0;
        List<Product> list = productMapper.getPlayHistoryLimit(userId, page, size);
        List<ProductView> viewList = new ArrayList<>();
        list.forEach(item -> {
            ProductView view = new ProductView();
            PropertyUtil.clone(item, view);
            view.setProductId(item.getProductId() + "");
            viewList.add(view);
        });
        return viewList;
    }

    @Override
    public Integer getPlayHistoryTotalPage(Long userId) {
        int count = productMapper.getPlayHistoryTotalPage(userId);
        Integer size = count / 10;
        if(count < 10) size = 1;
        return size < 0 ? 1000 : size;
    }

    @Override
    public List<ProductView> getNewProductLimitWithYear(Integer page) {
        String size =  "10";
        if(page == null) page = 0;
        List<Product> list = productMapper.selectNewProductLimitWithYear( page, size);
        List<ProductView> viewList = new ArrayList<>();
        list.forEach(item -> {
            ProductView view = new ProductView();
            PropertyUtil.clone(item, view);
            view.setProductId(item.getProductId() + "");
            viewList.add(view);
        });
        return viewList;
    }

    @Override
    public Integer getNewProductTotalPageWithYear() {
        List<Product> list = productMapper.getNewProductTotalPageWithYear();
        Integer size = list.size() / 10;
        return size < 0 ? 1000 : size;
    }

    @Override
    public ProductDetail getProductDetail(Long productId, Long userId) {
        final Long finalUserId = userId;

        ProductDetail detail = productMapper.selectProductDetail(productId);
        List<ProductDetail> adjoinList = productMapper.selectListByCategoryId(detail.getCategoryId());
        detail.setAdjoinList(adjoinList);

        Double permanent = Double.valueOf(DataDictionary.Map.get("product.price.permanent").getValue());
        detail.setCommonAmount(permanent);

        String commonDesc = DataDictionary.Map.get("product.desc.common").getValue();
        detail.setCommonDesc(commonDesc);

        Order order =  null;
        if(!userId.equals(0L)){
           order = orderMapper.selectOrderByUserProduct(detail.getProductId(), finalUserId);
        }
        if(order == null){
            detail.setBuy(false);
            detail.setHowToBuy(0);
            detail.setVideoUrl(null);
        }else{
            if(order.getStatus().equals(2)){
                if(order.getAmount() < permanent){
                    detail.setBuy(true);
                    detail.setHowToBuy(1);
                }else if(order.getAmount().equals(permanent)){
                    detail.setBuy(true);
                    detail.setHowToBuy(2);
                }
            }else{
                detail.setBuy(false);
                detail.setHowToBuy(0);
                detail.setVideoUrl(null);
            }
        }

       try{
           Collect collect = collectMapper.selectByUserProduct(productId, finalUserId);
           detail.setCollect(collect != null);
       }catch (Exception e){
           System.err.println(e);
       }

       //是否已经购买过此视频资源--单次观看
        if(detail.isBuy() && detail.getHowToBuy().equals(1)){
            History history = historyMapper.selectByUserProduct(finalUserId, productId);
            if(history != null && !history.getAmount().equals(permanent)){
                detail.setWatchOne(true);
            }
        }

        return detail;
    }

    @Override
    public boolean collect(Long productId, Long userId) {
        Collect collect = new Collect();
        collect.setProductId(productId);
        collect.setUserId(userId);
        collect.setAddDate(new Date());
        return collectMapper.insert(collect) > 0;
    }

    @Override
    public boolean unCollect(Long productId, Long userId) {
        return collectMapper.deleteByUser(productId, userId) > 0;
    }

    @Override
    @Transactional
    public boolean player(Long userId, Long productId) {
        int count = 0;

        ProductDetail detail = productMapper.selectProductDetail(productId);

        detail.setUserId(userId);
        try{
            historyMapper.insertHistory(detail);
            productMapper.addShowCount(productId);
        }catch (Exception e){
            System.err.println(e);
        }

        //删除临时订单
        Order order = orderMapper.selectOrderByUserProduct(productId + "", userId);
        if(order != null){
            Double permanent = Double.valueOf(DataDictionary.Map.get("product.price.permanent").getValue());
            if(!order.getAmount().equals(permanent)){
                count = orderMapper.deleteByPrimaryKey(order);
                if(count == 0){
                    throw new InfoException("更新订单信息失败");
                }
            }
        }

        return true;
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
            where += " AND (" + ConditionUtil.like("product_id", condition, true, "t1");
            if (condition.split("-").length == 2){
                where += " OR " + ConditionUtil.like("add_date", condition, true, "t1");
                where += " OR " + ConditionUtil.like("edit_date", condition, true, "t1");
            }
            where += " OR " + ConditionUtil.like("product_name", condition, true, "t1") + ")";
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
