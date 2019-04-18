/***
 * @pName management
 * @name PayService
 * @user HongWei
 * @date 2018/8/16
 * @desc
 */
package com.management.admin.biz;

import com.management.admin.entity.db.Accounts;
import com.management.admin.entity.db.Pays;
import com.management.admin.entity.db.Wallet;
import com.management.admin.entity.param.PayParam;
import com.management.admin.entity.resp.GroupView;
import com.management.admin.entity.resp.MemberView;
import com.management.admin.entity.resp.PaysView;
import com.management.admin.entity.resp.UserResp;

import java.util.List;

public interface IPayService extends IBaseService<Pays> {
    /**
     * 转账 韦德 2018年8月16日13:17:17
     * @param payParam
     */
    void transfer(PayParam payParam);

    /**
     * 奖金 韦德 2019年3月8日03:29:20
     * @param payParam
     */
    void bonus(PayParam payParam);


    /**
     * 批量转账 韦德 2018年8月21日01:53:25
     * @param payParams
     */
    void batchTransfer(List<PayParam> payParams);

    /**
     * 提现 韦德 2018年8月21日13:42:56
     * @param payParam
     */
    Long withdraw(PayParam payParam);

    /**
     * 消费 韦德 2018年8月16日13:17:17
     *
     * @param payParam
     */
    void consume(PayParam payParam);

    /**
     * 批量消费 韦德 2018年8月21日01:53:25
     * @param payParams
     */
    void batchConsume(List<PayParam> payParams);

    /**
     * 充值 韦德 2018年8月16日13:17:17
     *
     * @param payParam
     */
    Long recharge(PayParam payParam);

    /**
     * 获取不可用余额 韦德 2018年8月21日15:50:04
     * @param userId
     * @return
     */
    Double getNotWithdrawAmount(Long userId);

    /**
     * 获取可用余额 韦德 2018年8月21日15:50:04
     * @param userId
     * @return
     */
    Double getWithdrawAmount(Long userId);

    /**
     * 分页加载财务会计账目数据列表 韦德 2018年8月27日00:20:54
     * @param page
     * @param limit
     * @param condition
     * @param trade_type
     * @param trade_date_begin
     * @param trade_date_end
     * @return
     */
    List<Accounts> getAccountsLimit(Integer page, String limit, String condition, Integer trade_type, Integer filter_type, String trade_date_begin, String trade_date_end);

    /**
     * 统计分页加载财务会计账目数据列表
     * @param condition
     * @param trade_type
     * @param trade_date_begin
     * @param trade_date_end
     * @return
     */
    int getAccountsLimitCount(String condition, Integer trade_type, Integer filter_type, String trade_date_begin, String trade_date_end);

    /**
     * 加载财务会计账目数据总条数 韦德 2018年8月27日00:21:16
     * @return
     */
    int getAccountsCount();

    /**
     * 统计系统账户余额以及交易额 韦德 2018年8月27日11:21:35
     * @param systemAccountsId
     * @return
     */
    UserResp getAccountAmount(Long systemAccountsId);

    /**
     * 取账户的总收入与总支出情况的实时数据 韦德 2018年8月7日00:43:31
     * @param id
     * @return
     */
    UserResp getAccountAmountForDB(Long id);

    /**
     * 获取账户的总收入与总支出情况的缓存 韦德 2018年8月7日00:43:31
     * @param id
     * @return
     */
    UserResp getAccountAmountForCache(Long id);

    /**
     * 分页加载交易流水 韦德 2018年8月27日21:55:17
     * @param page
     * @param limit
     * @param condition
     * @param trade_type
     * @param trade_date_begin
     * @param trade_date_end
     * @return
     */
    List<Pays> getPaysLimit(Integer page, String limit, String condition, Integer trade_type, Integer filter_type, String trade_date_begin, String trade_date_end);

    /**
     * 加载交易流水数据总条数 韦德 2018年8月27日21:55:32
     * @return
     */
    int getPaysCount();

    /**
     * 加载交易流水数据分页总条数 韦德 2018年8月27日22:37:10
     * @param condition
     * @param trade_type
     * @param trade_date_begin
     * @param trade_date_end
     * @return
     */
    Integer getPaysLimitCount(String condition, Integer trade_type, Integer filter_type, String trade_date_begin, String trade_date_end);

    /**
     * 充值 韦德 2018年8月7日03:05:53
     * @param id
     * @param amount
     */
    Boolean recharge(Long id, Double amount);

    /**
     * 更新交易回执单号 韦德 2018年8月30日14:54:32
     * @param systemRecordId
     * @param channelRecordId
     * @return
     */
    int updateChannelRecordId(Long systemRecordId, String channelRecordId);

    List<GroupView> getTransLimit(Long userId, Integer page);

    Integer getTransTotalPage(Long userId);

    Wallet getBalance(Long userId);

    long refund(PayParam payParam);
}
