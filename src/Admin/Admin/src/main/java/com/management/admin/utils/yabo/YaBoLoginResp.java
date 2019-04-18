/***
 * @pName Admin
 * @name YaBoLoginResp
 * @user HongWei
 * @date 2019/3/31
 * @desc
 */
package com.management.admin.utils.yabo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class YaBoLoginResp {
    private String flags;
    private String status;
    private Integer status_code;
    private String message;
    private YaBoLoginDataResp data;
}
