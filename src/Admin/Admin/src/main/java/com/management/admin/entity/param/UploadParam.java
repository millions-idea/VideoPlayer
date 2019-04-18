/***
 * @pName Admin
 * @name UploadParam
 * @user HongWei
 * @date 2019/3/15
 * @desc
 */
package com.management.admin.entity.param;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UploadParam {
    private List<String> uploadFileList;
}
