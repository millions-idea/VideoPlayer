/***
 * @pName Admin
 * @name History
 * @user HongWei
 * @date 2019/3/27
 * @desc
 */
package com.management.admin.entity.db;

import lombok.*;

import javax.persistence.Table;
import java.util.Date;

@Table(name = "tb_historys")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class History {
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

    private Long userId ;

    private Integer showCount ;


}
