/***
 * @pName management
 * @name UserFacadeService
 * @user HongWei
 * @date 2018/8/20
 * @desc
 */
package com.management.admin.facade;


import com.management.admin.entity.db.User;

public interface IUserFacadeService {
    /**
     * 注册用户 韦德 2018年8月20日15:39:37
     * @param user
     * @param parentUser
     * @param smsCode
     * @return
     */
    boolean registerWithPassword(User user, User parentUser, String smsCode);

    /**
     * 微信公众号注册用户 韦德 2019年3月12日16:07:37
     * @param openid
     * @param access_token
     * @param nickname
     * @param headimgurl
     * @return
     */
    User registerWithWx(String openid, String access_token, String nickname, String headimgurl, String userIp);

    void bind(User user, User parentUser, String smsCode);

    User login(String username, String password, String userIp);

    User loginWithPhone(String username, String username1, String password, String userIp);

    boolean registerWithPhone(User regUser);
}
