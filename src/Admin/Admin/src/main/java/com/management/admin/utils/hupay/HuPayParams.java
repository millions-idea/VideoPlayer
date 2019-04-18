/***
 * @pName Admin
 * @name HuPayParams
 * @user HongWei
 * @date 2019/4/2
 * @desc
 */
package com.management.admin.utils.hupay;

import com.management.admin.entity.param.BizBody;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HuPayParams {
    private String tradeOrderId;
    private String title;
    private Double amount;
    private BizBody ext;
}

