/***
 * @pName Admin
 * @name ProductDetail
 * @user HongWei
 * @date 2019/3/27
 * @desc
 */
package com.management.admin.entity.resp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductDetail {
    private String productId ;

    /**
     * 商品名称
     */
    private String productName ;

    /**
     * 单价
     */
    private Double amount ;

    /**
     * 图像地址
     */
    private String image ;

    private String videoUrl;

    /**
     * 类目id
     */
    private Integer categoryId ;

    private String categoryName;

    private Date addDate ;

    private Date editDate ;

    /**
     * 状态（0=未上架，1=已上架，2=已下架）
     */
    private Integer status ;

    private Integer showCount ;

    /**
     * 是否显示(0=显示1=隐藏)
     */
    private Integer isShow ;


    /**
     * 是否已经购买
     */
    private boolean isBuy;
    /**
     * 购买的方式, 0=未购买, 1=观看一次, 2=永久观看
     */
    private Integer HowToBuy;


    /**
     * 相似商品推荐
     */
    private List<ProductDetail> adjoinList;


    private Double commonAmount;

    private String commonDesc;

    private boolean isCollect;

    private Long userId;

    private boolean isWatchOne;
}
