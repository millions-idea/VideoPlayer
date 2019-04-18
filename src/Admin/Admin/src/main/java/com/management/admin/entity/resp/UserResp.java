/***
 * @pName management
 * @name UsersView
 * @user HongWei
 * @date 2018/8/14
 * @desc
 */
package com.management.admin.entity.resp;

import com.fasterxml.jackson.annotation.JsonView;
import com.management.admin.utils.StringUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResp {
    public interface SampleView {}
    public interface FinanceView extends SampleView {}
    public interface SecurityView extends SampleView  {}

    private Long userId;
    @JsonView(FinanceView.class)
    private String account;
    @JsonView(SecurityView.class)
    private String phone;
    @JsonView(SecurityView.class)
    private String token;
    @JsonView(SecurityView.class)
    private String financeId;
    @JsonView(SecurityView.class)
    private String financeName;

    @JsonView(FinanceView.class)
    private Double balance;
    @JsonView(FinanceView.class)
    private Double canWithdrawAmount;
    @JsonView(FinanceView.class)
    private Double canNotWithdrawAmount;

    @JsonView(SecurityView.class)
    public String getPhone() {
        String encryptPhoneBefore = phone.substring(0,3);
        String encryptPhoneAfter = phone.substring(9,11);
        String encryptPhone = StringUtil.padLeft(encryptPhoneBefore, 9, '*').concat(encryptPhoneAfter);
        return encryptPhone;
    }


    @JsonView(FinanceView.class)
    /**
     * 总收入
     */
    private Double incomeAmount;


    @JsonView(FinanceView.class)
    /**
     * 总支出
     */
    private Double expendAmount;

}
