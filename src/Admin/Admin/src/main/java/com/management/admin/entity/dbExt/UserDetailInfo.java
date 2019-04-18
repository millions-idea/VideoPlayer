/***
 * @pName management
 * @name UserWalletDetail
 * @user HongWei
 * @date 2018/8/16
 * @desc
 */
package com.management.admin.entity.dbExt;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailInfo {
    /*users*/
    private Long userId ;

    /**
     * 用户初始绑定账号(可以是手机号可以是微信openId)
     */
    private String account ;

    /**
     * 微信用户openId
     */
    private String openId ;

    /**
     * 微信验证令牌
     */
    private String openToken ;

    /**
     * 手机号(用户名)
     */
    private String phone ;

    /**
     * 密码
     */
    private String password ;

    /**
     * 头像
     */
    private String photo ;

    /**
     * 昵称(默认=用户编码)
     */
    private String nickName ;

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

    /**
     * 备注
     */
    private String remark ;

    /**
     * 注册ip
     */
    private String ip ;

    /**
     * 推荐人id
     */
    private Long parentId ;

    /**
     * 关系路径
     */
    private String path ;

    /**
     * 等级(0=普通用户,1=会员,2=初级代理,3=区域代理,4=高级代理,5=事业股东)
     */
    private Integer level;

    /**
     * 账户类型(0=手机号, 1=微信)
     */
    private Integer type;

    /**
     * 推广码
     */
    private String promotionCode;

    private String paymentPassword;

    /*wallets*/
    private Integer walletId;
    private Double balance;
    private Date roomCardGetTime;
    private Date updateTime;
    private Integer version;

    
    private String levelText;


    private  Integer playerCount;

}
