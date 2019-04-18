/***
 * @pName Admin
 * @name CollectDetailInfo
 * @user HongWei
 * @date 2019/3/22
 * @desc
 */
package com.management.admin.entity.dbExt;

import lombok.*;
import org.joda.time.DateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CollectDetailInfo {
    private Integer collectId;
    private Long productId;
    private String productName;
    private String image;
    private Integer showCount;
    private Long userId;
    private DateTime collectDate;
}
