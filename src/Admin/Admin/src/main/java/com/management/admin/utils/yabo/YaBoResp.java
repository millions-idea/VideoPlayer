/***
 * @pName Admin
 * @name YaBoProfileResp
 * @user HongWei
 * @date 2019/4/1
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
public class YaBoResp {
    private String flags;
    private String status;
    private Integer status_code;
    private String message;
}
