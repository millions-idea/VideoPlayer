/***
 * @pName management
 * @name BootstrapController
 * @user HongWei
 * @date 2018/8/26
 * @desc
 */
package com.management.admin.controller;

import com.google.common.collect.ImmutableMap;
import com.management.admin.annotaion.WxSign;
import com.management.admin.biz.IAdminUserService;
import com.management.admin.entity.db.AdminUser;
import com.management.admin.entity.template.JsonResult;
import com.management.admin.utils.MD5Util;
import com.management.admin.utils.TokenUtil;
import com.management.admin.utils.web.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/")
public class BootstrapController {
    @Autowired
    private IAdminUserService adminUserService;

    @Autowired
    private RedisTemplate redisTemplate;

    @WxSign
    @GetMapping(value = {"","wxVerify"})
    @ResponseBody
    public String index(HttpServletRequest request){
        String echostr = request.getParameter("echostr");
        return echostr;
    }

    @GetMapping(value = {"admin","management/bootstrap/signin"})
    public String signup(){
        return "bootstrap/index";
    }


    @PostMapping("management/bootstrap/login")
    @ResponseBody
    public JsonResult login(HttpServletRequest req, HttpServletResponse response, String username, String password){
        //基本权限认证
        AdminUser user = adminUserService.login(username, password);
        if(user == null) return new JsonResult().failingAsString("管理者账号不存在");

        //生成身份令牌
        Map<String, String> fields
                = ImmutableMap.of("account", username, "userId", user.getUserId() + "");
        String token = TokenUtil.create(fields);
        CookieUtil.setCookie(req, response, "token", token);

        //刷入缓存服务
        String key  = "token:" + MD5Util.encrypt16(user.getAccount() + user.getUserId());
        redisTemplate.opsForValue().set(key, token, 30, TimeUnit.DAYS);

        return new JsonResult().successful();
    }

}
