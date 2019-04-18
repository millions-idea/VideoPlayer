/***
 * @pName management
 * @name WithdrawMapper
 * @user HongWei
 * @date 2018/8/21
 * @desc
 */
package com.management.admin.repository;

import com.management.admin.entity.db.Withdraw;
import com.management.admin.entity.dbExt.WithdrawDetailInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface WithdrawMapper extends MyMapper<Withdraw> {
    @Select("SELECT t1.*, t2.account, t2.finance_id AS financeId, t2.finance_name AS financeName FROM tb_withdraw t1 " +
            "LEFT JOIN tb_users t2 ON t1.user_id = t2.user_id " +
            "WHERE ${condition} ORDER BY t1.add_time DESC LIMIT #{page},${limit}")
    /**
     * 分页查询 韦德 2018年8月30日11:33:22
     * @param page
     * @param limit
     * @param state
     * @param beginTime
     * @param endTime
     * @param where
     * @return
     */
    List<WithdrawDetailInfo> selectLimit(@Param("page") Integer page, @Param("limit") String limit
            , @Param("state") Integer state
            , @Param("beginTime") String beginTime
            , @Param("endTime") String endTime
            , @Param("condition") String condition);

    @Select("SELECT COUNT(t1.withdraw_id) FROM tb_withdraw t1\n" +
            "LEFT JOIN tb_users t2 ON t1.user_id = t2.user_id \n" +
            "WHERE ${condition}")
    /**
     * 分页查询记录数 韦德 2018年8月30日11:33:30
     * @param state
     * @param beginTime
     * @param endTime
     * @param where
     * @return
     */
    Integer selectLimitCount(@Param("state") Integer state
            , @Param("beginTime") String beginTime
            , @Param("endTime") String endTime
            , @Param("condition") String condition);

    @Select("SELECT * FROM tb_withdraw WHERE state = 0 LIMIT 1;")
    /**
     * 查询最新记录
     */
    Withdraw selectNewRecord();

    @Select("SELECT * FROM tb_withdraw WHERE user_id = #{userId} ORDER BY add_time DESC LIMIT ${page}, ${size}")
    List<Withdraw> selectUserWithdrawLimit(@Param("userId") Long userId, @Param("page") Integer page, @Param("size") String size);

    @Select("SELECT * FROM tb_withdraw WHERE user_id = #{userId} ORDER BY add_time DESC")
    List<Withdraw> selectUserWithdraw(@Param("userId") Long userId);

    @Update("UPDATE tb_withdraw SET state=1, channel_record_id=#{channelId}, update_time=NOW() WHERE withdraw_id = #{id}")
    int updateState(@Param("id") Integer id, @Param("channelId") String channelId);
}
