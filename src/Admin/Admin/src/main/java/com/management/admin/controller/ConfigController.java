/***
 * @pName management
 * @name FinanceController
 * @user HongWei
 * @date 2018/8/27
 * @desc
 */
package com.management.admin.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.management.admin.biz.IDictionaryService;
import com.management.admin.biz.IPayService;
import com.management.admin.biz.IUserService;
import com.management.admin.entity.db.Accounts;
import com.management.admin.entity.db.Dictionary;
import com.management.admin.entity.db.Pays;
import com.management.admin.entity.db.User;
import com.management.admin.entity.resp.AccountsResp;
import com.management.admin.entity.resp.PaysResp;
import com.management.admin.entity.resp.UserResp;
import com.management.admin.entity.template.*;
import com.management.admin.exception.FinanceException;
import com.management.admin.utils.JsonUtil;
import com.management.admin.utils.PropertyUtil;
import com.management.admin.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/management/config")
public class ConfigController {
    @Autowired
    private IPayService payService;
    @Autowired
    private IUserService userService;

    @Autowired
    private IDictionaryService dictionaryService;

    @GetMapping("/index")
    public String index(final Model model){
        model.addAttribute("map",DataDictionary.Map);
        model.addAttribute("appVersion", JsonUtil.getModel(DataDictionary.Map.get("app.version").getValue(), AppVersion.class));
        return "config/index";
    }

    @PostMapping("/editProfile")
    @ResponseBody
    public JsonResult editProfile(HttpServletRequest request){
        Map<String, String[]> parameterMap = request.getParameterMap();
        boolean result = dictionaryService.updateMap(parameterMap);
        if(result) {
            List<Dictionary> list = dictionaryService.getList();
            Map<String,Dictionary> dataDictionaryList = new HashMap<>();
            list.forEach(dictionary -> dataDictionaryList.put(dictionary.getKey() , dictionary));
            DataDictionary.Map = dataDictionaryList;
            return JsonResult.successful();
        }
        return JsonResult.failing();
    }
}
