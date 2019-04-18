/***
 * @pName Admin
 * @name OrderParam
 * @user HongWei
 * @date 2019/3/8
 * @desc
 */
package com.management.admin.entity.param;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderParam {
    private String amount;
    private String body;
    private String subject;
    private String outTradeNo;
}
