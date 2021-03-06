/***
 * @pName management
 * @name Wallets
 * @user HongWei
 * @date 2018/8/15
 * @desc 用户钱包表
 */
package com.management.admin.entity.db;

import lombok.*;
import tk.mybatis.mapper.annotation.Version;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "tb_wallets")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Wallet {
    @Id
    private Integer walletId;
    private Long userId;
    private Double balance;
    private Date updateTime;
    @Version
    private Integer version;
}
