/***
 * @pName Admin
 * @name AdminUserServiceImpl
 * @user HongWei
 * @date 2019/3/7
 * @desc
 */
package com.management.admin.biz.impl;

import com.management.admin.biz.IAdminUserService;
import com.management.admin.entity.db.AdminUser;
import com.management.admin.repository.AdminUserMapper;
import com.management.admin.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.security.provider.MD5;

@Service
public class AdminUserServiceImpl implements IAdminUserService {
    private final AdminUserMapper adminUserMapper;

    @Autowired
    public AdminUserServiceImpl(AdminUserMapper adminUserMapper) {
        this.adminUserMapper = adminUserMapper;
    }

    /**
     * 管理员登录
     *
     * @param account
     * @param password
     * @return
     */
    @Override
    public AdminUser login(String account, String password) {
        String encodePassword = MD5Util.md5(account + password);
        AdminUser adminUser = adminUserMapper.selectByPassword(account, encodePassword);
        return adminUser;
    }

    /**
     * 管理员登录-根据account查询
     *
     * @param username
     * @return
     */
    @Override
    public AdminUser getByAccount(String username) {
        AdminUser adminUser = adminUserMapper.selectByAccount(username);
        return adminUser;
    }
}
