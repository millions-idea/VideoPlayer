/***
 * @pName Admin
 * @name AgentController
 * @user HongWei
 * @date 2019/4/7
 * @desc
 */
package com.management.admin.controller;

import com.management.admin.biz.IUserService;
import com.management.admin.entity.db.User;
import com.management.admin.entity.resp.UserView;
import com.management.admin.entity.template.JsonArrayResult;
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
@RequestMapping("/management/agent")
public class AgentController {
    @Autowired
    private IUserService userService;

    @GetMapping("/index")
    public String index(){
        return "/agent/index";
    }

    @GetMapping("/getLimit")
    @ResponseBody
    public JsonArrayResult<UserView> getLimit(Integer page, String limit, String condition, Integer type, Integer state, String beginTime, String endTime) {
        Integer count = 0;
        List<UserView> list = userService.getAgentLimit(page, limit, condition, state, beginTime, endTime);
        JsonArrayResult jsonArrayResult = new JsonArrayResult(0, list);
        if (StringUtil.isBlank(condition)
                && StringUtil.isBlank(beginTime)
                && StringUtil.isBlank(endTime)
                && (state == null || state == 0)) {
            count = userService.getAgentCount();
        } else {
            count = userService.getAgentLimitCount(condition, state, beginTime, endTime);
        }
        jsonArrayResult.setCount(count);
        return jsonArrayResult;
    }


}
