/***
 * @pName Admin
 * @name HuPayResp
 * @user HongWei
 * @date 2019/4/2
 * @desc
 */
package com.management.admin.utils.hupay;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HuPayResp {
    private String url;
    private Integer errcode;
    private String errmsg;
    private String hash;
}
