/***
 * @pName management
 * @name WithdrawResp
 * @user HongWei
 * @date 2018/8/30
 * @desc
 */
package com.management.admin.entity.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WithdrawResp {
    /*withdraw*/
    private Integer withdrawId;
    private String userId;
    private Double amount;
    private Integer state;
    private String remark;
    private String systemRecordId;
    private String channelRecordId;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date addTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    private String financeId;
    private String financeName;

    /*users*/
    private String phone;
    private String account;
}
