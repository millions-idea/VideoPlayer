/***
 * @pName Admin
 * @name WithdrawParam
 * @user HongWei
 * @date 2019/3/16
 * @desc
 */
package com.management.admin.entity.param;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Id;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WithdrawParam {
    private Integer withdrawId;
    private String userId;
    private Double amount;
    private Integer state;
    private String remark;
    private String systemRecordId;
    private String channelRecordId;
    private Date addTime;
    private Date updateTime;
    private String financeId;
    private String financeName;
}
