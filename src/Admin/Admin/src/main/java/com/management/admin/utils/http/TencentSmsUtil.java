/***
 * @pName Admin
 * @name AliSmsUtil
 * @user HongWei
 * @date 2018/11/27
 * @desc
 */
package com.management.admin.utils.http;

import com.alibaba.fastjson.JSONException;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.management.admin.entity.template.DataDictionary;

import javax.xml.ws.http.HTTPException;
import java.io.IOException;

/**
 * 腾讯云短信API服务工具类 DF 2019年3月8日23:18:43
 */
public class TencentSmsUtil {
    // 短信应用SDK AppID
    private static int appid = 1400009099; // 1400开头

    // 短信应用SDK AppKey
    private static String appkey = "9ff91d87c2cd7cd0ea762f141975d1df37481d48700d70ac37470aefc60f9bad";

    // 需要发送短信的手机号码
    private static String[] phoneNumbers = {"21212313123", "12345678902", "12345678903"};

    // 短信模板ID，需要在短信应用中申请

    // 签名
    private static String smsSign = "腾讯云"; // NOTE: 这里的签名"腾讯云"只是一个示例，真实的签名需要在短信控制台中申请，另外签名参数使用的是`签名内容`，而不是`签名ID`

    static {
        appid = Integer.parseInt(DataDictionary.Map.get("sms.tencent.config.appId").getValue());
        appkey = DataDictionary.Map.get("sms.tencent.config.appKey").getValue();
        smsSign = DataDictionary.Map.get("sms.tencent.config.smsSign").getValue();
    }

    /**
     * 发送短信
     * @param phone
     * @param templateId
     * @param smsCode
     * @param minute
     * @return
     */
    public static boolean sendMessage(String phone, int templateId, String smsCode,  String minute){
        try {
            if(minute == null) minute = "5";
            String[] params = {smsCode, minute};
            SmsSingleSender ssender = new SmsSingleSender(appid, appkey);
            SmsSingleSenderResult result = ssender.sendWithParam("86", phone, templateId, params, smsSign, "" ,"");
            System.out.println(result);
            if (result.result == 0) {
                return true;
            }
        } catch (HTTPException e) {
            // HTTP响应码错误
            //e.printStackTrace();
        } catch (JSONException e) {
            // json解析错误
            //e.printStackTrace();
        } catch (IOException e) {
            // 网络IO错误
            //e.printStackTrace();
        } catch (com.github.qcloudsms.httpclient.HTTPException e) {
            //e.printStackTrace();
        }
        return false;
    }
}
