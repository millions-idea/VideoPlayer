/***
 * @pName Admin
 * @name User
 * @user HongWei
 * @date 2019/3/6
 * @desc 用户表
 */
package com.management.admin.entity.db;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Table;
import java.util.Date;

@Table(name = "tb_admin_users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminUser {
    private Integer userId ;

    /**
     * 用户初始绑定账号(可以是手机号可以是微信openId)
     */
    private String account ;

    /**
     * 密码
     */
    private String password ;

    /**
     * 注册时间
     */
    private Date addDate ;

    /**
     * 最后一次编辑时间
     */
    private Date editDate ;

    /**
     * 是否删除(黑名单)
     */
    private Integer isDelete ;
}
