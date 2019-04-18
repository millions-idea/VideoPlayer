/***
 * @pName Admin
 * @name AliPayUtil
 * @user HongWei
 * @date 2019/3/8
 * @desc
 */
package com.management.admin.utils.alipay;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.request.AlipayFundTransOrderQueryRequest;
import com.alipay.api.request.AlipayFundTransToaccountTransferRequest;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayFundTransOrderQueryResponse;
import com.alipay.api.response.AlipayFundTransToaccountTransferResponse;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.management.admin.entity.template.DataDictionary;
import com.management.admin.utils.JsonUtil;

public class AliPayUtil {
    private static String APP_ID;
    private static String APP_PRIVATE_KEY;
    public static String CHARSET;
    public static String ALIPAY_PUBLIC_KEY;
    private static String NotifyUrl;
    private static AlipayClient alipayClient;

    static {
        APP_ID = DataDictionary.Map.get("payment.alipay.appId").getValue();
        APP_PRIVATE_KEY = DataDictionary.Map.get("payment.alipay.privateKey").getValue();
        CHARSET = "UTF-8";
        ALIPAY_PUBLIC_KEY = DataDictionary.Map.get("payment.alipay.publicKey").getValue();
        NotifyUrl = DataDictionary.Map.get("payment.alipay.notify").getValue();
        //实例化客户端
        alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", APP_ID, APP_PRIVATE_KEY, "json", CHARSET, ALIPAY_PUBLIC_KEY, "RSA2");
    }

    public static String getAppPaymentParams(Long tradeNo, String body, String subject, Double totalAmount){
//实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
//SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setBody(body);
        model.setSubject(subject);
        model.setOutTradeNo(tradeNo + "");
        model.setTimeoutExpress("30m");
        model.setTotalAmount(totalAmount + "");
        model.setProductCode("QUICK_MSECURITY_PAY");
        request.setBizModel(model);
        request.setNotifyUrl(NotifyUrl);
        try {
            //这里和普通的接口调用不同，使用的是sdkExecute
            AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
            System.out.println(response.getBody());//就是orderString 可以直接给客户端请求，无需再做处理。
            return response.getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 支付宝快速转账
     * @param outBizNo
     * @param account
     * @param amount
     * @param showName
     * @param realName
     * @param desc
     * @return
     */
    public static AliPayTransfer transfer(String outBizNo, String account, Double amount, String showName, String realName, String desc){
        AlipayFundTransToaccountTransferRequest request = new AlipayFundTransToaccountTransferRequest();
        request.setBizContent("{" +
                "    \"out_biz_no\":\""  + outBizNo + "\"," +
                "    \"payee_type\":\"ALIPAY_LOGONID\"," +
                "    \"payee_account\":\"" + account + "\"," +
                "    \"amount\":\"" + amount +  "\"," +
                "    \"payer_show_name\":\"" + showName + "\"," +
                "    \"payee_real_name\":\"" + realName + "\"," +
                "    \"remark\":\"" + desc + "\"," +
                "  }");
        AlipayFundTransToaccountTransferResponse response = null;
        try {
            response = alipayClient.execute(request);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        if(response.isSuccess()){
            System.out.println("调用成功");
            //支付宝转账单据号，成功一定返回，失败可能不返回也可能返回。
            /*{
                "alipay_fund_trans_toaccount_transfer_response": {
                "code": "10000",
                        "msg": "Success",
                        "out_biz_no": "3142321423432",
                        "order_id": "20160627110070001502260006780837",
                        "pay_date": "2013-01-01 08:08:08"
            },
                "sign": "ERITJKEIJKJHKKKKKKKHJEREEEEEEEEEEE"
            }*/
            return JsonUtil.getModel(response.getBody(),AliPayTransfer.class);
        } else {
            System.out.println("调用失败");
        }
        return null;
    }

    /**
     * 查询转账结果
     * @param out_biz_no 商户转账唯一订单号：发起转账来源方定义的转账单据ID。
    和支付宝转账单据号不能同时为空。当和支付宝转账单据号同时提供时，将用支付宝转账单据号进行查询，忽略本参数。
     * @param order_id 支付宝转账单据号：和商户转账唯一订单号不能同时为空。当和商户转账唯一订单号同时提供时，将用本参数进行查询，忽略商户转账唯一订单号。
     * @return
     */
    public static AliPayTransfer getTransferStatus(String out_biz_no, String order_id){
        AlipayFundTransOrderQueryRequest request = new AlipayFundTransOrderQueryRequest();
        request.setBizContent("{" +
                "\"out_biz_no\":\"" + out_biz_no + "\"," +
                "\"order_id\":\"" + order_id + "\"" +
                "  }");
        AlipayFundTransOrderQueryResponse response = null;
        try {
            response = alipayClient.execute(request);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        if(response.isSuccess()){
            System.out.println("调用成功");
            return JsonUtil.getModel(response.getBody(),AliPayTransfer.class);
        } else {
            System.out.println("调用失败");
        }
        return null;
    }
}
