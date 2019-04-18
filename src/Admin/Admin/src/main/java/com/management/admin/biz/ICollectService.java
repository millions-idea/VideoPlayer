/***
 * @pName Admin
 * @name ICollectService
 * @user HongWei
 * @date 2019/3/22
 * @desc
 */
package com.management.admin.biz;

import com.management.admin.entity.dbExt.CollectDetailInfo;
import com.management.admin.entity.resp.CollectView;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ICollectService {

    List<CollectView> getLimit(Long userId,Integer page);

    Integer getTotalPage(Long userId);
}
