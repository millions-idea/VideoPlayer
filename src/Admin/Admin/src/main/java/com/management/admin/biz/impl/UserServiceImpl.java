/***
 * @pName Admin
 * @name UserServiceImpl
 * @user HongWei
 * @date 2019/3/7
 * @desc
 */
package com.management.admin.biz.impl;

import com.management.admin.biz.IUserService;
import com.management.admin.entity.db.Order;
import com.management.admin.entity.db.User;
import com.management.admin.entity.db.Wallet;
import com.management.admin.entity.dbExt.UserDetailInfo;
import com.management.admin.entity.param.UserParam;
import com.management.admin.entity.resp.*;
import com.management.admin.entity.template.Constant;
import com.management.admin.exception.InfoException;
import com.management.admin.exception.MsgException;
import com.management.admin.repository.*;
import com.management.admin.repository.utils.ConditionUtil;
import com.management.admin.utils.*;
import com.management.admin.utils.yabo.YaBoResp;
import com.management.admin.utils.yabo.YaBoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements IUserService {
    private final UserMapper userMapper;
    private final WalletMapper walletMapper;
    private final PayMapper payMapper;
    private final RedisTemplate redisTemplate;
    private final OrderMapper orderMapper;
    @Autowired
    public UserServiceImpl(UserMapper userMapper, WalletMapper walletMapper, PayMapper payMapper, RedisTemplate redisTemplate, OrderMapper orderMapper) {
        this.userMapper = userMapper;
        this.walletMapper = walletMapper;
        this.payMapper = payMapper;
        this.redisTemplate = redisTemplate;
        this.orderMapper = orderMapper;
    }

    @Override
    @Transactional
    public Boolean registerWithPhone(User user) {
        //注册账号
        try {
                user.setNickName(user.getAccount());
                user.setPassword(MD5Util.md5(user.getAccount() + user.getPassword()));
                user.setPhoto(Constant.UserPhoto);
                user.setPromotionCode(RandomUtils.generateUpperString(8));
                if(user.getParentId() == null || user.getPath() == null){
                    user.setParentId(0L);
                    user.setPath("");
                }
            user.setUserId(IdWorker.getFlowIdWorkerInstance().nextId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        int count = 0;
        try{
            count = userMapper.insertWithAccountPortion(user);
        }catch (Exception e){
            if(e != null  && e.getCause() != null  && e.getMessage() != null  && e.getCause().getMessage().contains("Duplicate entry")){
                String msg = "对不起, 您注册的账号已存在!";
                throw new MsgException(msg);
            }
        }
        if(count == 0) throw new MsgException("注册失败");
        //开通钱包
        Wallet wallets = new Wallet();
        wallets.setUserId(user.getUserId());
        wallets.setBalance(0D);
        wallets.setUpdateTime(new Date());
        wallets.setVersion(0);
        count = 0;
        count = walletMapper.insert(wallets);
        if(count == 0) throw new MsgException("开通钱包失败");
        return true;
    }

    @Override
    public UserResp getSysUserById(Long systemAccountsId) {
        UserDetailInfo userInfo = userMapper.selectUserDetail(systemAccountsId.toString());
        if(userInfo == null) return null;
        UserResp userResp = new UserResp();
        PropertyUtil.clone(userInfo, userResp);
        return userResp;
    }

    @Override
    public User getUserByAccount(String account) {
        return userMapper.selectByAccount(account);
    }

    @Override
    @Transactional
    public int changeBalance(String username, Double amount) {
        User user = this.getUserByAccount(username);
        if(user == null) throw new InfoException("用户不存在");
        user.setEditDate(new Date());
        userMapper.updateByPrimaryKey(user);
        Wallet wallet = walletMapper.selectByUid(user.getUserId());
        if(wallet == null) throw new MsgException("查询钱包失败");
        wallet.setBalance(wallet.getBalance() + amount);
        wallet.setUpdateTime(new Date());
        int count = walletMapper.updateByPrimaryKey(wallet);
        if(count == 0) throw new MsgException("更新失败");
        return count;
    }

    @Override
    public User getUserByPromotionCode(String promotionCode) {
        return userMapper.selectByPromotionCode(promotionCode);
    }

    /**
     * 获取推广会员列表
     * @param userId
     * @return
     */
    @Override
    public List<User> getPromoteMembers(Long userId) {
        return userMapper.selectPromoteMembers(userId);
    }

    /**
     * 晋级或忽略
     *
     * @param userId
     * @param level
     */
    @Override
    public void changeLevel(Long userId, Integer level) {
        int count = userMapper.updateLevel(userId, level);
        if(count <= 0) throw new InfoException("晋级失败");
    }

    @Override
    public User getUserById(Long userId) {
        return userMapper.selectById(userId);
    }

    @Override
    public Boolean resetPassword(String account, String phone, String password, String smsCode) {
        // 校验短信验证码是否正确或过期
        String dbSmsCode = (String) redisTemplate.opsForValue().get("reset-sms-" + phone);
        if(dbSmsCode == null || !dbSmsCode.equalsIgnoreCase(smsCode)) throw new InfoException("短信验证码不正确");

        // 加盐加密用户数据
        String encryptPassword = MD5Util.md5(account + password);

        // 更新数据库
        Boolean result = userMapper.resetPassword(phone, encryptPassword) > 0;
        return result;
    }

    @Override
    public User loginByUsername(String username, String password) {
        String encryptPassword = MD5Util.md5(username + password);

        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("account", username);
        criteria.andEqualTo("password", encryptPassword);
        example.and(criteria);
        User user = userMapper.selectOneByExample(example);

        return user;
    }

    @Override
    public boolean changeAvatar(Long userId, String url) {
        return userMapper.updateAvatar(userId, url) > 0;
    }

    @Override
    public boolean changeNickName(Long userId, String nickName) {
        return userMapper.updateNickName(userId, nickName) > 0;
    }

    @Override
    public boolean changeWechatCard(Long userId, String url) {
        return userMapper.updateWechatCard(userId, url) > 0;
    }

    @Override
    public boolean changePhone(Long userId, String phone, String smsCode) {
        // 校验短信验证码是否正确或过期
        String dbSmsCode = (String) redisTemplate.opsForValue().get("change-sms-" + phone);
        if(dbSmsCode == null || !dbSmsCode.equalsIgnoreCase(smsCode)) throw new InfoException("短信验证码不正确");

        User user = userMapper.selectByPhone(phone);
        if(user != null) throw new InfoException("该手机号已被绑定");
        return userMapper.updatePhoneAndAccount(userId, phone) > 0;
    }

    @Override
    public boolean changePaymentPassword(String account, Long userId, String password, String smsCode) {
        // 校验短信验证码是否正确或过期
        String dbSmsCode = (String) redisTemplate.opsForValue().get("payment-sms-" + account);
        if(dbSmsCode == null || !dbSmsCode.equalsIgnoreCase(smsCode)) throw new InfoException("短信验证码不正确");

        // 加盐加密用户数据
        String encryptPassword = MD5Util.md5(account + password);

        // 更新数据库
        boolean result = userMapper.changePaymentPassword(userId, encryptPassword) > 0;
        return result;
    }

    @Override
    public UserAssetView getAssets(Long userId) {
        UserAssetView assetView =  userMapper.selectAssets(userId);
        assetView.setAccount("0");
        return assetView;
    }

    @Override
    public boolean verifyPaymentPassword(String account, Long userId, String password) {
        User user = userMapper.selectById(userId);
        if(user.getPaymentPassword() == null){
            if(userMapper.changePaymentPassword(userId, MD5Util.md5(account + password)) <= 0){
                throw new InfoException("设置支付密码失败");
            }
        }
        return userMapper.selectByPaymentPassword(userId, MD5Util.md5(account + password)) != null;
    }


    @Override
    public boolean changePoster(Long userId, String url) {
        return userMapper.updatePoster(userId, url) > 0;
    }

    @Override
    public void bind(User user) {
        int count = userMapper.bind(user);
        if(count <= 0) throw new InfoException("绑定失败");
    }

    @Override
    public Boolean registerWithWx(User user) {
        //注册账号
        try {
            //微信通道
            user.setAccount(user.getOpenId());
            user.setPromotionCode(RandomUtils.generateUpperString(8));
            user.setParentId(0L);
            user.setUserId(IdWorker.getFlowIdWorkerInstance().nextId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        int count = 0;
        try{
            count = userMapper.insertWithWxPortion(user);
        }catch (Exception e){
            if(e != null  && e.getCause() != null  && e.getMessage() != null  && e.getCause().getMessage().contains("Duplicate entry")){
                String msg = "对不起, 您注册的账号已存在!";
                throw new MsgException(msg);
            }
        }
        if(count == 0) throw new MsgException("注册失败");
        //开通钱包
        Wallet wallets = new Wallet();
        wallets.setUserId(user.getUserId());
        wallets.setBalance(0D);
        wallets.setUpdateTime(new Date());
        wallets.setVersion(0);
        count = 0;
        count = walletMapper.insert(wallets);
        if(count == 0) throw new MsgException("开通钱包失败");
        return true;
    }

    @Override
    public User getUserByOpenId(String openid) {
        return userMapper.selectByOpenId(openid);
    }

    @Override
    public UserProfileView getUserWalletById(Long userId) {
        return userMapper.selectUserWalletById(userId);
    }

    @Override
    public List<User> getLimit(Integer page, String limit, String condition, Integer state, String beginTime, String endTime) {
        // 计算分页位置
        page = ConditionUtil.extractPageIndex(page, limit);
        String where = extractLimitWhere(condition, state, beginTime, endTime);
        List<User> list = userMapper.selectLimit(page, limit, beginTime, endTime, where);
        return list;
    }

    @Override
    public Integer getCount() {
        return userMapper.selectCount(new User());
    }

    @Override
    public Integer getLimitCount(String condition, Integer state, String beginTime, String endTime) {
        String where = extractLimitWhere(condition, state, beginTime, endTime);
        return userMapper.selectLimitCount(state, beginTime, endTime, where);
    }

    @Override
    public boolean editProfile(UserParam param) {
        String nickName = param.getNickName();
        String password = param.getPassword();
        String phone = param.getUsername();
        Integer level = param.getLevel();

        String condition = " edit_date=NOW() ";
        if(nickName != null && nickName.length() > 0) condition += ", nick_name='" + nickName +  "' ";
        if(password != null && password.length() > 0) condition += ", password='" + MD5Util.md5(param.getAccount() + param.getPassword()) +  "' ";
        if(phone != null && phone.length() > 0) condition += ", username='" + phone +  "' ";
        if(level != null) condition += ", level=" + level +  " ";

        return  userMapper.updateProfile(condition, Long.valueOf(param.getUserId())) > 0;
    }

    @Override
    public boolean blocking(UserParam param) {
        return userMapper.updateDelete(Long.valueOf(param.getUserId())) > 0;
    }

    @Override
    @Transactional
    public boolean changePassword(String account, Long userId, String password, String smsCode) {
        // 校验短信验证码是否正确或过期
        String dbSmsCode = (String) redisTemplate.opsForValue().get("password-sms-" + account);
        if(dbSmsCode == null || !dbSmsCode.equalsIgnoreCase(smsCode)) throw new InfoException("短信验证码不正确");

        // 加盐加密用户数据
        String encryptPassword = MD5Util.md5(account + password);

        // 更新数据库
        boolean result = userMapper.changePassword(userId, encryptPassword) > 0;

        //更新亚博平台密码
        //User profile = userMapper.selectById(userId);

        /*YaBoResp resp = YaBoUtil.changeLoginPassword(profile.getYaboToken(), profile.getAccount(), profile.getYaboPassword(), password);

        if(resp == null || !resp.getStatus().equals("success")) throw new InfoException("同步新密码失败");*/

        return result;
    }

    @Override
    public User loginWithPhone(String phone, String username, String password) {
        String encryptPassword = MD5Util.md5(username + password);

        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("phone", phone);
        criteria.andEqualTo("password", encryptPassword);
        example.and(criteria);
        User user = userMapper.selectOneByExample(example);

        return user;
    }


    /**
     * 提取分页条件
     * @return
     */
    private String extractLimitWhere(String condition, Integer state, String beginTime, String endTime) {
        // 查询模糊条件
        String where = " 1=1";
        if(condition != null) {
            condition = condition.trim();
            where += " AND (" + ConditionUtil.like("user_id", condition, true, "t1");
            if (condition.split("-").length == 2){
                where += " OR " + ConditionUtil.like("add_date", condition, true, "t1");
                where += " OR " + ConditionUtil.like("edit_date", condition, true, "t1");
            }
            where += " OR " + ConditionUtil.like("account", condition, true, "t1");
            where += " OR " + ConditionUtil.like("phone", condition, true, "t1");
            where += " OR " + ConditionUtil.like("ip", condition, true, "t1") + ")";
        }

        // 取两个日期之间或查询指定日期
        where = extractBetweenTime(beginTime, state, endTime, where);
        return where.trim();
    }


    /**
     * 提取两个日期之间的条件
     * @return
     */
    private String extractBetweenTime(String beginTime,Integer state, String endTime, String where) {
        if ((beginTime != null && beginTime.contains("-")) &&
                endTime != null && endTime.contains("-")){
            where += " AND t1.add_date BETWEEN #{beginTime} AND #{endTime}";
        }else if (beginTime != null && beginTime.contains("-")){
            where += " AND t1.add_date BETWEEN #{beginTime} AND #{endTime}";
        }else if (endTime != null && endTime.contains("-")){
            where += " AND t1.add_date BETWEEN #{beginTime} AND #{endTime}";
        }
        return where;
    }

    @Override
    public TeamView getTeamPreview(Long userId) {
        TeamView teamView = new TeamView();

        //查询我推广的所有会员-耗时操作
        List<User> promoteMembers = userMapper.selectPromoteMembers(userId);

        //筛选我的直推会员
        List<User> directMembers = promoteMembers.stream().filter(item -> item.getParentId().equals(userId)).collect(Collectors.toList());

        //筛选我的团队会员
        List<User> teamMembers = promoteMembers.stream().filter(item -> !item.getParentId().equals(userId)).collect(Collectors.toList());

        //计算得出销售冠军是哪位会员
        Map<Long, Long> countMap = promoteMembers.stream().collect(Collectors.groupingBy(p -> p.getParentId(), Collectors.counting()));
        List<User> smallMarket = new ArrayList<>();
        if(countMap.size() > 0){
            Map.Entry<Long, Long> maxMarket = countMap.entrySet().stream().max(Comparator.comparing(u -> u.getValue())).get();
            //计算得出小市场会员
            smallMarket = promoteMembers.stream().filter(tm -> !tm.getParentId().equals(maxMarket.getKey())).collect(Collectors.toList());
        }

        teamView.setDirectSaleCount(directMembers == null ? 0 : directMembers.size());
        teamView.setSamllMarketCount(smallMarket == null ? 0 : smallMarket.size());
        teamView.setChannelSaleCount(teamMembers == null ? 0 : teamMembers.size());

        return teamView;
    }

    @Override
    public boolean changeYaBoToken(Long userId, String token) {
        return userMapper.updateYaboToken(userId, token) > 0;
    }

    @Override
    public boolean unBlocking(UserParam param) {
        return userMapper.unBlocking(Long.valueOf(param.getUserId())) > 0;
    }

    @Override
    public List<UserView> getAgentLimit(Integer page, String limit, String condition, Integer state, String beginTime, String endTime) {
        // 计算分页位置
        page = ConditionUtil.extractPageIndex(page, limit);
        String where = extractLimitWhere(condition, state, beginTime, endTime);
        List<UserView> list = userMapper.selectAgentLimit(page, limit, beginTime, endTime, where);
        return list;
    }

    @Override
    public Integer getAgentCount() {
        return userMapper.selectAgentCount();
    }

    @Override
    public Integer getAgentLimitCount(String condition, Integer state, String beginTime, String endTime) {
        String where = extractLimitWhere(condition, state, beginTime, endTime);
        return userMapper.selectAgentLimitCount(state, beginTime, endTime, where);
    }

    @Override
    public List<OrderView> getOrders(Long userId) {
        return orderMapper.selectOrderViews(userId);
    }

    @Override
    public Integer getPlayerCount(Long userId) {
        UserAssetView assetView = userMapper.selectAssets(userId);
        return assetView.getPlayCount();
    }

    @Override
    public UserDetailInfo getUserViewById(Long userId) {
        return userMapper.selectUserDetail(userId + "");
    }

    @Override
    public List<MemberView> getPromoteMembersLimit(Long userId, Integer page, String phone) {
        String size =  "10";
        if(page == null) page = 0;
        String condition = " 1=1 ";
        if(phone != null && phone.length() > 0) {
            size = "10000";
            condition += " AND phone = '" + phone + "' ";
        }else{
            condition += " AND path LIKE '%" + userId +  "%' ";
        }
        return userMapper.selectPromoteMembersLimit(userId, page, size, condition);
    }

    @Override
    public Integer getPromoteMembersTotalPage(Long userId) {
        List<User> list = userMapper.selectPromoteMembers(userId);
        int size = list.size() / 10;
        return  size == 0 ? 1000 : size;
    }

}
