/***
 * @pName Admin
 * @name WxApiController
 * @user HongWei
 * @date 2019/3/12
 * @desc
 */
package com.management.admin.apiController;

import com.google.common.collect.ImmutableMap;
import com.management.admin.biz.IOrderService;
import com.management.admin.biz.IUserService;
import com.management.admin.entity.db.Order;
import com.management.admin.entity.db.User;
import com.management.admin.entity.param.BizBody;
import com.management.admin.entity.template.Constant;
import com.management.admin.entity.template.DataDictionary;
import com.management.admin.entity.template.JsonResult;
import com.management.admin.entity.template.SessionModel;
import com.management.admin.exception.InfoException;
import com.management.admin.facade.IFinanceFacadeService;
import com.management.admin.facade.IUserFacadeService;
import com.management.admin.utils.*;
import com.management.admin.utils.web.SessionUtil;
import com.management.admin.utils.wxOpen.*;
import com.utility.http.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/wx")
public class WxApiController {

    @Autowired
    private IUserFacadeService userFacadeService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IOrderService orderService;

    @Autowired
    private IFinanceFacadeService financeFacadeService;
    private final Logger logger = LoggerFactory.getLogger(WxApiController.class);


    @GetMapping("/authorize")
    public JsonResult authorize(){
        String appId = DataDictionary.Map.get("payment.wechat.mp.appId").getValue();
        String url = "http%3A%2F%2F" + Constant.WebUrl.replaceAll("http://","").replaceAll("/","") + "%2Fapi%2Fwx/oauth";
        return new JsonResult().successful("https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + appId
                + "&redirect_uri=" + url + "&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect");
    }


