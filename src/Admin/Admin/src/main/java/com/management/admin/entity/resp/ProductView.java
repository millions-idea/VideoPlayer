/***
 * @pName Admin
 * @name ProductView
 * @user HongWei
 * @date 2019/3/9
 * @desc
 */
package com.management.admin.entity.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ProductView {
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

    /**
     * 类目id
     */
    private Integer categoryId ;
    private String categoryName;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date addDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date editDate;

    private Integer showCount;
    private String statusText;
    private Integer status;

    private String videoUrl;
}
