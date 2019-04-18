/***
 * @pName Admin
 * @name YaBoLoginDataResp
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
public class YaBoLoginDataResp {
    private Integer id;
    private String name;
    private String real_name;
    private Boolean is_detail;
    private String token;
}
