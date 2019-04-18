/***
 * @pName Admin
 * @name PaysView
 * @user HongWei
 * @date 2019/3/9
 * @desc
 */
package com.management.admin.entity.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PaysView extends GroupView{
    private Long payId;
    private Long fromUid;
    private String fromName;
    private Long toUid;
    private String toName;
    private Integer channelType;
    private String channelName;
    private Integer productType;
    private String productName;
    private Integer tradeType;
    private String tradeName;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date addTime;
    private Double amount;
    private Long systemRecordId;
    private String remark;
    private String channelRecordId;
    private Integer status;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date toAccountTime;
}
