/***
 * @pName Admin
 * @name AccessToken
 * @user HongWei
 * @date 2019/3/12
 * @desc
 */
package com.management.admin.utils.wxOpen;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WxAccessToken {
    private String access_token;
    private String refresh_token;
    private String openid;
    private String scope;
    private String expires_in;
    private String errcode;
}
