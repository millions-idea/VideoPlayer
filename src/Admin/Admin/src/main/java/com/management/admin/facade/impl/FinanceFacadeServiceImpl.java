/***
 * @pName management
 * @name FinanceFacadeServiceImpl
 * @user HongWei
 * @date 2018/8/21
 * @desc
 */
package com.management.admin.facade.impl;

import com.management.admin.biz.*;
import com.management.admin.entity.db.*;
import com.management.admin.entity.enums.UserType;
import com.management.admin.entity.param.PayParam;
import com.management.admin.entity.resp.UserProfileView;
import com.management.admin.entity.template.Constant;
import com.management.admin.entity.template.DataDictionary;
import com.management.admin.entity.template.JsonResult;
import com.management.admin.exception.InfoException;
import com.management.admin.exception.MsgException;
import com.management.admin.facade.IFinanceFacadeService;
import com.management.admin.utils.MD5Util;
import com.management.admin.utils.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FinanceFacadeServiceImpl implements IFinanceFacadeService {
    @Autowired
    private IWithdrawService withdrawService;
    @Autowired
    private IPayService payService;
    @Autowired
    private IRechargeService rechargeService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IOrderService orderService;

    /**
     * 申请提现 韦德 2018年8月21日10:57:26
     *
     * @param token
     * @param amount
     */
    @Override
    @Transactional
    public void addWithdraw(String token, Double amount) {
        if(amount == null || amount == 0) return;

        // 加载用户信息
        Map<String, String> map = TokenUtil.validate(token);
        if(map.isEmpty()) JsonResult.failing();
        String userId = map.get("userId");
        if(userId == null || userId.isEmpty()) throw new MsgException("身份校验失败");
        Double withdrawAmount = payService.getWithdrawAmount(Long.valueOf(userId));
        // 转账
        PayParam payParam = new PayParam();
        payParam.setFromUid(Long.valueOf(userId));
        payParam.setAmount(amount);
        payParam.setToUid(Constant.SYSTEM_ACCOUNTS_ID);
        long  systemRecordId = payService.withdraw(payParam);
        withdrawService.addWithdraw(token,withdrawAmount, amount, systemRecordId);
    }

    /**
     * 提现审批 韦德 2018年8月21日10:42:23
     *
     * @param withdraw
     * @return
     */
    @Override
    @Transactional
    public boolean confirmWithdraw(Withdraw withdraw) {
        // 1、更新提现审批表
        Withdraw model = withdrawService.getWithdrawById(withdraw);
        if(model == null) throw new MsgException("审批请求不存在");

        /*PayParam payParam = new PayParam();
        payParam.setFromUid(model.getUserId());
        payParam.setAmount(withdraw.getAmount());
        payParam.setToUid(Constant.SYSTEM_ACCOUNTS_ID);
        long  systemRecordId = payService.withdraw(payParam);*/

        model.setUpdateTime(new Date());
        model.setState(1);
        model.setRemark(withdraw.getRemark());
        model.setSystemRecordId(withdraw.getSystemRecordId());
        model.setChannelRecordId(withdraw.getChannelRecordId());
        int count = withdrawService.update(model);
        if(count == 0) throw new MsgException("编辑提现状态失败");

        // 2、更新财务日志
        count = 0;
        count = payService.updateChannelRecordId(withdraw.getSystemRecordId(),  withdraw.getChannelRecordId());
        if(count == 0) throw new MsgException("更新交易回执失败");

        withdraw.setUserId(model.getUserId());
        return true;
    }

    /**
     * 充值审批 韦德 2018年8月21日10:42:23
     *
     * @param recharge
     * @return
     */
    @Override
    @Transactional
    public boolean confirmRecharge(Recharge recharge) {
        Recharge model = rechargeService.getById(recharge);
        if(model == null) throw new MsgException("审批请求不存在");

        // 1、插入交易流水
        PayParam payParam = new PayParam();
        payParam.setFromUid(Constant.SYSTEM_ACCOUNTS_ID);
        payParam.setAmount(recharge.getAmount());
        payParam.setToUid(model.getUserId());
        long  systemRecordId = payService.recharge(payParam);

        // 2、更新提现审批表
        model.setUpdateTime(new Date());
        model.setState(1);
        model.setRemark(recharge.getRemark());
        model.setSystemRecordId(systemRecordId);
        model.setChannelRecordId(recharge.getChannelRecordId());
        int count = rechargeService.update(model);
        if(count == 0) throw new MsgException("编辑审批状态失败");


        // 3、更新财务日志
        count = 0;
        count = payService.updateChannelRecordId(systemRecordId,  model.getChannelRecordId());
        if(count == 0) throw new MsgException("更新交易回执失败");

        recharge.setUserId(model.getUserId());
        return true;
    }

    /**
     * 充值 韦德 2018年8月31日18:14:50
     *
     * @param token
     * @param amount
     */
    @Override
    @Transactional
    public void addRecharge(String token, Double amount) {
        // 加载用户信息
        Map<String, String> map = TokenUtil.validate(token);
        if(map.isEmpty()) JsonResult.failing();
        String userId = map.get("userId");
        if(userId == null || userId.isEmpty()) throw new MsgException("身份校验失败");

        // 转账

        // 2、插入交易流水
        PayParam payParam = new PayParam();
        payParam.setFromUid(Constant.SYSTEM_ACCOUNTS_ID);
        payParam.setAmount(amount);
        payParam.setToUid(Long.valueOf(userId));
        long  systemRecordId = payService.recharge(payParam);

    }

    /**
     * 金币充值 2019年3月8日19:50:18
     *
     * @param userId
     * @param amount
     */
    @Override
    public Long recharge(Long userId, Double amount) {
        PayParam payParam = new PayParam();
        payParam.setFromUid(Constant.SYSTEM_ACCOUNTS_ID);
        payParam.setAmount(amount);
        payParam.setToUid(Long.valueOf(userId));
        long systemRecordId = payService.recharge(payParam);
        return systemRecordId;
    }


    public Long recharge(Long id, Long userId, Double amount) {
        PayParam payParam = new PayParam();
        payParam.setSystemRecordId(id);
        payParam.setFromUid(Constant.SYSTEM_ACCOUNTS_ID);
        payParam.setAmount(amount);
        payParam.setToUid(Long.valueOf(userId));
        long systemRecordId = payService.recharge(payParam);
        return systemRecordId;
    }

    @Override
    @Transactional
    public void apply(String account, Long userId, Double amount, String financeId, String realName, String paymentPassword) {
        User user = userService.getUserById(userId);

        if(!user.getPaymentPassword().equalsIgnoreCase(MD5Util.md5(account + paymentPassword))) throw new MsgException("支付密码输入有误");

        Wallet wallet = payService.getBalance(userId);

        amount = amount + 2;

        if(wallet.getBalance() <= 0 || (amount > wallet.getBalance())) throw new MsgException("余额不足");

        // 转账
        PayParam payParam = new PayParam();
        payParam.setFromUid(Long.valueOf(userId));
        payParam.setAmount(amount);
        payParam.setToUid(Constant.SYSTEM_ACCOUNTS_ID);
        long  systemRecordId = payService.withdraw(payParam);

        withdrawService.addWithdraw(userId, amount - 2,financeId, realName, wallet.getBalance(), systemRecordId);
    }

    @Override
    @Transactional
    public boolean payment(Long userId, Long orderId, String channel) {
        UserProfileView user = userService.getUserWalletById(userId);

        Order order = orderService.getOrder(orderId);

        if(order == null) throw new InfoException("订单不存在");
        if(!order.getStatus().equals(0)) throw new InfoException("订单状态有误");

        if(channel.contains("balance") && user.getBalance() < order.getAmount()) throw new InfoException("余额不足");

        boolean result = orderService.finishOrder(orderId, channel);

        if(!result) throw new InfoException("更新订单状态失败");

        PayParam payParam = new PayParam();
        //扣用户钱
        payParam.setFromUid(userId);
        payParam.setAmount(order.getAmount());
        payParam.setToUid(Constant.SYSTEM_ACCOUNTS_ID);

        //如果是在线充值, 商品id为固定值0
        if(order.getProductId().equals(0)){
            payParam.setRemark("在线充值");
        }else{
            payParam.setRemark("购买商品-" + order.getProductName());
        }

        if(channel.equalsIgnoreCase("alipay")){
            payParam.setChannelType(DataDictionary.Map.get("finance.pays.channel.alipay").getDictionaryId());
        }else if(channel.equalsIgnoreCase("wxpay")){
            payParam.setChannelType(DataDictionary.Map.get("finance.pays.channel.wechat").getDictionaryId());
        }else{
            payParam.setChannelType(DataDictionary.Map.get("finance.pays.channel.internal").getDictionaryId());
        }
        payService.consume(payParam);

        //返佣金-判断是不是会员
        /*if(user != null && !user.getType().equals(0)){
            payParam = new PayParam();
            payParam.setFromUid(Constant.SYSTEM_ACCOUNTS_ID);
            payParam.setAmount(order.getReturnAmount());
            payParam.setToUid(Long.valueOf(userId));
            payParam.setRemark("商品佣金");
            payService.bonus(payParam);
        }*/

        //发货
        orderService.deliver(orderId);

        //分销分润
        User parentUser = userService.getUserById(user.getParentId());
        if(parentUser != null){
            extractUserBonusComputed(order.getAmount(), user, parentUser);
        }
        return true;
    }


    private void extractUserBonusComputed(Double amount, UserProfileView user, User parentUser) {
        if(amount <= 0) return;
        Double bonus = Double.valueOf(DataDictionary.Map.get("rate.zhixiaojiangjin").getValue());
        Double primaryAgent = Double.valueOf(DataDictionary.Map.get("rate.dailishangjiangjin").getValue());
        Double areaAgent = Double.valueOf(DataDictionary.Map.get("rate.quyudailijiangjin").getValue());
        Double highAgent = Double.valueOf(DataDictionary.Map.get("rate.gaojidailijiangjin").getValue());
        Double shareholder = Double.valueOf(DataDictionary.Map.get("rate.gudongjiangjin").getValue());
        Double primaryAgentDiff = Double.valueOf(DataDictionary.Map.get("rate.tj.chujidaili").getValue());
        Double areaAgentDiff = Double.valueOf(DataDictionary.Map.get("rate.tj.quyudaili").getValue());
        Double highAgentDiff = Double.valueOf(DataDictionary.Map.get("rate.tj.gaojidaili").getValue());
        Double shareholderDiff = Double.valueOf(DataDictionary.Map.get("rate.tj.gudong").getValue());

        UserType userType = computedBonus(parentUser);
        Double bonusAmount = Math.floor(amount * bonus);//88*0.4322
        String bonusRemark = "推荐用户" + user.getAccount() + "消费奖金";
        switch (userType) {
            case VipUser:
                //直销奖金
                pushBonus(parentUser.getUserId(), bonusAmount, bonusRemark);
                break;
            case PrimaryAgent:
                //直销奖金+初级代理商奖金
                bonusAmount += Math.floor(amount * primaryAgent);//88*0.1525
                pushBonus(parentUser.getUserId(), bonusAmount, bonusRemark);
                break;
            case AreaAgent:
                //直销奖金+区域代理商奖金
                bonusAmount += Math.floor(amount * areaAgent);//88*0.2118
                pushBonus(parentUser.getUserId(), bonusAmount, bonusRemark);
                break;
            case HighAgent:
                //直销奖金+高级代理商奖金
                bonusAmount += Math.floor(amount * highAgent);//88*0.2542
                pushBonus(parentUser.getUserId(), bonusAmount, bonusRemark);
                break;
            case Shareholder:
                //直销奖金+股东奖金
                bonusAmount += Math.floor(amount * shareholder);//88*0.3050
                pushBonus(parentUser.getUserId(), bonusAmount, bonusRemark);
                break;
        }

        UserType peerUserType = computedPeerBonus(parentUser);
        bonusRemark = "市场" + user.getAccount() + "同级奖金";
        switch (peerUserType) {
            case PrimaryAgent:
                bonusAmount = amount * primaryAgentDiff;
                pushBonus(getTopUserId(parentUser), bonusAmount, bonusRemark);
                break;
            case AreaAgent:
                bonusAmount = amount * areaAgentDiff;
                pushBonus(getTopUserId(parentUser), bonusAmount, bonusRemark);
                break;
            case HighAgent:
                bonusAmount = amount * highAgentDiff;
                pushBonus(getTopUserId(parentUser), bonusAmount, bonusRemark);
                break;
            case Shareholder:
                bonusAmount = amount * shareholderDiff;
                pushBonus(getTopUserId(parentUser), bonusAmount, bonusRemark);
                break;
        }

        // 晋级或忽略
        changeLevel(parentUser.getUserId(), userType);
    }

    @Override
    public boolean recharge(String tradeNo, Long systemId, Long userId, Double amount, String channel) {
        PayParam payParam = new PayParam();
        payParam.setSystemRecordId(systemId);
        payParam.setFromUid(Constant.SYSTEM_ACCOUNTS_ID);
        payParam.setAmount(amount);
        payParam.setToUid(Long.valueOf(userId));
        payParam.setChannelRecordId(tradeNo);
        payParam.setToAccountTime(new Date());
        payParam.setChannel(channel);
        payService.recharge(payParam);
        return true;
    }

    @Override
    @Transactional
    public void buyVip(Long userId, Long aLong, String alipay) {
        UserProfileView user = userService.getUserWalletById(userId);
        if(user.getLevel() > 0) throw new InfoException("您已经是VIP会员了");
        Double amount = Double.valueOf(DataDictionary.Map.get("platform.vip.amount").getValue());

        if(user.getBalance() < amount) throw new InfoException("余额不足");

        if(amount == 0) {
            //免费加入会员
            userService.changeLevel(userId, 1);
            return;
        }
        PayParam payParam = new PayParam();
        payParam.setFromUid(userId);
        payParam.setAmount(amount);
        payParam.setToUid(Constant.SYSTEM_ACCOUNTS_ID);
        payParam.setRemark("充值会员");
        payService.consume(payParam);
        userService.changeLevel(userId, 1);
    }


    @Override
    @Transactional
    public void reApply(Withdraw withdraw) {
        Double amount = withdraw.getAmount() + 2;

        // 转账
        PayParam payParam = new PayParam();
        payParam.setFromUid(Constant.SYSTEM_ACCOUNTS_ID);
        payParam.setAmount(amount);
        payParam.setToUid(withdraw.getUserId());
        long systemRecordId = payService.refund(payParam);

        if(systemRecordId <= 0) throw new InfoException("退款失败");

        boolean pass = withdrawService.pass(withdraw);
        if(!pass) throw new InfoException("拒审失败");
    }


    /**
     * 获取最高层级的推荐人id
     * @param parentUser
     * @return
     */
    private Long getTopUserId(User parentUser){
        String path = parentUser.getPath();
        if(path.length() == 0 || path.isEmpty()) return -10000L;
        String[] uidArr = path.split(",");
        Long topUserId = uidArr.length == 1 ? Long.valueOf(uidArr[uidArr.length - 1]) : Long.valueOf(uidArr[uidArr.length]);
        return topUserId;
    }

    /**
     * 晋级或忽略
     * @param userId
     * @param userType
     */
    private void changeLevel(Long userId, UserType userType) {
        Integer level = 0;
        switch (userType){
            case PrimaryAgent:
                level = 2;
                break;
            case AreaAgent:
                level = 3;
                break;
            case HighAgent:
                level = 4;
                break;
            case Shareholder:
                level = 5;
                break;
        }
        if(level > 0){
            userService.changeLevel(userId, level);
        }
    }

    /**
     * 计算同级奖金
     * @param parentUser
     * @return
     */
    private UserType computedPeerBonus(User parentUser) {
        //判断当前用户的等级
        switch (parentUser.getLevel()){
            case 0:
                return UserType.NormalUser;
            case 1:
                return UserType.VipUser;
            case 2:
                return UserType.PrimaryAgent;
            case 3:
                return UserType.AreaAgent;
            case 4:
                return UserType.HighAgent;
            case 5:
                return UserType.Shareholder;
        }
        return UserType.NormalUser;
    }

    /**
     * 发放销售奖金
     * @param userId
     * @param bonusAmount
     * @param bonusRemark
     */
    private void pushBonus(Long userId, Double bonusAmount, String bonusRemark) {
        if(userId < 0) {
            return;
        }
        PayParam payParam = new PayParam();
        payParam.setFromUid(Constant.SYSTEM_ACCOUNTS_ID);
        payParam.setAmount(bonusAmount);
        payParam.setToUid(userId);
        payParam.setRemark(bonusRemark);
        payParam.setCurrency(1);
        payService.bonus(payParam);
    }

    /**
     * 计算销售奖金
     * @param parentUser
     * @return
     */
    private UserType computedBonus(User parentUser) {
        //查询我推广的所有会员-耗时操作
        List<User> promoteMembers = userService.getPromoteMembers(parentUser.getUserId());

        //筛选我的直推会员
        List<User> directMembers = promoteMembers.stream().filter(item -> item.getParentId().equals(parentUser.getUserId())).collect(Collectors.toList());

        //筛选我的团队会员
        List<User> teamMembers = promoteMembers.stream().filter(item -> !item.getParentId().equals(parentUser.getUserId())).collect(Collectors.toList());

        //计算得出销售冠军是哪位会员
        Map<Long, Long> countMap = promoteMembers.stream().collect(Collectors.groupingBy(p -> p.getParentId(), Collectors.counting()));
        List<User> smallMarket = new ArrayList<>();
        if(countMap.size() > 0){
            Map.Entry<Long, Long> maxMarket = countMap.entrySet().stream().max(Comparator.comparing(u -> u.getValue())).get();
            //计算得出小市场会员
            smallMarket = promoteMembers.stream().filter(tm -> !tm.getParentId().equals(maxMarket.getKey())).collect(Collectors.toList());
        }

        //初级代理
        if((directMembers != null && directMembers.size() > 5) && (teamMembers != null && teamMembers.size() > 50)){
            return UserType.PrimaryAgent;
        }else if((directMembers != null && directMembers.size() > 10)
                && (teamMembers != null && teamMembers.size() > 180)
                && (smallMarket != null && smallMarket.size() > 20)){
            //区域代理
            return UserType.AreaAgent;
        }else if((directMembers != null && directMembers.size() > 20)
                && (teamMembers != null && teamMembers.size() > 560)
                && (smallMarket != null && smallMarket.size() > 50)){
            //高级代理
            return UserType.HighAgent;
        }else if((directMembers != null && directMembers.size() > 30)
                && (teamMembers != null && teamMembers.size() > 1680)
                && (smallMarket != null && smallMarket.size() > 160)){
            //事业股东
            return UserType.Shareholder;
        }else{
            return UserType.VipUser;
        }
    }
}
