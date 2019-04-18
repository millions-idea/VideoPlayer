/***
 * @pName Admin
 * @name WxUserInfo
 * @user HongWei
 * @date 2019/3/12
 * @desc
 */
package com.management.admin.utils.wxOpen;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WxUserInfo {
   /* openid	用户的唯一标识
    nickname	用户昵称
    sex	用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
    province	用户个人资料填写的省份
    city	普通用户个人资料填写的城市
    country	国家，如中国为CN
    headimgurl	用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空。若用户更换头像，原有头像URL将失效。
    privilege	用户特权信息，json 数组，如微信沃卡用户为（chinaunicom）
    unionid	只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段。*/

    private String openid;
    private String nickname;
    private String sex;
    private String province;
    private String city;
    private String country;
    private String headimgurl;
    private String privilege;
    private String unionid;
}
