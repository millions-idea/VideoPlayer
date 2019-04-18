/***
 * @pName management
 * @name DictionaryServiceImpl
 * @user DF
 * @date 2018/8/16
 * @desc
 */
package com.management.admin.biz.impl;

import com.management.admin.biz.IDictionaryService;
import com.management.admin.entity.db.Dictionary;
import com.management.admin.entity.template.AppVersion;
import com.management.admin.repository.DictionaryMapper;
import com.management.admin.utils.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DictionaryServiceImpl extends BaseServiceImpl<Dictionary> implements IDictionaryService {
    private final DictionaryMapper dictionaryMapper;

    @Autowired
    public DictionaryServiceImpl(DictionaryMapper dictionaryMapper) {
        this.dictionaryMapper = dictionaryMapper;
    }


    /**
     * 查询数据字典信息列表 2018年10月31日21:46:59
     *
     * @return
     */
    @Override
    public List<Dictionary> getList() {
        return dictionaryMapper.selectAll();
    }

    @Override
    public boolean updateMap(Map<String, String[]> parameterMap) {
        StringBuffer buffer = new StringBuffer();
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue()[0].toString());
            switch (entry.getKey()){
                case "homeBanner1":
                    buffer.append("UPDATE tb_dictionary SET `value`='" + entry.getValue()[0].toString() + "' WHERE `key` = " + "'link.img.homeBanner1';");
                    break;
                case "homeBanner2":
                    buffer.append("UPDATE tb_dictionary SET `value`='" + entry.getValue()[0].toString() + "' WHERE `key` = " + "'link.img.homeBanner2';");
                    break;
                case "homeBanner3":
                    buffer.append("UPDATE tb_dictionary SET `value`='" + entry.getValue()[0].toString() + "' WHERE `key` = " + "'link.img.homeBanner3';");
                    break;
                case "linkTextHomeBanner1":
                    buffer.append("UPDATE tb_dictionary SET `value`='" + entry.getValue()[0].toString() + "' WHERE `key` = " + "'link.text.homeBanner1';");
                    break;
                case "linkTextHomeBanner2":
                    buffer.append("UPDATE tb_dictionary SET `value`='" + entry.getValue()[0].toString() + "' WHERE `key` = " + "'link.text.homeBanner2';");
                    break;
                case "linkTextHomeBanner3":
                    buffer.append("UPDATE tb_dictionary SET `value`='" + entry.getValue()[0].toString() + "' WHERE `key` = " + "'link.text.homeBanner3';");
                    break;
                case "smsTencentConfigAppId":
                    buffer.append("UPDATE tb_dictionary SET `value`='" + entry.getValue()[0].toString() + "' WHERE `key` = " + "'sms.tencent.config.appId';");
                    break;
                case "smsTencentConfigAppKey":
                    buffer.append("UPDATE tb_dictionary SET `value`='" + entry.getValue()[0].toString() + "' WHERE `key` = " + "'sms.tencent.config.appKey';");
                    break;
                case "smsTencentConfigSmsSign":
                    buffer.append("UPDATE tb_dictionary SET `value`='" + entry.getValue()[0].toString() + "' WHERE `key` = " + "'sms.tencent.config.smsSign';");
                    break;
                case "smsRegTemplateCode":
                    buffer.append("UPDATE tb_dictionary SET `value`='" + entry.getValue()[0].toString() + "' WHERE `key` = " + "'sms.reg.templateCode';");
                    break;
                case "smsResetTemplateCode":
                    buffer.append("UPDATE tb_dictionary SET `value`='" + entry.getValue()[0].toString() + "' WHERE `key` = " + "'sms.reset.templateCode';");
                    break;
                case "smsBindPhoneTemplateCode":
                    buffer.append("UPDATE tb_dictionary SET `value`='" + entry.getValue()[0].toString() + "' WHERE `key` = " + "'sms.bindPhone.templateCode';");
                    break;
                case "smsPaymentTemplateCode":
                    buffer.append("UPDATE tb_dictionary SET `value`='" + entry.getValue()[0].toString() + "' WHERE `key` = " + "'sms.payment.templateCode';");
                    break;
                case "cosBucket":
                    buffer.append("UPDATE tb_dictionary SET `value`='" + entry.getValue()[0].toString() + "' WHERE `key` = " + "'cos.bucket';");
                    break;
                case "cosSecretId":
                    buffer.append("UPDATE tb_dictionary SET `value`='" + entry.getValue()[0].toString() + "' WHERE `key` = " + "'cos.secretId';");
                    break;
                case "cosSecretKey":
                    buffer.append("UPDATE tb_dictionary SET `value`='" + entry.getValue()[0].toString() + "' WHERE `key` = " + "'cos.secretKey';");
                    break;
                case "cosAccessUrl":
                    buffer.append("UPDATE tb_dictionary SET `value`='" + entry.getValue()[0].toString() + "' WHERE `key` = " + "'cos.accessUrl';");
                    break;
                case "cosRegion":
                    buffer.append("UPDATE tb_dictionary SET `value`='" + entry.getValue()[0].toString() + "' WHERE `key` = " + "'cos.region';");
                    break;
                case "paymentAlipayAppId":
                    buffer.append("UPDATE tb_dictionary SET `value`='" + entry.getValue()[0].toString() + "' WHERE `key` = " + "'payment.alipay.appId';");
                    break;
                case "paymentAlipaySecret":
                    buffer.append("UPDATE tb_dictionary SET `value`='" + entry.getValue()[0].toString() + "' WHERE `key` = " + "'payment.alipay.secret';");
                    break;
                case "paymentAlipayChannel":
                    buffer.append("UPDATE tb_dictionary SET `value`='" + entry.getValue()[0].toString() + "' WHERE `key` = " + "'payment.alipay.channel';");
                    break;
                case "productPricePermanent":
                    buffer.append("UPDATE tb_dictionary SET `value`='" + entry.getValue()[0].toString() + "' WHERE `key` = " + "'product.price.permanent';");
                    break;
                case "productDescCommon":
                    buffer.append("UPDATE tb_dictionary SET `value`='" + entry.getValue()[0].toString() + "' WHERE `key` = " + "'product.desc.common';");
                    break;
                case "rateZhixiaojiangjin":
                    buffer.append("UPDATE tb_dictionary SET `value`='" + entry.getValue()[0].toString() + "' WHERE `key` = " + "'rate.zhixiaojiangjin';");
                    break;
                case "rateQuyudailijiangjin":
                    buffer.append("UPDATE tb_dictionary SET `value`='" + entry.getValue()[0].toString() + "' WHERE `key` = " + "'rate.quyudailijiangjin';");
                    break;
                case "rateGaojidailijiangjin":
                    buffer.append("UPDATE tb_dictionary SET `value`='" + entry.getValue()[0].toString() + "' WHERE `key` = " + "'rate.gaojidailijiangjin';");
                    break;
                case "rateGudongjiangjin":
                    buffer.append("UPDATE tb_dictionary SET `value`='" + entry.getValue()[0].toString() + "' WHERE `key` = " + "'rate.gudongjiangjin';");
                    break;
                case "rateTjChujidaili":
                    buffer.append("UPDATE tb_dictionary SET `value`='" + entry.getValue()[0].toString() + "' WHERE `key` = " + "'rate.tj.chujidaili';");
                    break;
                case "rateTjQuyudaili":
                    buffer.append("UPDATE tb_dictionary SET `value`='" + entry.getValue()[0].toString() + "' WHERE `key` = " + "'rate.tj.quyudaili';");
                    break;
                case "rateTjGaojidaili":
                    buffer.append("UPDATE tb_dictionary SET `value`='" + entry.getValue()[0].toString() + "' WHERE `key` = " + "'rate.tj.gaojidaili';");
                    break;
                case "rateTjGudong":
                    buffer.append("UPDATE tb_dictionary SET `value`='" + entry.getValue()[0].toString() + "' WHERE `key` = " + "'rate.tj.gudong';");
                    break;
                case "linkYabo":
                    buffer.append("UPDATE tb_dictionary SET `value`='" + entry.getValue()[0].toString() + "' WHERE `key` = " + "'link.yabo';");
                    break;
                case "linkAppYabo":
                    buffer.append("UPDATE tb_dictionary SET `value`='" + entry.getValue()[0].toString() + "' WHERE `key` = " + "'link.app.yabo';");
                    break;
                case "linkAppDownload":
                    buffer.append("UPDATE tb_dictionary SET `value`='" + entry.getValue()[0].toString() + "' WHERE `key` = " + "'link.app.download';");
                    break;
            }
        }

        String version = parameterMap.get("version")[0];
        String wgtUrl = parameterMap.get("wgtUrl")[0];
        String pkgUrl = parameterMap.get("pkgUrl")[0];
        String node = parameterMap.get("node")[0];
        AppVersion appVersion = new AppVersion(version, wgtUrl, pkgUrl, node, false);
        buffer.append("UPDATE tb_dictionary SET `value`='" + JsonUtil.getJsonNotEscape(appVersion) + "' WHERE `key` = '" + "app.version" +  "';");

        dictionaryMapper.execute(buffer.toString());
        return true;
    }
}
