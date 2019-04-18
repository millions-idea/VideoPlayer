/***
 * @pName Admin
 * @name ProductParam
 * @user HongWei
 * @date 2019/3/13
 * @desc
 */
package com.management.admin.entity.param;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductParam {
    private String productId;

    /**
     * 商品名称
     */
    private String productName ;

    /**
     * 单价
     */
    private Double amount ;

    /**
     * 佣金
     */
    private Double returnAmount ;

    /**
     * 图像地址
     */
    private String image ;

    /**
     * 类目id
     */
    private Integer categoryId ;

    private Date addDate;

    private Date editDate;

    private Integer status;

    private Integer showCount;

    private String videoUrl;

    private Integer top;
}
