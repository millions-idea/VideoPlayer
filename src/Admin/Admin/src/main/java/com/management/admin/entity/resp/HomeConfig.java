/***
 * @pName Admin
 * @name HomeConfig
 * @user HongWei
 * @date 2019/3/11
 * @desc
 */
package com.management.admin.entity.resp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HomeConfig {
    private String notice;
    private List<Banner> bannerList;
    private String businessSchoolLink;
    private String agentHelpLink;
    private String commissionSystemLink;
    private String appDownloadLink;
}
