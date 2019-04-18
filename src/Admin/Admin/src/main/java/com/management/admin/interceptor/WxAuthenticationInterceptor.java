/***
 * @pName proback
 * @name FinanceAuthenticationInterceptor
 * @user DF
 * @date 2018/8/5
 * @desc
 */
package com.management.admin.interceptor;

import com.management.admin.annotaion.WxSign;
import com.management.admin.entity.template.Constant;
import com.management.admin.exception.InfoException;
import com.management.admin.utils.Base64Util;
import com.management.admin.utils.RSAUtil;
import com.management.admin.utils.RequestUtil;
import com.management.admin.utils.wxOpen.WxSignUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/***
 * 微信验签拦截器
 */
public class WxAuthenticationInterceptor implements HandlerInterceptor {
    /**
     * 控制验签系统开闭
     */
    private final Boolean isOpen;

    public WxAuthenticationInterceptor(Boolean isOpen) {
        this.isOpen = isOpen;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (isOpen){
            // 只拦截method级别的处理器
            if (!(handler instanceof HandlerMethod)) return true;
            // 只拦截token注解过的方法
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            // 判断接口是否需要验签
            WxSign signAnnotation = method.getAnnotation(WxSign.class);
            if (signAnnotation != null){
                String url = RequestUtil.getParameters(request);

                if (StringUtils.isNotBlank(request.getParameter("signature"))) {
                    String signature = request.getParameter("signature");
                    String timestamp = request.getParameter("timestamp");
                    String nonce = request.getParameter("nonce");
                    String echostr = request.getParameter("echostr");
                    //LOGGER.info("signature[{}], timestamp[{}], nonce[{}], echostr[{}]", signature, timestamp, nonce, echostr);
                    if (WxSignUtil.checkSignature(signature, timestamp, nonce)) {
                        System.err.println(url + "数据源为微信后台，将echostr["  + echostr +  "]返回！");
                        return true;
                    }else{
                        throw new InfoException("验签错误");
                    }
                }else{
                    throw new InfoException("验签失败");
                }


            }
        }
        return true;
    }



    /**
     * 快速加密
     * @param body
     * @return
     * @throws Exception
     */
    private String getEncrypt(String body) throws Exception {
        // formUid=1&toUid=2&amount=1.9&remark=充值&token=
        byte[] bytes = RSAUtil.encryptByPublicKey(body.getBytes(), Constant.INFO_PUB_KEY);
        return  Base64Util.encode(bytes);
    }

    /**
     * 快速解密
     * @param body
     * @return
     * @throws Exception
     */
    private String getDecrypt(String body) throws Exception {
        byte[] decode = Base64Util.decode(body);
        return new String(RSAUtil.decryptByPrivateKey(decode, Constant.INFO_PRI_KEY), "UTF-8");
    }
}
