/***
 * @pName Admin
 * @name Product
 * @user HongWei
 * @date 2019/3/9
 * @desc
 */
package com.management.admin.entity.db;

import lombok.*;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "tb_products")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    private Long productId ;

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

    /**
     * 类目id
     */
    private Integer categoryId ;

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

    private String videoUrl;

    private Integer top;
}
