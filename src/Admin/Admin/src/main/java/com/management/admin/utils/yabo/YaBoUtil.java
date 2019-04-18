/***
 * @pName Admin
 * @name YaBoUtil
 * @user HongWei
 * @date 2019/3/31
 * @desc
 */
package com.management.admin.utils.yabo;

import com.management.admin.utils.JsonUtil;
import com.utility.http.HttpUtil;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class YaBoUtil {
    private final static String DOMAIN = "https://www.yabo2022.com/";

    public static YaBoLoginResp login(String username, String password){
        YaBoLoginBody body = new YaBoLoginBody();
        body.setDomain("www.yabo2022.com");
        body.setName(username);
        body.setPassword(password);
        String postForm = "name=" + body.getName() + "&password=" + body.getPassword() + "&domain=" + body.getDomain();
        String response = HttpUtil.postForm(DOMAIN + "api/v2/login", postForm, null);
        System.out.println(response);
        return JsonUtil.getModel(response, YaBoLoginResp.class);
    }

    public static YaBoProfileResp getProfile(String token, String name){
        Map<String, String> map = new HashMap<>();
        map.put("X-API-TOKEN", token);
        String response = HttpUtil.get(DOMAIN + "api/v1/web/userinfo?name=" + name, map);
        System.out.println(response);
        return JsonUtil.getModel(response, YaBoProfileResp.class);
    }

    public static YaBoResp changeLoginPassword(String token, String name, String oldPwd, String newPwd){
        Map<String, String> map = new HashMap<>();
        map.put("X-API-TOKEN", token);
        StringBuffer params = new StringBuffer();
        params.append("name=" + name);
        params.append("&old_password=" + oldPwd);
        params.append("&new_password=" + newPwd);
        params.append("&new_password_confirmation=" + newPwd);
        String response = HttpUtil.postForm(DOMAIN + "api/v2/loginpassowrd", params.toString(), map);
        System.out.println(response);
        return JsonUtil.getModel(response, YaBoResp.class);
    }

    public static void main(String[] args) {
        YaBoLoginResp login = YaBoUtil.login("hongwei521", "123456");
        YaBoProfileResp profile = YaBoUtil.getProfile(login.getData().getToken(), login.getData().getName());
        //YaBoResp loginPassword = YaBoUtil.changeLoginPassword(loginByUsername.getData().getToken(), loginByUsername.getData().getName(), "test123456", "123456");
        System.out.println("finish");
    }
}
