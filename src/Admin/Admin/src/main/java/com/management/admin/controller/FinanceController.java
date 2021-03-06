/***
 * @pName management
 * @name FinanceController
 * @user HongWei
 * @date 2018/8/27
 * @desc
 */
package com.management.admin.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.management.admin.biz.IPayService;
import com.management.admin.biz.IUserService;
import com.management.admin.entity.db.Accounts;
import com.management.admin.entity.db.Pays;
import com.management.admin.entity.db.User;
import com.management.admin.entity.resp.AccountsResp;
import com.management.admin.entity.resp.PaysResp;
import com.management.admin.entity.resp.UserResp;
import com.management.admin.entity.template.Constant;
import com.management.admin.entity.template.JsonArrayResult;
import com.management.admin.entity.template.JsonResult;
import com.management.admin.exception.FinanceException;
import com.management.admin.utils.PropertyUtil;
import com.management.admin.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/management/finance")
public class FinanceController {
    @Autowired
    private IPayService payService;
    @Autowired
    private IUserService userService;

    /**
     * 财务会计账目 韦德 2018年8月27日00:18:43
     * @return
     */
    @GetMapping("/accounts")
    public String  accounts(){
        return "finance/accounts/index";
    }

    /**
     * 分页加载财务会计账目 韦德 2018年8月3日11:48:50
     * @param condition
     * @return
     */
    @RequestMapping("/accounts/getAccountsLimit")
    @ResponseBody
    public JsonArrayResult<AccountsResp> getAccountsLimit(Integer page, String limit, String condition, Integer trade_type, Integer filter_type, String trade_date_begin, String trade_date_end){
        Integer count = 0;
        if(condition != null && condition.contains("[add]")) condition = condition.replace("[add]", "+");
        if(condition != null && condition.contains("[reduce]")) condition = condition.replace("[reduce]", "-");
        List<Accounts> list = payService.getAccountsLimit(page, limit, condition, trade_type, filter_type, trade_date_begin, trade_date_end);
        List<AccountsResp> wrapList = new ArrayList<>();
        list.forEach(item -> {
            AccountsResp resp = new AccountsResp();
            PropertyUtil.clone(item, resp);
            resp.setAccountsId(item.getAccountsId().toString());
            resp.setPayId(item.getPayId().toString());
            wrapList.add(resp);
        });
        JsonArrayResult jsonArrayResult = new JsonArrayResult(0,wrapList);
        if (StringUtil.isBlank(condition)
                && StringUtil.isBlank(trade_date_begin)
                && StringUtil.isBlank(trade_date_end)
                && (trade_type == null || trade_type == 0)){
            count = payService.getAccountsCount();
        }else{
            count = payService.getAccountsLimitCount(condition, trade_type, filter_type, trade_date_begin, trade_date_end);
        }
        jsonArrayResult.setCount(count);
        return jsonArrayResult;
    }


    /**
     * 财务交易流水 韦德 2018年8月27日00:18:43
     * @return
     */
    @GetMapping("/pay")
    public String  pay(){
        return "finance/pay/index";
    }

    /**
     * 查询系统账户信息 韦德 2018年8月27日21:53:30
     * @return
     */
    @GetMapping("/pay/getSystemAccount")
    @ResponseBody
    @JsonView(UserResp.FinanceView.class)
    public UserResp getSystemAccount(){
        UserResp userDetail = userService.getSysUserById(Constant.SYSTEM_ACCOUNTS_ID);
        if(userDetail != null) {
            UserResp accountAmount = payService.getAccountAmount(Constant.SYSTEM_ACCOUNTS_ID);
            if(accountAmount != null) {
                userDetail.setIncomeAmount(accountAmount.getIncomeAmount());
                userDetail.setExpendAmount(accountAmount.getExpendAmount());
            }
        }
        return userDetail;
    }

    /**
     * 分页加载交易流水 韦德 2018年8月27日21:54:08
     * @param condition
     * @return
     */
    @RequestMapping("/pay/getPayLimit")
    @ResponseBody
    public JsonArrayResult<PaysResp> getPayLimit(Integer page, String limit, String condition, Integer trade_type, Integer filter_type, String trade_date_begin, String trade_date_end){
        Integer count = 0;
        List<Pays> list = payService.getPaysLimit(page, limit, condition, trade_type, filter_type, trade_date_begin, trade_date_end);
        List<PaysResp> wrapList = new ArrayList<>();
        list.forEach(item -> {
            PaysResp resp = new PaysResp();
            PropertyUtil.clone(item, resp);
            resp.setPayId(item.getPayId().toString());
            resp.setSystemRecordId(item.getSystemRecordId().toString());
            wrapList.add(resp);
        });
        JsonArrayResult jsonArrayResult = new JsonArrayResult(0,wrapList);
        if (StringUtil.isBlank(condition)
                && StringUtil.isBlank(trade_date_begin)
                && StringUtil.isBlank(trade_date_end)
                && (trade_type == null || trade_type == 0)){
            count = payService.getPaysCount();
        }else{
            count = payService.getPaysLimitCount(condition, trade_type, filter_type, trade_date_begin, trade_date_end);
        }
        jsonArrayResult.setCount(count);
        return jsonArrayResult;
    }

    @GetMapping("/recharge")
    /**
     * 手工充值页面
     * @return
     */
    public String recharge(){
        return "finance/pay/recharge";
    }

    @GetMapping("/pay/toRecharge")
    @ResponseBody
    /**
     * 手工充值
     * @return
     */
    public JsonResult recharge(String username, Double amount) {
        User user = userService.getUserByAccount(username);
        if(user == null) throw new FinanceException(FinanceException.Errors.NOT_FOUND_USER,"用户不存在");
        if(payService.recharge(user.getUserId(), amount)) return JsonResult.successful();
        return JsonResult.failing();
    }


    @GetMapping("/pay/changeBalance")
    @ResponseBody
    /**
     * 无流水充值
     * @return
     */
    public JsonResult changeBalance(String username, Double amount) {
        userService.changeBalance(username, amount);
        return JsonResult.successful();
    }

}
