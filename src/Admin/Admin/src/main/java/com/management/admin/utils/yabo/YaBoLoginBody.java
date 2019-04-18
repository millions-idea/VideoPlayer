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

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class YaBoLoginBody {
   private String name;
   private String password;
   private String domain;
}
