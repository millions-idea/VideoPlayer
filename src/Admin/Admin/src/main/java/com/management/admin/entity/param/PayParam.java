/***
 * @pName management
 * @name PayParam
 * @user DF
 * @date 2018/8/16
 * @desc 交易专用参数类
 */
package com.management.admin.entity.param;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PayParam {
    private Long fromUid;
    private Long toUid;
    private Double amount;
    private String remark;
    private long systemRecordId;
    private Integer currency = 0;
    private String channelRecordId;
    private String tradeType;
    private String channel;
    private Date toAccountTime;
    private Integer channelType;
}
