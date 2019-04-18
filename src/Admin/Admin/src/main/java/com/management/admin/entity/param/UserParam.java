/***
 * @pName Admin
 * @name UserParam
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
public class UserParam {
    private String userId;
    private String username;
    private String phone;
    private String password;
    private String smsCode;
    private String promotionCode;
    private String nickName;
    private Integer level;
    private String account;
}
