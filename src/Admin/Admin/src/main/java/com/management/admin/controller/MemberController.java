/***
 * @pName Admin
 * @name UserController
 * @user HongWei
 * @date 2018/12/16
 * @desc
 */
package com.management.admin.controller;

import com.management.admin.annotaion.WebLog;
import com.management.admin.biz.IUserService;
import com.management.admin.entity.db.Order;
import com.management.admin.entity.db.User;
import com.management.admin.entity.dbExt.UserDetailInfo;
import com.management.admin.entity.param.UserParam;
import com.management.admin.entity.resp.OrderView;
import com.management.admin.entity.resp.UserView;
import com.management.admin.entity.template.JsonArrayResult;
import com.management.admin.entity.template.JsonResult;
import com.management.admin.utils.DateUtil;
import com.management.admin.utils.PropertyUtil;
import com.management.admin.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/management/member")
public class MemberController {
    @Autowired
    private IUserService userService;

    @GetMapping("/index")
    public String index() {
        return "member/index";
    }

    @GetMapping("/getMemberLimit")
    @ResponseBody
    public JsonArrayResult<UserView> getMemberLimit(Integer page, String limit, String condition, Integer type, Integer state, String beginTime, String endTime) {
        Integer count = 0;
        List<User> list = userService.getLimit(page, limit, condition, state, beginTime, endTime);
        List<UserView> viewList = new ArrayList<>();
        list.forEach(item -> {
            UserView view = new UserView();
            PropertyUtil.clone(item, view);
            view.setUserId(item.getUserId() + "");
            viewList.add(view);
        });
        JsonArrayResult jsonArrayResult = new JsonArrayResult(0, viewList);
        if (StringUtil.isBlank(condition)
                && StringUtil.isBlank(beginTime)
                && StringUtil.isBlank(endTime)
                && (state == null || state == 0)) {
            count = userService.getCount();
        } else {
            count = userService.getLimitCount(condition, state, beginTime, endTime);
        }
        jsonArrayResult.setCount(count);
        return jsonArrayResult;
    }

    @GetMapping("edit")
    public String edit(String userId, final Model model) {
        User user = userService.getUserById(Long.valueOf(userId));
        UserDetailInfo userDetailInfo = new UserDetailInfo();
        PropertyUtil.clone(user,userDetailInfo);
        switch (user.getLevel()){
            case 0:
                userDetailInfo.setLevelText("普通用户");
                break;
            case 1:
                userDetailInfo.setLevelText("VIP会员");
                break;
            case 2:
                userDetailInfo.setLevelText("初级代理");
                break;
            case 3:
                userDetailInfo.setLevelText("区域代理");
                break;
            case 4:
                userDetailInfo.setLevelText("高级代理");
                break;
            case 5:
                userDetailInfo.setLevelText("事业股东");
                break;
        }
        //查询该用户开发的市场
        List<User> promoteMembers = userService.getPromoteMembers(user.getUserId());
        model.addAttribute("editDate",user.getEditDate());
        model.addAttribute("addDate",user.getAddDate());
        model.addAttribute("user", userDetailInfo);
        model.addAttribute("promoteMembers", promoteMembers);
        return "member/edit";
    }


    @PostMapping("/editProfile")
    @ResponseBody
    public JsonResult editProfile(UserParam param){
        boolean result = userService.editProfile(param);
        if(result) return JsonResult.successful();
        return JsonResult.failing();
    }


    @PostMapping("/blocking")
    @ResponseBody
    public JsonResult blocking(UserParam param){
        boolean result = userService.blocking(param);
        if(result) return JsonResult.successful();
        return JsonResult.failing();
    }


    @PostMapping("/unBlocking")
    @ResponseBody
    public JsonResult unBlocking(UserParam param){
        boolean result = userService.unBlocking(param);
        if(result) return JsonResult.successful();
        return JsonResult.failing();
    }

    @GetMapping("/preview")
    public String preview(String userId, final Model model) {
        UserDetailInfo userDetailInfo = userService.getUserViewById(Long.valueOf(userId));
        switch (userDetailInfo.getLevel()){
            case 0:
                userDetailInfo.setLevelText("普通用户");
                break;
            case 1:
                userDetailInfo.setLevelText("VIP会员");
                break;
            case 2:
                userDetailInfo.setLevelText("初级代理");
                break;
            case 3:
                userDetailInfo.setLevelText("区域代理");
                break;
            case 4:
                userDetailInfo.setLevelText("高级代理");
                break;
            case 5:
                userDetailInfo.setLevelText("事业股东");
                break;
        }
        //查询该用户开发的市场
        //List<User> promoteMembers = userService.getPromoteMembers(user.getUserId());
        //查询购买记录


        //查询观看次数
        Integer playerCount = userService.getPlayerCount(userDetailInfo.getUserId());
        model.addAttribute("editDate",userDetailInfo.getEditDate());
        model.addAttribute("addDate",userDetailInfo.getAddDate());
        model.addAttribute("user", userDetailInfo);
        model.addAttribute("playerCount", playerCount);
        return "member/preview";
    }

    @GetMapping("/getUserOrders")
    @ResponseBody
    public JsonArrayResult<OrderView> getUserOrders(String userId) {
        Integer count = 0;
        List<OrderView> orders = userService.getOrders(Long.valueOf(userId));
        orders.forEach(item -> {
            item.setOrderId(item.getOrderId() + "");
            item.setProductId(item.getProductId() + "");
            switch (item.getStatus()){
                case 0:
                    item.setStatusText("未上架");
                    break;
                case 1:
                    item.setStatusText("已上架");
                    break;
                case 2:
                    item.setStatusText("已下架");
                    break;
            }
        });
        JsonArrayResult jsonArrayResult = new JsonArrayResult(0, orders);
        return jsonArrayResult;
    }


}
