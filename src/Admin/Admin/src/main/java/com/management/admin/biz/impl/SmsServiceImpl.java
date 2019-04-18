/***
 * @pName management
 * @name SmServiceImpl
 * @user HongWei
 * @date 2018/8/13
 * @desc
 */
package com.management.admin.biz.impl;

import com.aliyuncs.exceptions.ClientException;
import com.management.admin.biz.ISmsService;
import com.management.admin.entity.template.DataDictionary;
import com.management.admin.utils.IdWorker;
import com.management.admin.utils.RandomUtils;
import com.management.admin.utils.http.AliSmsUtil;
import com.management.admin.utils.http.TencentSmsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class SmsServiceImpl implements ISmsService {
    private final Logger logger = LoggerFactory.getLogger(SmsServiceImpl.class);
    private final RedisTemplate redisTemplate;

    @Autowired
    public SmsServiceImpl(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 发送手机用户注册验证码 DF 2018年11月27日00:47:18
     *
     * @param phone 手机号码 15000000000, 15000000001... 1000+
     * @return
     */
    @Override
    public boolean sendRegCode(String phone) {
        long randomCode = 0;
        boolean sendResult = false;

        try {
            // 生成纯数字随机验证码
            /*randomCode = IdWorker.getFlowIdWorkerInstance().nextId();
            randomCode = Long.parseLong((randomCode + "").substring(0, 6));*/
            randomCode = Long.parseLong(RandomUtils.generateNumberString(6));

            // 调用短信服务中心远程接口发送验证码到客户端手机
            //sendResult = AliSmsUtil.sendMessage(username, DataDictionary.Map.get("sms.signature").getValue(), DataDictionary.Map.get("sms.reg.templateCode").getValue(), String.format("{\"code\":\"%s\"}", randomCode), username);
            sendResult = TencentSmsUtil.sendMessage(phone, Integer.parseInt(DataDictionary.Map.get("sms.reg.templateCode").getValue()), String.valueOf(randomCode), "5");

            // 存储正确结果到缓存服务器中, 有效期5分钟
            if(sendResult){
                redisTemplate.opsForValue().set("reg-sms-" + phone, randomCode + "", 5, TimeUnit.MINUTES);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(!sendResult)
            logger.error("SmsServiceImpl_sendRegCode()_发送失败_" + phone);

        return sendResult;
    }

    /**
     * 发送找回密码短信验证码 DF 2018年12月7日02:06:02
     *
     * @param phone 手机号码 15000000000 必填
     * @return
     */
    @Override
    public boolean sendResetPwdSmsCode(String phone) {
        long randomCode = 0;
        boolean sendResult = false;

        try {
            // 生成纯数字随机验证码
            /*randomCode = IdWorker.getFlowIdWorkerInstance().nextId();
            randomCode = Long.parseLong((randomCode + "").substring(0, 6));*/
            randomCode = Long.parseLong(RandomUtils.generateNumberString(6));

            // 调用短信服务中心远程接口发送验证码到客户端手机
            //sendResult = AliSmsUtil.sendMessage(username, DataDictionary.Map.get("sms.signature").getValue(), DataDictionary.Map.get("sms.reset.templateCode").getValue(), String.format("{\"code\":\"%s\"}", randomCode), username);
            sendResult = TencentSmsUtil.sendMessage(phone, Integer.parseInt(DataDictionary.Map.get("sms.reset.templateCode").getValue()), String.valueOf(randomCode), "5");

            // 存储正确结果到缓存服务器中, 有效期5分钟
            if(sendResult){
                redisTemplate.opsForValue().set("reset-sms-" + phone, randomCode + "", 5, TimeUnit.MINUTES);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(!sendResult)
            logger.error("SmsServiceImpl_sendResetPwdSmsCode()_发送失败_" + phone);

        return sendResult;
    }

    @Override
    public boolean sendChangePhoneSmsCode(String phone) {
        long randomCode = 0;
        boolean sendResult = false;

        try {
            // 生成纯数字随机验证码
            /*randomCode = IdWorker.getFlowIdWorkerInstance().nextId();
            randomCode = Long.parseLong((randomCode + "").substring(0, 6));*/
            randomCode = Long.parseLong(RandomUtils.generateNumberString(6));

            // 调用短信服务中心远程接口发送验证码到客户端手机
            //sendResult = AliSmsUtil.sendMessage(username, DataDictionary.Map.get("sms.signature").getValue(), DataDictionary.Map.get("sms.bindPhone.templateCode").getValue(), String.format("{\"code\":\"%s\"}", randomCode), username);
            sendResult = TencentSmsUtil.sendMessage(phone, Integer.parseInt(DataDictionary.Map.get("sms.bindPhone.templateCode").getValue()), String.valueOf(randomCode), "5");

            // 存储正确结果到缓存服务器中, 有效期5分钟
            if(sendResult){
                redisTemplate.opsForValue().set("change-sms-" + phone, randomCode + "", 5, TimeUnit.MINUTES);
            }
        }  catch (Exception e) {
            e.printStackTrace();
        }

        if(!sendResult)
            logger.error("SmsServiceImpl_sendChangePhoneSmsCode()_发送失败_" + phone);

        return sendResult;
    }

    @Override
    public boolean sendPaymentSmsCode(String phone) {
        long randomCode = 0;
        boolean sendResult = false;

        try {
            // 生成纯数字随机验证码
            /*randomCode = IdWorker.getFlowIdWorkerInstance().nextId();
            randomCode = Long.parseLong((randomCode + "").substring(0, 6));*/
            randomCode = Long.parseLong(RandomUtils.generateNumberString(6));

            // 调用短信服务中心远程接口发送验证码到客户端手机
            //sendResult = AliSmsUtil.sendMessage(username, DataDictionary.Map.get("sms.signature").getValue(), DataDictionary.Map.get("sms.payment.templateCode").getValue(), String.format("{\"code\":\"%s\"}", randomCode), username);
            sendResult = TencentSmsUtil.sendMessage(phone, Integer.parseInt(DataDictionary.Map.get("sms.payment.templateCode").getValue()), String.valueOf(randomCode), "5");

            // 存储正确结果到缓存服务器中, 有效期5分钟
            if(sendResult){
                redisTemplate.opsForValue().set("payment-sms-" + phone, randomCode + "", 5, TimeUnit.MINUTES);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(!sendResult)
            logger.error("SmsServiceImpl_sendChangePhoneSmsCode()_发送失败_" + phone);

        return sendResult;
    }

    @Override
    public boolean sendPasswordSmsCode(String phone) {
        long randomCode = 0;
        boolean sendResult = false;

        try {
            // 生成纯数字随机验证码
            /*randomCode = IdWorker.getFlowIdWorkerInstance().nextId();
            randomCode = Long.parseLong((randomCode + "").substring(0, 6));*/
            randomCode = Long.parseLong(RandomUtils.generateNumberString(6));

            // 调用短信服务中心远程接口发送验证码到客户端手机
            //sendResult = AliSmsUtil.sendMessage(username, DataDictionary.Map.get("sms.signature").getValue(), DataDictionary.Map.get("sms.payment.templateCode").getValue(), String.format("{\"code\":\"%s\"}", randomCode), username);
            sendResult = TencentSmsUtil.sendMessage(phone, Integer.parseInt(DataDictionary.Map.get("sms.operation.templateCode").getValue()), String.valueOf(randomCode), "5");

            // 存储正确结果到缓存服务器中, 有效期5分钟
            if(sendResult){
                redisTemplate.opsForValue().set("password-sms-" + phone, randomCode + "", 5, TimeUnit.MINUTES);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(!sendResult)
            logger.error("SmsServiceImpl_sendPasswordSmsCode()_发送失败_" + phone);

        return sendResult;
    }
}
