/***
 * @pName Admin
 * @name OrderView
 * @user HongWei
 * @date 2019/3/9
 * @desc
 */
package com.management.admin.entity.resp;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderView {
    private String  orderId ;

    private String  productId ;

    private String productName ;

    private String productImage ;

    private Double amount ;

    private Double returnAmount ;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date addDate ;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")

    private Date editDate ;

    /**
     * 状态（0=待付款，1=待发货，2=已完成）
     */
    private Integer status ;

    private String userId ;

    private String channel;


    private String channelText;

    private String statusText;

    private Integer showCount;


    private String account;

    private String categoryName;

}
