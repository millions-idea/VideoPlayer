/***
 * @pName Admin
 * @name BizBody
 * @user HongWei
 * @date 2019/3/9
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
public class BizBody {
    private String channel;
    private Long userId;
    private Long tempOrderId;
}
