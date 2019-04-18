/***
 * @pName management
 * @name FinanceFacadeService
 * @user HongWei
 * @date 2018/8/21
 * @desc
 */
package com.management.admin.facade;

import com.management.admin.entity.db.Recharge;
import com.management.admin.entity.db.Withdraw;

public interface IFinanceFacadeService {

    /**
     * 申请提现 韦德 2018年8月21日10:57:26
     *
     * @param token
     * @param amount
     */
    void addWithdraw(String token, Double amount);


    /**
     * 提现审批 韦德 2018年8月21日10:42:23
     * @return
     */
    boolean confirmWithdraw(Withdraw withdraw);


    /**
     * 充值审批 韦德 2018年8月21日10:42:23
     * @return
     */
    boolean confirmRecharge(Recharge recharge);

    /**
     * 充值 韦德 2018年8月31日18:14:50
     * @param token
     * @param amount
     */
    void addRecharge(String token, Double amount);

    /**
     * 金币充值 2019年3月8日19:50:18
     * @param userId
     * @param amount
     */
    Long recharge(Long userId, Double amount);

    Long recharge(Long id, Long userId, Double amount);

    void apply(String account, Long userId, Double amount, String financeId, String realName, String paymentPassword);

    boolean payment(Long userId, Long orderId, String channel);

    boolean recharge(String tradeNo, Long systemId, Long userId, Double amount, String channel);

    void buyVip(Long userId, Long aLong, String alipay);

    void reApply(Withdraw withdraw);
}
