/***
 * @pName Admin
 * @name AliPayTransferResponse
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
public class AliPayTransferResponse {
    private String code;
    private String msg;
    private String out_biz_no;
    private String order_id;
    private String pay_date;
    private String status;
    private String arrival_time_end;
    private String order_fee;
    private String fail_reason;
    private String error_code;
}
