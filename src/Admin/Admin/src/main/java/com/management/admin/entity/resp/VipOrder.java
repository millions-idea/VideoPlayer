/***
 * @pName Admin
 * @name VipOrder
 * @user HongWei
 * @date 2019/3/10
 * @desc
 */
package com.management.admin.entity.resp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VipOrder {
    private String title;
    private Double amount;
    private String id;
}
