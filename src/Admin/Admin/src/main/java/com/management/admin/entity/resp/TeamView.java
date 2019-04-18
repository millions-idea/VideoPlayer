/***
 * @pName Admin
 * @name TeamView
 * @user HongWei
 * @date 2019/3/8
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
public class TeamView {
    private Integer directSaleCount;
    private Integer samllMarketCount;
    private Integer channelSaleCount;
}
