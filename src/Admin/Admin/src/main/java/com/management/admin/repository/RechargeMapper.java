/***
 * @pName management
 * @name WithdrawMapper
 * @user HongWei
 * @date 2018/8/21
 * @desc
 */
package com.management.admin.repository;

import com.management.admin.entity.db.Recharge;
import com.management.admin.entity.dbExt.RechargeDetailInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RechargeMapper extends MyMapper<Recharge> {
    @Select("SELECT t1.*, t2.account FROM tb_recharge t1 " +
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
    List<RechargeDetailInfo> selectLimit(@Param("page") Integer page, @Param("limit") String limit
            , @Param("state") Integer state
            , @Param("beginTime") String beginTime
            , @Param("endTime") String endTime
            , @Param("condition") String condition);

    @Select("SELECT COUNT(t1.recharge_id) FROM tb_recharge t1\n" +
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

    @Select("SELECT COUNT(*) FROM tb_recharge WHERE user_id = #{userId} AND state = 0")
    Integer selectNotExamine(@Param("userId") Long userId);
}
