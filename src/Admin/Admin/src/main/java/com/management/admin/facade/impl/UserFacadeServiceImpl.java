/***
 * @pName management
 * @name UserFacadeServiceImpl
 * @user HongWei
 * @date 2018/8/20
 * @desc
 */
package com.management.admin.facade.impl;

import com.management.admin.biz.IPayService;
import com.management.admin.biz.IPushMessageService;
import com.management.admin.biz.IUserService;
import com.management.admin.entity.db.PushMessage;
import com.management.admin.entity.db.User;
import com.management.admin.entity.enums.UserType;
import com.management.admin.entity.param.PayParam;
import com.management.admin.entity.template.Constant;
import com.management.admin.entity.template.DataDictionary;
import com.management.admin.exception.InfoException;
import com.management.admin.facade.IUserFacadeService;
import com.management.admin.utils.MD5Util;
import com.management.admin.utils.RandomUtils;
import com.management.admin.utils.yabo.YaBoLoginResp;
import com.management.admin.utils.yabo.YaBoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserFacadeServiceImpl implements IUserFacadeService {
    private final IUserService userService;
    private final IPayService payService;
    private final IPushMessageService pushMessageService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    public UserFacadeServiceImpl(IUserService userService, IPayService payService, IPushMessageService pushMessageService) {
        this.userService = userService;
        this.payService = payService;
        this.pushMessageService = pushMessageService;
    }

    /**
     * 注册用户 韦德 2018年8月20日15:39:37
     *
     * @param user
     * @param parentUser
     * @param smsCode
     * @return
     */
    @Override
    @Transactional
    public boolean registerWithPassword(User user, User parentUser, String smsCode) {
        /*String dbSmsCode = (String) redisTemplate.opsForValue().get("reg-sms-" + user.getPhone());
        if(dbSmsCode == null || !dbSmsCode.equalsIgnoreCase(smsCode)) throw new InfoException("短信验证码不正确");*/

        // 注册用户、开通钱包
        userService.registerWithPhone(user);

        // 计算推荐人奖金 TODO::消费时再分润
        //extractUserBonusComputed(user, parentUser);
        return true;
    }

    private void extractUserBonusComputed(User user, User parentUser) {
        Double amount = Double.valueOf(DataDictionary.Map.get("platform.vip.amount").getValue());
        if(amount <= 0) return;
        Double bonus = Double.valueOf(DataDictionary.Map.get("rate.zhixiaojiangjin").getValue());
        Double primaryAgent = Double.valueOf(DataDictionary.Map.get("rate.dailishangjiangjin").getValue());
        Double areaAgent = Double.valueOf(DataDictionary.Map.get("rate.quyudailijiangjin").getValue());
        Double highAgent = Double.valueOf(DataDictionary.Map.get("rate.gaojidailijiangjin").getValue());
        Double shareholder = Double.valueOf(DataDictionary.Map.get("rate.gudongdailijiangjin").getValue());
        Double primaryAgentDiff = Double.valueOf(DataDictionary.Map.get("rate.tj.chujidaili").getValue());
        Double areaAgentDiff = Double.valueOf(DataDictionary.Map.get("rate.tj.quyudaili").getValue());
        Double highAgentDiff = Double.valueOf(DataDictionary.Map.get("rate.tj.gaojidaili").getValue());
        Double shareholderDiff = Double.valueOf(DataDictionary.Map.get("rate.tj.gudong").getValue());

        UserType userType = computedBonus(parentUser);
        Double bonusAmount = Math.floor(amount * bonus);//88*0.4322
        String bonusRemark = "推荐用户" + user.getAccount() + "销售奖金";
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

    /**
     * 微信公众号注册用户 韦德 2019年3月12日16:07:37
     *
     * @param openid
     * @param access_token
     * @param nickname
     * @param headimgurl
     * @return
     */
    @Override
    public User registerWithWx(String openid, String access_token, String nickname, String headimgurl, String userIp) {
        User dbUser = userService.getUserByOpenId(openid);
        if(dbUser != null){
            return dbUser;
        }

        User user = new User();
        user.setPassword(MD5Util.md5(openid));
        user.setIp(userIp);
        user.setAddDate(new Date());
        user.setPromotionCode(RandomUtils.generateUpperString(8));
        /*user.setParentId(parentUser.getUserId());
        user.setPath(parentUser.getUserId() + "," + parentUser.getPath());*/
        user.setAccount(user.getPhone());
        user.setNickName(nickname);
        user.setPhoto(headimgurl);
        user.setType(1);
        user.setOpenId(openid);
        user.setOpenToken(access_token);
        // 注册用户、开通钱包
        userService.registerWithWx(user);
        return user;
    }

    @Override
    public void bind(User user, User parentUser, String smsCode) {
       userService.bind(user);
    }

    @Override
    @Transactional
    public User login(String username, String password, String userIp) {
        User user = userService.loginByUsername(username, password);
        //用户在亚博平台注册, 到我们平台登录, 此时给用户进行无感自动注册
        syncAccount(username, password, userIp, user);
        user = userService.loginByUsername(username, password);
        return user;
    }

    private void syncAccount(String username, String password, String userIp, User user) {
        if(user == null) {
            YaBoLoginResp login = YaBoUtil.login(username, password);
            if(login == null) throw new InfoException("亚博登录次数过多, 请稍后重试!");

            if (login.getStatus().equals("success") && login.getData() != null && login.getData().getId() > 0) {
                //用户在亚博平台存在, 进行无感自动注册
                User regUser = new User();
                regUser.setPhone(username);
                regUser.setPassword(password);
                regUser.setIp(userIp);
                regUser.setAddDate(new Date());
                regUser.setPromotionCode(RandomUtils.generateUpperString(8));
                regUser.setAccount(username);
                regUser.setNickName(username);
                regUser.setPhoto(Constant.UserPhoto);
                regUser.setYaboToken(login.getData().getToken());
                regUser.setYaboPassword(password);
                boolean result = this.registerWithPhone(regUser);
                if (!result) {
                    throw new InfoException("同步信息失败");
                }
            } else {
                throw new InfoException(login.getMessage());
            }
        }else{
            //更新token
            YaBoLoginResp login = YaBoUtil.login(username, password);
            if(login != null && login.getStatus().equals("success")){
                if(!userService.changeYaBoToken(user.getUserId(), login.getData().getToken())){
                    throw new InfoException("登录令牌已失效");
                }
            }else {
                throw new InfoException(login.getMessage());
            }
        }
    }

    @Override
    @Transactional
    public User loginWithPhone(String phone, String username, String password, String userIp) {
        User user = userService.loginWithPhone(phone, username, password);
        //用户在亚博平台注册, 到我们平台登录, 此时给用户进行无感自动注册
        syncAccount(username, password, userIp, user);
        user = userService.loginWithPhone(phone, username, password);
        return user;
    }

    @Override
    public boolean registerWithPhone(User regUser) {
        // 注册用户、开通钱包
        userService.registerWithPhone(regUser);
        return true;
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

        PushMessage pushMessage = new PushMessage();
        pushMessage.setMessage("您有一笔" + bonusAmount + "元奖金转入钱包");
        pushMessage.setUserId(userId);
        pushMessage.setToUrl("bill");
        pushMessageService.sendMessage(pushMessage);
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