    @GetMapping("/oauth")
    public void oauth(HttpServletRequest request, HttpServletResponse response, String code, String state){
        //code说明 ： code作为换取access_token的票据，每次用户授权带上的code将不一样，code只能使用一次，5分钟未被使用自动过期。
        /*错误返回码说明如下：
        返回码	说明
        10003	redirect_uri域名与后台配置不一致
        10004	此公众号被封禁
        10005	此公众号并没有这些scope的权限
        10006	必须关注此测试号
        10009	操作太频繁了，请稍后重试
        10010	scope不能为空
        10011	redirect_uri不能为空
        10012	appid不能为空
        10013	state不能为空
        10015	公众号未授权第三方平台，请检查授权状态
        10016	不支持微信开放平台的Appid，请使用公众号Appid*/
        WxAccessToken wxAccessToken = WxApiUtil.getAccessToken(code);
        if(wxAccessToken != null){
            String userIp = RequestUtil.getIp(request);

            WxUserInfo userInfo = WxApiUtil.getUserInfo(wxAccessToken.getAccess_token(), wxAccessToken.getOpenid());
            if(userInfo != null){
                //预注册账号
               try {
                   User profile = userService.getUserByOpenId(wxAccessToken.getOpenid());
                   if(profile == null){
                       User user = userFacadeService.registerWithWx(wxAccessToken.getOpenid(), wxAccessToken.getAccess_token()
                               , userInfo.getNickname(), userInfo.getHeadimgurl(), userIp);
                       if(user != null) {
                           try {
                               //response.sendRedirect(Constant.AppUrl  + "#/pages/user/bootstrap/bind?userId=" + user.getUserId() + "");
                               response.getWriter().print("<script>window.top.location='" + Constant.AppUrl  + "#/pages/user/bootstrap/bind?userId=" + user.getUserId() + "" + "';</script>");
                               return;
                           } catch (IOException e) {
                               e.printStackTrace();
                           }
                       }
                   }else if(profile.getPhone() == null){
                       try {
                           //response.sendRedirect(Constant.AppUrl  + "#/pages/user/bootstrap/bind?userId=" + profile.getUserId() + "");
                           response.getWriter().print("<script>window.top.location='" + Constant.AppUrl  + "#/pages/user/bootstrap/bind?userId=" + profile.getUserId() + "" + "';</script>");
                           return;
                       } catch (IOException e) {
                           e.printStackTrace();
                       }

                   }else{
                       try {
                           Map<String, String> fields
                                   = ImmutableMap.of("account", profile.getAccount(), "userId", profile.getUserId() + "");
                           String token = TokenUtil.create(fields);


                           //response.sendRedirect(Constant.AppUrl  + "#/pages/user/bootstrap/loginByUsername?token=" + token +  "");
                           response.getWriter().print("<script>window.top.location='" + Constant.AppUrl  + "#/pages/user/bootstrap/loginByUsername?token=" + token +  "" + "';</script>");
                           return;
                       } catch (IOException e) {
                           e.printStackTrace();
                       }

                   }


               }catch (Exception e){
                   try {
                       //response.sendRedirect(Constant.AppUrl  + "#/pages/user/bootstrap/loginByUsername?pop=" + e.getMessage());
                       response.getWriter().print("<script>window.top.location='" + Constant.AppUrl  + "#/pages/user/bootstrap/loginByUsername?pop=" + e.getMessage() + "';</script>");
                       return;
                   } catch (IOException ex) {
                       ex.printStackTrace();
                   }

               }

            }
        }
        try {
            //response.sendRedirect(Constant.AppUrl  + "#/pages/user/bootstrap/loginByUsername?pop=" + "微信登录授权失败");
            response.getWriter().print("<script>window.top.location='" + Constant.AppUrl  + "#/pages/user/bootstrap/loginByUsername?pop=" + "微信登录授权失败" + "';</script>");
            return;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 统一下单接口，获取prepay_id
     * @param request
     * @return
     */
    @RequestMapping(value = "/unifiedOrder", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, String> unifiedOrder(HttpServletRequest request, String outTradeNo,  String totalFee) {
        SessionModel session = SessionUtil.getSession(request);

        User user = userService.getUserById(session.getUserId());

        if(user.getOpenId() == null) throw new InfoException(" OpenId不能为空, 请重新微信授权!");

        String appId = DataDictionary.Map.get("payment.wechat.mp.appId").getValue();
        String mchId = DataDictionary.Map.get("payment.wechat.mp.mchId").getValue();
        Boolean isVip = outTradeNo.equalsIgnoreCase(DataDictionary.Map.get("platform.vip.pid").getValue());
        String notifyUrl = DataDictionary.Map.get("payment.wechat.notify").getValue();
        String paternerKey = DataDictionary.Map.get("payment.wechat.paternerKey").getValue();

        Order order = orderService.getOrder(Long.valueOf(outTradeNo));
        if(order == null) throw new InfoException("订单不存在");
        if(!order.getStatus().equals(0)) throw new InfoException("订单状态有误");

        try {
            // 不带properties扩展名的文件名
            //ResourceBundle wx = ResourceBundle.getBundle("wx");
            // 统一下单 https://api.mch.weixin.qq.com/pay/unifiedorder
            String unifiedorder_url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
            //拼接统一下单地址参数
            Map<String, String> paraMap = new HashMap<String, String>();
            //获取请求ip地址
            String ip = request.getHeader("x-forwarded-for");
            if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
                ip = request.getHeader("Proxy-Client-IP");
            }
            if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
                ip = request.getRemoteAddr();
            }
            if(ip.indexOf(",")!=-1){
                String[] ips = ip.split(",");
                ip = ips[0].trim();
            }
            paraMap.put("appid", appId);
            paraMap.put("body", isVip ? "充值会员" : "购买商品-" + order.getProductName());

            BizBody bizBody = new BizBody();
            if (!isVip){
                bizBody.setChannel("order");
            }else{
                bizBody.setChannel("vip");
            }
            bizBody.setUserId(session.getUserId());
            paraMap.put("attach", JsonUtil.getJsonNotEscape(bizBody));

            paraMap.put("mch_id", mchId);
            paraMap.put("nonce_str", WXPayUtil.generateNonceStr());
            paraMap.put("openid", user.getOpenId());
            paraMap.put("out_trade_no", outTradeNo);//订单号
            paraMap.put("spbill_create_ip", ip);
            paraMap.put("total_fee", AmountUtil.changeY2F(totalFee));
            paraMap.put("trade_type", "JSAPI");
            paraMap.put("notify_url", notifyUrl);// 此路径是微信服务器调用支付结果通知路径随意写
            String sign = WXPayUtil.generateSignature(paraMap, paternerKey);
            paraMap.put("sign", sign);
            String xml = WXPayUtil.mapToXml(paraMap);//将所有参数(map)转xml格式
            System.out.println(xml);
            String xmlStr = HttpUtil.postForm(unifiedorder_url, xml, null); //发送post请求"统一下单接口"返回预支付id:prepay_id
            System.out.println(xmlStr);

            //以下内容是返回前端页面的json数据
            String prepay_id = "";//预支付id
            if (xmlStr.indexOf("SUCCESS") != -1) {
                Map<String, String> map = WXPayUtil.xmlToMap(xmlStr);
                prepay_id = (String) map.get("prepay_id");
            }
            Map<String, String> payMap = new HashMap<String, String>();
            payMap.put("appId", appId);
            payMap.put("timeStamp", WXPayUtil.getCurrentTimestamp()+"");
            payMap.put("nonceStr", WXPayUtil.generateNonceStr());
            payMap.put("signType", "MD5");
            payMap.put("package", "prepay_id=" + prepay_id);
            String paySign = WXPayUtil.generateSignature(payMap, paternerKey);
            payMap.put("paySign", paySign);
            String xml2 = WXPayUtil.mapToXml(payMap);//将所有参数(map)转xml格式
            System.out.println(xml2);
            return payMap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @RequestMapping(value = "/recharge", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, String> recharge(HttpServletRequest request, String totalFee) {
        SessionModel session = SessionUtil.getSession(request);

        User user = userService.getUserById(session.getUserId());

        if(user.getOpenId() == null) throw new InfoException(" OpenId不能为空, 请重新微信授权!");

        String appId = DataDictionary.Map.get("payment.wechat.mp.appId").getValue();
        String mchId = DataDictionary.Map.get("payment.wechat.mp.mchId").getValue();
        String notifyUrl = DataDictionary.Map.get("payment.wechat.notify").getValue();
        String paternerKey = DataDictionary.Map.get("payment.wechat.paternerKey").getValue();

        try {
            // 不带properties扩展名的文件名
            //ResourceBundle wx = ResourceBundle.getBundle("wx");
            // 统一下单 https://api.mch.weixin.qq.com/pay/unifiedorder
            String unifiedorder_url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
            //拼接统一下单地址参数
            Map<String, String> paraMap = new HashMap<String, String>();
            //获取请求ip地址
            String ip = request.getHeader("x-forwarded-for");
            if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
                ip = request.getHeader("Proxy-Client-IP");
            }
            if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
                ip = request.getRemoteAddr();
            }
            if(ip.indexOf(",")!=-1){
                String[] ips = ip.split(",");
                ip = ips[0].trim();
            }
            paraMap.put("appid", appId);
            paraMap.put("body", "充值金币");

            BizBody bizBody = new BizBody();
            bizBody.setChannel("recharge");
            bizBody.setUserId(session.getUserId());
            paraMap.put("attach", JsonUtil.getJsonNotEscape(bizBody));

            paraMap.put("mch_id", mchId);
            paraMap.put("nonce_str", WXPayUtil.generateNonceStr());
            paraMap.put("openid", user.getOpenId());
            paraMap.put("out_trade_no", IdWorker.getFlowIdWorkerInstance().nextId() + "");//订单号
            paraMap.put("spbill_create_ip", ip);
            paraMap.put("total_fee", AmountUtil.changeY2F(totalFee));
            paraMap.put("trade_type", "JSAPI");
            paraMap.put("notify_url", notifyUrl);// 此路径是微信服务器调用支付结果通知路径随意写
            String sign = WXPayUtil.generateSignature(paraMap, paternerKey);
            paraMap.put("sign", sign);
            String xml = WXPayUtil.mapToXml(paraMap);//将所有参数(map)转xml格式
            System.out.println(xml);
            String xmlStr = HttpUtil.postForm(unifiedorder_url, xml, null); //发送post请求"统一下单接口"返回预支付id:prepay_id
            System.out.println(xmlStr);

            //以下内容是返回前端页面的json数据
            String prepay_id = "";//预支付id
            if (xmlStr.indexOf("SUCCESS") != -1) {
                Map<String, String> map = WXPayUtil.xmlToMap(xmlStr);
                prepay_id = (String) map.get("prepay_id");
            }
            Map<String, String> payMap = new HashMap<String, String>();
            payMap.put("appId", appId);
            payMap.put("timeStamp", WXPayUtil.getCurrentTimestamp()+"");
            payMap.put("nonceStr", WXPayUtil.generateNonceStr());
            payMap.put("signType", "MD5");
            payMap.put("package", "prepay_id=" + prepay_id);
            String paySign = WXPayUtil.generateSignature(payMap, paternerKey);
            payMap.put("paySign", paySign);
            String xml2 = WXPayUtil.mapToXml(payMap);//将所有参数(map)转xml格式
            System.out.println(xml2);
            return payMap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    @RequestMapping("notify")
    public String callBack(HttpServletRequest request,HttpServletResponse response){
        //System.out.println("微信支付成功,微信发送的callback信息,请注意修改订单信息");
        String paternerKey = DataDictionary.Map.get("payment.wechat.paternerKey").getValue();
        InputStream is = null;
        try {
            is = request.getInputStream();//获取请求的流信息(这里是微信发的xml格式所有只能使用流来读)
            String xml = WXPayUtil.inputStream2String(is);
            Map<String, String> notifyMap = WXPayUtil.xmlToMap(xml);//将微信发的xml转map

            if(!WXPayUtil.isSignatureValid(notifyMap, paternerKey)){
                logger.error("微信支付回调验签失败:" + xml);
                response.getWriter().write("<xml><return_code><![CDATA[SUCCESS]]></return_code></xml>");
                is.close();
                return null;
            }

            if(notifyMap.get("return_code").equals("SUCCESS")){
                if(notifyMap.get("result_code").equals("SUCCESS")){
                    String ordersSn = notifyMap.get("out_trade_no");//商户订单号
                    String totalFee = notifyMap.get("total_fee");//实际支付的订单金额:单位 分
                    String amount = AmountUtil.changeF2Y(totalFee);//将分转换成元-实际支付金额:元
                    String openid = notifyMap.get("openid");  //如果有需要可以获取
                    String body = notifyMap.get("attach");  //如果有需要可以获取
                    String tradeNo = notifyMap.get("transaction_id");  //微信支付订单号
                    //String trade_type = notifyMap.get("trade_type");

                    //侧订单金额校验
                    Order order = orderService.getOrder(Long.valueOf(ordersSn));
                    if(order != null){
                        if(!Double.valueOf(amount).equals(order.getAmount())){
                            logger.error("微信支付回调订单金额不匹配:" + ordersSn);
                            response.getWriter().write("<xml><return_code><![CDATA[SUCCESS]]></return_code></xml>");
                            is.close();
                            return null;
                        }

                        /*以下是自己的业务处理------仅做参考
                         */
                        System.err.println("系统收到微信支付通知：" + ordersSn);


                       /* if(body != null){
                            BizBody model = JsonUtil.getModel(body, BizBody.class);
                            //充值金币
                            if (model.getChannel().equalsIgnoreCase("recharge")){
                                try{
                                    financeFacadeService.recharge(tradeNo, Long.valueOf(ordersSn), model.getUserId(), Double.valueOf(amount)
                                            , "finance.pays.channel.wechat");
                                }catch (Exception e){
                                    logger.error("wechat_notify_exception_recharge_" + e);
                                }
                            }else if(model.getChannel().equalsIgnoreCase("order")){
                                //订单消费
                                try {
                                    financeFacadeService.payment(model.getUserId(), Long.valueOf(ordersSn), "wxpay", amount);
                                }catch (Exception e){
                                    logger.error("wechat_notify_exception_payment_" + e);
                                }
                            }else if(model.getChannel().equalsIgnoreCase("vip")){
                                //充值会员
                                try {
                                    financeFacadeService.buyVip(model.getUserId(), Long.valueOf(ordersSn), "wxpay");
                                }catch (Exception e){
                                    logger.error("wechat_notify_exception_payment_" + e);
                                }
                            }
                        }*/


                    }else{
                        logger.error("微信支付回调订单不存在:" + ordersSn);
                        response.getWriter().write("<xml><return_code><![CDATA[SUCCESS]]></return_code></xml>");
                        is.close();
                        return null;
                    }

                }
            }

            //告诉微信服务器收到信息了，不要在调用回调action了========这里很重要回复微信服务器信息用流发送一个xml即可
            response.getWriter().write("<xml><return_code><![CDATA[SUCCESS]]></return_code></xml>");
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
