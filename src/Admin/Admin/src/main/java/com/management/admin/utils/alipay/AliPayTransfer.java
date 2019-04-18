/***
 * @pName Admin
 * @name AliPayTransfer
 * @user HongWei
 * @date 2019/3/13
 * @desc
 */
package com.management.admin.utils.alipay;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AliPayTransfer {
    private AliPayTransferResponse alipay_fund_trans_toaccount_transfer_response;
    private String sign;
}
