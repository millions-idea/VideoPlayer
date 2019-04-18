/***
 * @pName Admin
 * @name Order
 * @user HongWei
 * @date 2019/3/9
 * @desc
 */
package com.management.admin.entity.db;

import lombok.*;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "tb_orders")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @Id
    private Long orderId ;

    private Long productId ;

    private String productName ;

    private String productImage ;

    private Double amount ;

    private Date addDate ;

    private Date editDate ;

    /**
     * 状态（0=待付款，1=待发货，2=已完成）
     */
    private Integer status ;

    private Long userId ;

    private String channel;

}
