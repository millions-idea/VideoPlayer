/***
 * @pName Admin
 * @name IAdminUserService
 * @user HongWei
 * @date 2019/3/7
 * @desc
 */
package com.management.admin.biz;

import com.management.admin.entity.db.AdminUser;

public interface IAdminUserService {
    /**
     * 管理员登录
     * @param account
     * @param password
     * @return
     */
    AdminUser login(String account, String password);

    /**
     * 管理员登录-根据account查询
     * @param username
     * @return
     */
    AdminUser getByAccount(String username);
}
