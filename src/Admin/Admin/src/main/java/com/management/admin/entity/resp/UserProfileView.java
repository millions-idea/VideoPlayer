/***
 * @pName Admin
 * @name UserProfileView
 * @user HongWei
 * @date 2019/3/8
 * @desc
 */
package com.management.admin.entity.resp;

import com.management.admin.utils.yabo.YaBoProfileResp;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileView {
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
     * 手机号(用户名)
     */
    private String phone ;

    /**
     * 头像
     */
    private String photo ;

    /**
     * 昵称(默认=用户编码)
     */
    private String nickName ;

    /**
     * 推荐人id
     */
    private Long parentId ;


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

    /**
     * 提现支付宝账号
     */
    private String financeId;

    /**
     * 提现支付宝姓名
     */
    private String financeName;

    /**
     * 微信二维码名片
     */
    private String wechatBusinessCard;

    private String avatar;
    private Boolean exitsPayment;
    private Boolean isVip;

    private String token;

    private UserProfileView parentUser;


    private Double balance;

    private YaBoProfileResp yabo;

    private String yaboUrl;
    private String yaboAppUrl;


}
