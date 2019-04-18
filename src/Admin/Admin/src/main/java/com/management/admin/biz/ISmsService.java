/***
 * @pName management
 * @name SmsService
 * @user HongWei
 * @date 2018/8/13
 * @desc
 */
package com.management.admin.biz;

public interface ISmsService {
    boolean sendRegCode(String phone);

    boolean sendResetPwdSmsCode(String phone);

    boolean sendChangePhoneSmsCode(String phone);

    boolean sendPaymentSmsCode(String phone);

    boolean sendPasswordSmsCode(String phone);
}
