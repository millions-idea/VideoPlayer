/***
 * @pName management
 * @name Pays
 * @user HongWei
 * @date 2018/8/15
 * @desc 财务交易记录表
 */
package com.management.admin.entity.db;

import lombok.*;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "tb_pays")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Pays {
    @Id
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
    private Date addTime;
    private Double amount;
    private Long systemRecordId;
    private String remark;
    private String channelRecordId;
    private Integer status;
    private Date toAccountTime;
}
