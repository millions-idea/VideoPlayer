/***
 * @pName Admin
 * @name ApiUtil
 * @user HongWei
 * @date 2019/3/12
 * @desc
 */
package com.management.admin.utils.wxOpen;

import com.management.admin.utils.JsonUtil;
import com.utility.http.HttpUtil;

public class WxApiUtil {
    private final static String appId = "wxc646c273c4be6842";
    private final static String appSecret = "da487eb08beada9e12b4c778f31e0e7e";

    public static String getAccessToken(){
        /*grant_type	是	获取access_token填写client_credential
        appid	是	第三方用户唯一凭证
        secret	是	第三方用户唯一凭证密钥，即appsecret*/
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + appId + "&secret=" + appSecret;
        String response = HttpUtil.get(url, null);
        return response;
    }

    public static void main(String[] args) {
        String accessToken = WxApiUtil.getAccessToken();
        System.out.println(accessToken);
    }

    public static WxAccessToken getAccessToken(String code) {
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + appId + "&secret=" + appSecret + "&code=" + code+ "&grant_type=authorization_code";
        String response = HttpUtil.get(url, null);
        /*{
            "access_token":"ACCESS_TOKEN",
                "expires_in":7200,
                "refresh_token":"REFRESH_TOKEN",
                "openid":"OPENID",
                "scope":"SCOPE"
        }*/
        if(!response.contains("errcode")){
            WxAccessToken wxAccessToken = JsonUtil.getModel(response, WxAccessToken.class);
            return wxAccessToken;
        }else {
            System.err.println("getAccessToken:" + response);
        }
        return null;
    }


    public static WxUserInfo getUserInfo(String accessToken, String openId){
        /*{
            "openid":" OPENID",
                " nickname": NICKNAME,
                "sex":"1",
                "province":"PROVINCE"
            "city":"CITY",
                "country":"COUNTRY",
                "headimgurl":       "http://thirdwx.qlogo.cn/mmopen/g3MonUZtNHkdmzicIlibx6iaFqAc56vxLSUfpb6n5WKSYVY0ChQKkiaJSgQ1dZuTOgvLLrhJbERQQ4eMsv84eavHiaiceqxibJxCfHe/46",
                "privilege":[ "PRIVILEGE1" "PRIVILEGE2"     ],
            "unionid": "o6_bmasdasdsad6_2sgVt7hMZOPfL"
        }*/
        String url = "https://api.weixin.qq.com/sns/userinfo?access_token="+ accessToken + "&openid=" + openId + "&lang=zh_CN";
        String response = HttpUtil.get(url, null);
        if(!response.contains("errcode")){
            WxUserInfo wxUserInfo = JsonUtil.getModel(response, WxUserInfo.class);
            return wxUserInfo;
        }else{
            System.err.println("getUserInfo:" + response);
        }
        return null;
    }

}
