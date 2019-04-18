/***
 * @pName Admin
 * @name WxPayParam
 * @user HongWei
 * @date 2019/3/10
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
public class WxPayParam {
    private String paySign;
    private String packageId;
    private String nonceStr;
    private BizBody body;
    private String appId;
}
