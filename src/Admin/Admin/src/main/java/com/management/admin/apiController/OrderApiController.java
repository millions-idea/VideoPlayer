/***
 * @pName Admin
 * @name OrderApiController
 * @user HongWei
 * @date 2019/3/8
 * @desc
 */
package com.management.admin.apiController;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.management.admin.biz.IOrderService;
import com.management.admin.entity.db.Order;
import com.management.admin.entity.param.BizBody;
import com.management.admin.entity.param.OrderParam;
import com.management.admin.entity.resp.OrderView;
import com.management.admin.entity.template.DataDictionary;
import com.management.admin.entity.template.JsonArrayResult;
import com.management.admin.entity.template.JsonResult;
import com.management.admin.entity.template.SessionModel;
import com.management.admin.exception.InfoException;
import com.management.admin.facade.IFinanceFacadeService;
import com.management.admin.utils.IdWorker;
import com.management.admin.utils.JsonUtil;
import com.management.admin.utils.alipay.AliPayUtil;
import com.management.admin.utils.hupay.HuPayParams;
import com.management.admin.utils.hupay.HuPayUtil;
import com.management.admin.utils.web.SessionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping("/api/order")
public class OrderApiController {
    @Autowired
    private IFinanceFacadeService financeFacadeService;

    @Autowired
    private IOrderService orderService;

    private final Logger logger = LoggerFactory.getLogger(OrderApiController.class);

    /**
     * 生成充值订单参数信息
     * @param request
     * @param orderParam
     * @return
     * @throws Exception
     */
    @GetMapping("/getRechargeOrderInfo")
    public String getRechargeOrderInfo(HttpServletRequest request, OrderParam orderParam) throws Exception {
        SessionModel session = SessionUtil.getSession(request);
        BizBody bizBody = new BizBody();
        bizBody.setChannel("recharge");
        bizBody.setUserId(session.getUserId());
        String params = AliPayUtil.getAppPaymentParams(IdWorker.getFlowIdWorkerInstance().nextId()
                , JsonUtil.getJsonNotEscape(bizBody)
                , "金币"
                , Double.valueOf(orderParam.getAmount()));
        return params;
    }

    @GetMapping("/notifyOrder")
    public JsonResult notifyOrder(HttpServletRequest request, String orderId){
        SessionModel session = SessionUtil.getSession(request);
        orderService.notifyOrder(Long.valueOf(orderId), session.getUserId());
        return JsonResult.successful();
    }

    @GetMapping("/buy")
    public JsonResult buy(HttpServletRequest request, String id, String channel, Double amount){
        SessionModel session = SessionUtil.getSession(request);
        Long orderId = orderService.createOrder(session.getUserId(), Long.valueOf(id), channel, Double.valueOf(amount));
        if(orderId != null && orderId > 0) return new JsonResult().successful(orderId + "");
        return JsonResult.failing();
    }

    @GetMapping("/payment")
    public JsonResult payment(HttpServletRequest request, String orderId, String channel) throws Exception {
        SessionModel session = SessionUtil.getSession(request);
        if(financeFacadeService.payment(session.getUserId(), Long.valueOf(orderId), channel)){
            return JsonResult.successful();
        }
        return JsonResult.failing();
    }

    @GetMapping("/getOrders")
    public JsonArrayResult<OrderView> getTeamMembers(HttpServletRequest request, @RequestParam(required = false) Integer page){
        SessionModel session = SessionUtil.getSession(request);
        List<OrderView> list = orderService.getOrderLimit(session.getUserId(), page);
        Integer maxPage = orderService.getOrderTotalPage(session.getUserId());
        list.forEach(item -> {
            if (item.getChannel().equalsIgnoreCase("balance")){
                item.setChannelText("余额支付");
            }
            if (item.getChannel().equalsIgnoreCase("alipay")){
                item.setChannelText("支付宝");
            }
            if (item.getChannel().equalsIgnoreCase("wxpay")){
                item.setChannelText("微信支付");
            }
        });
        JsonArrayResult jsonArrayResult = new JsonArrayResult(0, list);
        jsonArrayResult.setCount(maxPage == null ? 1000 : maxPage);
        return jsonArrayResult;
    }

    @PostMapping("/notify")
    public String notify(HttpServletRequest request){
        //获取支付宝POST过来反馈信息
        Map<String,String> params = new HashMap<String,String>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }
        try {
            boolean flag = AlipaySignature.rsaCheckV1(params, AliPayUtil.ALIPAY_PUBLIC_KEY, AliPayUtil.CHARSET,"RSA2");
           if(flag){
               String trade_order_id = params.get("trade_order_id");//支付宝交易号
               Double totalAmount = Double.valueOf(params.get("total_amount"));//本次交易支付的订单金额，单位为人民币（分）
               String transaction_id = params.get("transaction_id");
               String plugins = params.get("plugins");
               String hash = params.get("hash");
               String body = params.get("body");

                if(body != null){
                    BizBody model = JsonUtil.getModel(body, BizBody.class);
                    //充值金币
                    if (model.getChannel().equalsIgnoreCase("recharge")){
                        try{
                            financeFacadeService.recharge(trade_order_id, Long.valueOf(transaction_id), model.getUserId(), Double.valueOf(totalAmount)
                                    , "finance.pays.channel.alipay");
                        }catch (Exception e){
                            logger.error("alipay_notify_exception_recharge_" + e);
                        }
                    }else if(model.getChannel().equalsIgnoreCase("order")){
                        //订单消费
                        try {
                            financeFacadeService.payment(model.getUserId(), Long.valueOf(transaction_id), "alipay");
                        }catch (Exception e){
                            logger.error("alipay_notify_exception_payment_" + e);
                        }
                    }
                }
               return "success";
           }
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/huPayment")
    public String huPayment(HttpServletRequest request, OrderParam orderParam) throws Exception {
        SessionModel session = SessionUtil.getSession(request);
        BizBody bizBody = new BizBody();
        bizBody.setChannel("order");
        bizBody.setUserId(session.getUserId());
        Order order = orderService.getOrder(Long.valueOf(orderParam.getOutTradeNo()));
        if(order == null) throw new InfoException("订单不存在");

        HuPayParams params = new HuPayParams();
        params.setTitle("购买商品-" + order.getProductName());
        params.setExt(bizBody);
        params.setAmount(Double.valueOf(order.getAmount()));
        params.setTradeOrderId(orderParam.getOutTradeNo());

        String url = HuPayUtil.payment(params.getTradeOrderId(), params.getTitle(), params.getAmount(), JsonUtil.getJsonNotEscape(params.getExt()));

        return url;
    }

}
