/***
 * @pName Admin
 * @name AppVersion
 * @user HongWei
 * @date 2019/3/11
 * @desc
 */
package com.management.admin.entity.template;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppVersion {
    private String version;
    private String wgtUrl;
    private String pkgUrl;
    private String node;
    private Boolean showCancel;
}
