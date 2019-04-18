/***
 * @pName Admin
 * @name AppUpdate
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
public class AppUpdate {
    /**
     * wgt 包的下载地址，用于 wgt 方式更新。
     */
    private String wgtUrl;

    /**
     * apk/ipa 包的下载地址或 AppStore 地址，用于整包升级的方式。
     */
    private String pkgUrl;


    /**
     * 是否有更新
     */
    private Boolean update;

    /**
     * 更新说明
     */
    private String node;

    /**
     * 非强更
     */
    private Boolean showCancel;
}
