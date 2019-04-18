/***
 * @pName Admin
 * @name StateParam
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
public class StateParam {
    private String text;
    private String link;
    private String[] imgs;
}
