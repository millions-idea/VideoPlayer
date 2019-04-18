/***
 * @pName Admin
 * @name YaBoProfileDataResp
 * @user HongWei
 * @date 2019/4/1
 * @desc
 */
package com.management.admin.utils.yabo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class YaBoProfileDataResp {
    private String address;
    private String birth;
    private String completed_recharge;
    private String completed_record;
    private Date created_at;
    private String email;
    private String gender;
    private String grade;
    private Boolean has_bank;
    private Boolean has_qk_pwd;
    private Integer id;
    private Boolean is_daili;
    private String join_days;
    private String money;
    private String name;
    private Integer not_read_message_count;
    private String phone;
    private String real_name;
    private String token;
    private String userprofile;
    private String verify_type;
}
