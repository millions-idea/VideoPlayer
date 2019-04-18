/***
 * @pName Admin
 * @name UserMapper
 * @user HongWei
 * @date 2019/3/7
 * @desc
 */
package com.management.admin.repository;

import com.management.admin.entity.db.User;
import com.management.admin.entity.dbExt.UserDetailInfo;
import com.management.admin.entity.resp.MemberView;
import com.management.admin.entity.resp.UserAssetView;
import com.management.admin.entity.resp.UserProfileView;
import com.management.admin.entity.resp.UserView;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper extends MyMapper<User>{
    /**
     * 通过推广码查询会员身份信息
     * @param promotionCode
     * @return
     */
    @Select("SELECT * FROM tb_users WHERE promotion_code = #{promotionCode} LIMIT 1")
    User selectByPromotionCode(@Param("promotionCode") String promotionCode);


    /**
     * 查询用户推广的直销会员信息列表
     * @param userId
     * @return
     */
    @Select("SELECT * FROM tb_users WHERE parent_id = #{userId}")
    List<User> selectSubMembers(@Param("userId") Long userId);

    /**
     * 查询用户的团队会员信息列表
     * @param userId
     * @return
     * TODO: 此处使用的LIKE以及OR语法会造成索引失效, 影响查询速度, 及时改正
     */
    @Select("SELECT * FROM tb_users WHERE path LIKE '%${userId}%' AND parent_id != #{userId}")
    List<User> selectTeamMembers(@Param("userId") Long userId);

    /**
     * 查询交易主体信息
     * @param userId
     * @return
     */
    @Select("SELECT t1.*,t2.* FROM tb_users t1 LEFT JOIN tb_wallets t2 ON t2.user_id = t1.user_id WHERE t1.user_id =#{userId}")
    UserDetailInfo selectUserDetail(@Param("userId") String userId);

    @Select("SELECT t1.*,t2.* FROM tb_users t1 LEFT JOIN tb_wallets t2 ON t2.user_id = t1.user_id WHERE t1.user_id IN(${userId})")
    /**
     * 查询交易主体信息 韦德 2018年8月16日13:33:09
     * @param userId
     * @return
     */
    List<UserDetailInfo> selectInUid(@Param("userId") String userId);

    /**
     * 查询用户信息--根据account查询
     * @param account
     * @return
     */
    @Select("SELECT * FROM tb_users WHERE account = #{account}")
    User selectByAccount(@Param("account") String account);

    /**
     * 查询推广会员列表
     * @param userId
     * @return
     */
    @Select("SELECT * FROM tb_users WHERE path LIKE '%${userId}%'")
    List<User> selectPromoteMembers(@Param("userId") Long userId);

    /**
     * 晋级或忽略
     * @param userId
     * @param level
     * @return
     */
    @Update("UPDATE tb_users SET level = #{level}, edit_date = NOW() WHERE user_id = #{userId}")
    int updateLevel(@Param("userId") Long userId, @Param("level") Integer level);

    /**
     * 插入指定字段的数据
     * @param user
     * @return
     */
    @Insert("INSERT INTO tb_users(user_id, account, password, photo, nick_name, add_date, ip,  parent_id, path, promotion_code, yabo_token, yabo_password) " +
            "VALUES(#{userId}, #{account}, #{password}, #{photo}, #{nickName}, NOW(), #{ip}, #{parentId}, #{path}, #{promotionCode}, #{yaboToken}, #{yaboPassword})")
    int insertWithAccountPortion(User user);

    /**
     * 查询用户--根据用户id
     * @param userId
     * @return
     */
    @Select("SELECT * FROM tb_users WHERE user_id = #{userId} LIMIT 1")
    User selectById(@Param("userId") Long userId);

    @Update("UPDATE tb_users SET password=#{password},edit_date=NOW() WHERE phone=#{phone}")
    int resetPassword(@Param("phone") String phone, @Param("password") String encryptPassword);

    @Update("UPDATE tb_users SET photo=#{url}, edit_date=NOW() WHERE user_id = #{userId}")
    int updateAvatar(@Param("userId") Long userId, @Param("url") String url);

    @Update("UPDATE tb_users SET nick_name=#{nickName}, edit_date=NOW() WHERE user_id = #{userId}")
    int updateNickName(@Param("userId") Long userId, @Param("nickName") String nickName);

    @Update("UPDATE tb_users SET wechat_business_card=#{url}, edit_date=NOW() WHERE user_id = #{userId}")
    int updateWechatCard(@Param("userId") Long userId, @Param("url") String url);

    @Update("UPDATE tb_users SET account=#{username}, username=#{username}, edit_date=NOW() WHERE user_id = #{userId}")
    int updatePhoneAndAccount(@Param("userId")Long userId, @Param("username") String phone);

    @Select("SELECT * FROM tb_users WHERE username = #{username} LIMIT 1")
    User selectByPhone(@Param("username") String phone);

    @Update("UPDATE tb_users SET payment_password=#{password},edit_date=NOW() WHERE user_id = #{userId}")
    int changePaymentPassword(@Param("userId") Long userId, @Param("password") String encryptPassword);

    @Select("SELECT FORMAT(IFNULL(balance, 0.00),2) AS balance,\n" +
            "(SELECT COUNT(*) FROM `tb_orders` WHERE user_id = #{userId}  AND `status` = 2) AS playCount,\n" +
            "(SELECT FORMAT(IFNULL(SUM(amount), 0.00),2) FROM `tb_pays` WHERE to_uid = #{userId} AND trade_type = 10)  AS consumeCount\n" +
            "\tFROM `tb_wallets` WHERE user_id = #{userId} LIMIT 1")
    UserAssetView selectAssets(@Param("userId") Long userId);

    @Select("SELECT * FROM tb_users WHERE user_id =#{userId} AND payment_password = #{paymentPassword}")
    User selectByPaymentPassword(@Param("userId") Long userId, @Param("paymentPassword") String paymentPassword);

    @Select("SELECT *,\n" +
            "\t(SELECT COUNT(*) FROM tb_users WHERE parent_id = t1.user_id)  AS directSaleCount,\n" +
            "\t(SELECT COUNT(*) FROM tb_users WHERE path LIKE CONCAT('%',t1.user_id,'%') AND parent_id != t1.user_id)  AS channelSaleCount \n" +
            "FROM tb_users t1 WHERE ${condition} ORDER BY directSaleCount desc ,channelSaleCount DESC,add_date desc   LIMIT ${page},${size}")
    List<MemberView> selectPromoteMembersLimit(@Param("userId") Long userId, @Param("page") Integer page, @Param("size") String size, @Param("condition") String condition);

    @Update("UPDATE tb_users SET promotion_poster=#{url},edit_date=NOW() WHERE user_id=#{userId}")
    int updatePoster(@Param("userId") Long userId, @Param("url") String url);

    @Insert("INSERT INTO tb_users(user_id, account, open_id, open_token, password, photo, nick_name, add_date, ip, parent_id, type) " +
            "VALUES(#{userId}, #{account}, #{openId}, #{openToken}, #{password}, #{photo}, #{nickName}, NOW(), #{ip}, #{parentId}, 1)")
    int insertWithWxPortion(User user);

    @Update("UPDATE tb_users SET edit_date=NOW(), phone=#{phone} WHERE user_id=#{userId}")
    int bind(User user);

    @Select("SELECT * FROM tb_users WHERE open_id =#{openId} LIMIT 1")
    User selectByOpenId(@Param("openId") String openid);

    @Select("SELECT * FROM tb_users t1\n" +
            "LEFT JOIN tb_wallets t2 ON t2.user_id = t1.user_id\n" +
            "WHERE t1.user_id = #{userId} LIMIT 1")
    UserProfileView selectUserWalletById(@Param("userId") Long userId);

    @Select("SELECT t1.* FROM tb_users t1 " +
            "WHERE ${condition} ORDER BY t1.add_date DESC LIMIT #{page},${limit}")
    List<User> selectLimit(@Param("page") Integer page, @Param("limit") String limit
            , @Param("beginTime") String beginTime
            , @Param("endTime") String endTime
            , @Param("condition") String condition);


    @Select("SELECT COUNT(t1.user_id) FROM tb_users t1\n" +
            "WHERE ${condition} AND t1.is_delete = 0")
    Integer selectLimitCount(@Param("isEnable") Integer isEnable
            , @Param("beginTime") String beginTime
            , @Param("endTime") String endTime
            , @Param("condition") String condition);


    @Update("UPDATE tb_users SET ${condition} WHERE user_id = #{userId}")
    int updateProfile(@Param("condition") String condition, @Param("userId") Long userId);

    @Update("UPDATE tb_users SET is_delete = 1 WHERE user_id =#{userId}")
    int updateDelete(@Param("userId") Long userId);

    @Update("UPDATE tb_users SET password=#{password},edit_date=NOW() WHERE user_id = #{userId}")
    int changePassword(@Param("userId") Long userId,@Param("password") String encryptPassword);

    @Update("UPDATE tb_users SET yabo_token=#{token} WHERE user_id = #{userId}")
    int updateYaboToken(@Param("userId") Long userId, @Param("token") String token);

    @Update("UPDATE tb_users SET is_delete = 0 WHERE user_id =#{userId}")
    int unBlocking(@Param("userId") Long userId);

    @Select("SELECT COUNT(*) FROM `tb_users` WHERE `level` > 0")
    Integer selectAgentCount();

    @Select("SELECT COUNT(*) FROM `tb_users` t1 WHERE `level` > 0 AND ${condition} ORDER BY t1.add_date DESC LIMIT #{page},${limit}")
    Integer selectAgentLimitCount(@Param("isEnable") Integer isEnable
            , @Param("beginTime") String beginTime
            , @Param("endTime") String endTime
            , @Param("condition") String condition);

    @Select("SELECT *, (SELECT COUNT(*) FROM tb_users WHERE path LIKE CONCAT('%',t1.user_id,'%') LIMIT 1) AS childrenCount," +
            " (SELECT balance FROM tb_wallets WHERE user_id = t1.user_id LIMIT 1) AS balance FROM `tb_users` t1" +
            " WHERE t1.`level` > 0 AND ${condition} ORDER BY t1.add_date DESC LIMIT #{page},${limit}")
    List<UserView> selectAgentLimit(@Param("page") Integer page, @Param("limit") String limit
            , @Param("beginTime") String beginTime
            , @Param("endTime") String endTime
            , @Param("condition") String condition);
}
