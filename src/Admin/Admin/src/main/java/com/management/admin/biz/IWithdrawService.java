/***
 * @pName management
 * @name WithdrawService
 * @user HongWei
 * @date 2018/8/21
 * @desc
 */
package com.management.admin.biz;

import com.management.admin.entity.db.Withdraw;
import com.management.admin.entity.dbExt.WithdrawDetailInfo;
import com.management.admin.entity.resp.GroupView;
import com.management.admin.entity.resp.WithdrawView;

import java.util.List;

public interface IWithdrawService extends IBaseService<Withdraw> {

    /**
     * 申请提现 韦德 2018年8月21日10:57:26
     * @param token
     * @param amount
     * @param systemRecordId
     */
    void addWithdraw(String token, Double withdrawAmount, Double amount, Long systemRecordId);

    /**
     * 根据id查询提现信息 韦德 2018年8月21日13:39:01
     * @param withdraw
     * @return
     */
    Withdraw getWithdrawById(Withdraw withdraw);

    /**
     * 分页加载 韦德 2018年8月30日11:29:00
     * @param page
     * @param limit
     * @param condition
     * @param state
     * @param beginTime
     * @param endTime
     * @return
     */
    List<WithdrawDetailInfo> getWithdrawLimit(Integer page, String limit, String condition, Integer state, String beginTime, String endTime);

    /**
     * 加载总记录数 韦德 2018年8月30日11:29:11
     * @return
     */
    Integer getWithdrawCount();

    /**
     * 加载分页记录数 韦德 2018年8月30日11:29:22
     * @param condition
     * @param state
     * @param beginTime
     * @param endTime
     * @return
     */
    Integer getWithdrawLimitCount(String condition, Integer state, String beginTime, String endTime);


    /**
     * 拒绝审批 韦德 2018年8月30日15:18:45
     * @param withdraw
     */
    boolean pass(Withdraw withdraw);

    /**
     * 获取最新记录
     * @return
     */
    Withdraw getNewRecord();

    List<GroupView> getUserWithdrawLimit(Long userId, Integer page);

    Integer getUserWithdrawTotalPage(Long userId);

    void addWithdraw(Long userId, Double withdrawAmount, String financeId, String financeName, Double amount, long systemRecordId);

    void receipt(Integer id, String order_id);
}
