/***
 * @pName Admin
 * @name YaBoBody
 * @user HongWei
 * @date 2019/3/31
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
public class YaBoRegisterBody {
   private String domain;
   private String name;
   private String password;
   private String password_confirmation;
   private String check1;
   private String i_code;
   private String code;
   private String XCodeId;
   private String uuid;
}
