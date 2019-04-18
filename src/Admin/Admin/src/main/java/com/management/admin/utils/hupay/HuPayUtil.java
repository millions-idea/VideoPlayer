/***
 * @pName Admin
 * @name HuPayUtil
 * @user HongWei
 * @date 2019/4/2
 * @desc
 */
package com.management.admin.utils.hupay;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.utils.HttpsUtils;
import com.management.admin.entity.param.BizBody;
import com.management.admin.entity.template.Constant;
import com.management.admin.utils.AmountUtil;
import com.management.admin.utils.MD5Util;
import com.management.admin.utils.StringUtil;
import com.management.admin.utils.wxOpen.WxApiUtil;
import com.management.admin.utils.wxOpen.WxSignUtil;
import com.utility.http.HttpUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.security.MD5Encoder;

import java.net.URLEncoder;
import java.util.*;

public class HuPayUtil {
    /*TODO::重打包时请及时更新此处信息*/
    private final static String AppId = "20146126568";
    private final static String AppSecret = "8C5FED359E080039808DE598639CF161";
    private final static String channelName=  "aishi";

    public static String payment(String tradeOrderId, String title, Double amount, String ext){
        try {
            Map<String, String> sortParams = new HashMap<String, String>();
            sortParams.put("version", "1.1");
            sortParams.put("lang", "zh-cn");
            sortParams.put("plugins", channelName);
            sortParams.put("appid", AppId);
            sortParams.put("trade_order_id", tradeOrderId);
            sortParams.put("payment", "alipay");
            sortParams.put("total_fee", amount + "");
            sortParams.put("title", title);
            sortParams.put("time", new Date().getTime()/1000+"");
            sortParams.put("notify_url", Constant.WebUrl +"api/order/notify?body=" + ext);//URLEncoder.encode(Constant.WebUrl +"api/order/notify?body=" + ext, "UTF-8")
            sortParams.put("return_url", Constant.AppUrl +"#/");
            sortParams.put("nonce_str", MD5Util.md5(UUID.randomUUID().toString()));
            sortParams.put("modal", "full");
            sortParams.put("hash", createSign(sortParams,AppSecret));

            System.out.println("sortParams" +sortParams);

            String response= HttpUtil.postForm("https://pay2.xunhupay.com/v2/payment/do.html", StringUtil.mapToFormData(sortParams, false), null);
            System.out.println(" response " +response);
            JSONObject jsonObject = JSONObject.parseObject(response);
            if (jsonObject != null) {
                Integer errorcode = jsonObject.getInteger("errcode");
                String errmsg = jsonObject.getString("errmsg");
                String openId = jsonObject.getString("openid");
                if (errorcode == 0 && StringUtils.equals(errmsg, "success!")) {
                    System.out.println(jsonObject.getString("html"));
                    String html = jsonObject.getString("html");
                    String url = StringUtil.getSubString(html, "pay_url:'","'");
                    System.out.println(url);
                    return url;
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String createSign(Map<String, String> params, String privateKey) {
        StringBuilder sb = new StringBuilder();
        // 将参数以参数名的字典升序排序
        Map<String, String> sortParams=sortMapByKey(params);
        // 遍历排序的字典,并拼接"key=value"格式
        for (Map.Entry<String, String> entry : sortParams.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue().trim();
            if (StringUtils.isNotEmpty(value))
            {
                sb.append("&").append(key).append("=").append(value);
            }
        }

        String stringA = sb.toString().replaceFirst("&", "");
        System.out.println("stringA" +stringA);
        String stringSignTemp = stringA + privateKey;
        String signValue = MD5Util.md5(stringSignTemp);
        return signValue;
    }



    public static Map<String, String> sortMapByKey(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        Map<String, String> sortMap = new TreeMap(new MapKeyComparator());
        sortMap.putAll(map);
        return sortMap;
    }


    public static class MapKeyComparator implements Comparator<String> {

        public int compare(String str1, String str2) {

            return str1.compareTo(str2);
        }
    }

    public static void main(String[] args) {

    }

}
