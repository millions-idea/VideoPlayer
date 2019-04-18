/***
 * @pName management
 * @name WithdrawController
 * @user HongWei
 * @date 2018/8/21
 * @desc
 */
package com.management.admin.controller;

import com.management.admin.biz.IWithdrawService;
import com.management.admin.entity.db.Withdraw;
import com.management.admin.entity.dbExt.WithdrawDetailInfo;
import com.management.admin.entity.param.WithdrawParam;
import com.management.admin.entity.resp.WithdrawResp;
import com.management.admin.entity.template.JsonArrayResult;
import com.management.admin.entity.template.JsonResult;
import com.management.admin.exception.InfoException;
import com.management.admin.facade.IFinanceFacadeService;
import com.management.admin.utils.JsonUtil;
import com.management.admin.utils.PropertyUtil;
import com.management.admin.utils.RandomUtils;
import com.management.admin.utils.StringUtil;
import com.management.admin.utils.alipay.AliPayTransfer;
import com.management.admin.utils.alipay.AliPayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/management/withdraw")
public class WithdrawController extends BaseController{
    @Autowired
    private IFinanceFacadeService financeFacadeService;
    @Autowired
    private IWithdrawService withdrawService;
    @PostMapping("/confirm")
    @ResponseBody
    public JsonResult confirmWithdraw(Withdraw withdraw){
        financeFacadeService.confirmWithdraw(withdraw);
        return JsonResult.successful();
    }


    @PostMapping("/pass")
    @ResponseBody
    public JsonResult pass(WithdrawParam param){
        Withdraw withdraw = new Withdraw();
        PropertyUtil.clone(param, withdraw);
        withdraw.setUserId(Long.valueOf(param.getUserId()));
        financeFacadeService.reApply(withdraw);
        return JsonResult.successful();
    }

    @GetMapping("/index")
    public String index(){
        return "withdraw/index";
    }

    /**
     * 提现审批 韦德 2018年8月29日11:42:31
     * @return
     */
    @GetMapping("/getWithdrawLimit")
    @ResponseBody
    public JsonArrayResult<WithdrawResp> getWithdrawLimit(Integer page, String limit, String condition, Integer state, String beginTime, String endTime){
        Integer count = 0;
        List<WithdrawDetailInfo> list = withdrawService.getWithdrawLimit(page, limit, condition, state, beginTime, endTime);
        List<WithdrawResp> wrapList = new ArrayList<>();
        list.forEach(item -> {
            WithdrawResp withdrawResp = new WithdrawResp();
            PropertyUtil.clone(item, withdrawResp);
            withdrawResp.setUserId(String.valueOf(item.getUserId()));
            withdrawResp.setSystemRecordId(item.getSystemRecordId().toString());
            wrapList.add(withdrawResp);
        });
        JsonArrayResult jsonArrayResult = new JsonArrayResult(0, wrapList);
        if (StringUtil.isBlank(condition)
                && StringUtil.isBlank(beginTime)
                && StringUtil.isBlank(endTime)
                && (state == null || state == 0)){
            count = withdrawService.getWithdrawCount();
        }else{
            count = withdrawService.getWithdrawLimitCount(condition, state, beginTime, endTime);
        }
        jsonArrayResult.setCount(count);
        return jsonArrayResult;
    }


    @GetMapping("/getNewRecord")
    @ResponseBody
    public JsonResult<Withdraw> getNewRecord(){
        Withdraw withdraw = withdrawService.getNewRecord();
        return new JsonResult<Withdraw>().successful(withdraw);
    }

    @PostMapping("/quickTransfer")
    @ResponseBody
    public JsonResult quickTransfer(Integer id){
        Withdraw withdraw = new Withdraw();
        withdraw.setWithdrawId(id);
        withdraw =  withdrawService.getWithdrawById(withdraw);
        String outBizNo = RandomUtils.generateNumberString(8)  + ""  + id + "";
        AliPayTransfer result = AliPayUtil.transfer(outBizNo, withdraw.getFinanceId(), withdraw.getAmount(), "微友宝", withdraw.getFinanceName(), "金币兑换");
        if (result != null){
            withdraw.setChannelRecordId(result.getAlipay_fund_trans_toaccount_transfer_response().getOrder_id());
            withdraw.setAmount(withdraw.getAmount() - 2);
            boolean isSuccess = financeFacadeService.confirmWithdraw(withdraw);
            if(isSuccess){
                return JsonResult.successful();
            }
        }
        return JsonResult.failing();
    }
}
