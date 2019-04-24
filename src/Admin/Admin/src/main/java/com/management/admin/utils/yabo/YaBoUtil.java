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
import okhttp3.Response;
import org.apache.tools.ant.taskdefs.condition.Http;
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

        //获取cookies
        Response cookieResponse = HttpUtil.getResponse(DOMAIN, null);
        String setCookie = cookieResponse.header("Set-Cookie");
        String cdnUid = setCookie.split(";")[0];
        System.out.println(cdnUid);

        Map<String, String> header = new HashMap<>();
        header.put("Content-Type","application/x-www-form-urlencoded");
        header.put("Referer", DOMAIN + "entry/login");
        header.put("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1");
        header.put("Cookie", "__cdnuid=a9f576a966147e4a35c7e80d1a7e9e41; serwe=we05; ser=a03; __cdn_clearance=1556073655.856|0|ScbJWJh%2Bbp3bQ5QoSJ437joszv0%3D");

        String response = HttpUtil.postForm(DOMAIN + "api/v2/login", postForm, header);
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
