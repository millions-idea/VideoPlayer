/***
 * @pName Admin
 * @name IUserService
 * @user HongWei
 * @date 2019/3/7
 * @desc
 */
package com.management.admin.biz;

import com.management.admin.entity.db.Order;
import com.management.admin.entity.db.User;
import com.management.admin.entity.dbExt.UserDetailInfo;
import com.management.admin.entity.param.UserParam;
import com.management.admin.entity.resp.*;

import java.util.List;

public interface IUserService {
    Boolean registerWithPhone(User user);

    UserResp getSysUserById(Long systemAccountsId);

    User getUserByAccount(String account);

    int changeBalance(String username, Double amount);

    User getUserByPromotionCode(String promotionCode);

    /**
     * 获取推广会员列表
     * @param userId
     * @return
     */
    List<User> getPromoteMembers(Long userId);

    /**
     * 晋级或忽略
     * @param userId
     * @param level
     */
    void changeLevel(Long userId, Integer level);

    User getUserById(Long userId);

    Boolean resetPassword(String account,String phone, String password, String smsCode);

    User loginByUsername(String phone, String password);

    boolean changeAvatar(Long userId, String url);

    boolean changeNickName(Long userId, String nickName);

    boolean changeWechatCard(Long userId, String url);

    boolean changePhone(Long userId, String phone, String smsCode);

    boolean changePaymentPassword(String account, Long userId, String password, String smsCode);

    UserAssetView getAssets(Long userId);

    boolean verifyPaymentPassword(String account, Long userId, String password);

    List<MemberView> getPromoteMembersLimit(Long userId, Integer page, String phone);

    Integer getPromoteMembersTotalPage(Long userId);

    boolean changePoster(Long userId, String url);

    void bind(User user);

    Boolean registerWithWx(User user);

    User getUserByOpenId(String openid);

    UserProfileView getUserWalletById(Long userId);

    List<User> getLimit(Integer page, String limit, String condition, Integer state, String beginTime, String endTime);

    Integer getCount();

    Integer getLimitCount(String condition, Integer state, String beginTime, String endTime);

    boolean editProfile(UserParam param);

    boolean blocking(UserParam param);

    boolean changePassword(String account, Long userId, String password, String smsCode);

    User loginWithPhone(String phone, String username, String password);

    TeamView getTeamPreview(Long userId);

    boolean changeYaBoToken(Long userId, String token);

    boolean unBlocking(UserParam param);

    List<UserView> getAgentLimit(Integer page, String limit, String condition, Integer state, String beginTime, String endTime);

    Integer getAgentCount();

    Integer getAgentLimitCount(String condition, Integer state, String beginTime, String endTime);

    List<OrderView> getOrders(Long userId);

    Integer getPlayerCount(Long userId);

    UserDetailInfo getUserViewById(Long aLong);
}
