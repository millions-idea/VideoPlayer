/***
 * @pName Admin
 * @name BillApiController
 * @user HongWei
 * @date 2019/3/9
 * @desc
 */
package com.management.admin.apiController;

import com.management.admin.biz.IPayService;
import com.management.admin.biz.IWithdrawService;
import com.management.admin.entity.db.Pays;
import com.management.admin.entity.db.Wallet;
import com.management.admin.entity.db.Withdraw;
import com.management.admin.entity.resp.GroupView;
import com.management.admin.entity.resp.MemberView;
import com.management.admin.entity.resp.PaysView;
import com.management.admin.entity.resp.WithdrawView;
import com.management.admin.entity.template.JsonArrayResult;
import com.management.admin.entity.template.JsonResult;
import com.management.admin.entity.template.SessionModel;
import com.management.admin.facade.IFinanceFacadeService;
import com.management.admin.utils.web.SessionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/bill")
public class BillApiController {
    @Autowired
    private IPayService payService;
    @Autowired
    private IWithdrawService withdrawService;
    @Autowired
    private IFinanceFacadeService financeFacadeService;

    @GetMapping("/getGroupList")
    public JsonArrayResult<List<GroupView>> getGroupList(HttpServletRequest request, Integer categoryId, @RequestParam(required = false) Integer page){
        SessionModel session = SessionUtil.getSession(request);
        if(categoryId == null) categoryId = 0;
        if(categoryId.equals(0)){
            //交易流水
            List<GroupView> list = payService.getTransLimit(session.getUserId(), page);

            list.forEach(item -> {
                if(item.getRemark() != null && item.getRemark().length() > 12){
                    item.setShortRemark(item.getRemark().substring(0, 12) + "...");
                }else{
                    item.setShortRemark(item.getRemark());
                }
            });

            Integer maxPage = payService.getTransTotalPage(session.getUserId());
            List<List<GroupView>> wrapper = new ArrayList<>();
            wrapper.add(list);

            List<GroupView> withdrawList = withdrawService.getUserWithdrawLimit(session.getUserId(), page);
            wrapper.add(withdrawList);

            JsonArrayResult jsonArrayResult = new JsonArrayResult(0, wrapper);
            jsonArrayResult.setCount(maxPage == null ? 1000 : maxPage);
            return jsonArrayResult;
        }else if(categoryId.equals(1)){
            //提现记录
            List<GroupView> list = withdrawService.getUserWithdrawLimit(session.getUserId(), page);
            Integer maxPage = withdrawService.getUserWithdrawTotalPage(session.getUserId());
            List<List<GroupView>> wrapper = new ArrayList<>();
            wrapper.add(list);
            JsonArrayResult jsonArrayResult = new JsonArrayResult(0, wrapper);
            jsonArrayResult.setCount(maxPage == null ? 1000 : maxPage);
            return jsonArrayResult;
        }
        return JsonArrayResult.failing();
    }

    /**
     * 申请提现
     * @param request
     * @param amount
     * @param realName
     * @param paymentPassword
     * @return
     */
    @GetMapping("/withdraw")
    public JsonResult withdraw(HttpServletRequest request, Double amount, String financeId,  String realName, String paymentPassword){
        SessionModel session = SessionUtil.getSession(request);
        financeFacadeService.apply(session.getAccount(), session.getUserId(), amount, financeId,  realName, paymentPassword);
        return JsonResult.successful();
    }

    /**
     * 实时查询余额
     * @param request
     * @return
     */
    @GetMapping("/getBalance")
    public JsonResult getBalance(HttpServletRequest request){
        SessionModel session = SessionUtil.getSession(request);
        Wallet wallet = payService.getBalance(session.getUserId());
        return new JsonResult().successful(wallet.getBalance());
    }
}
