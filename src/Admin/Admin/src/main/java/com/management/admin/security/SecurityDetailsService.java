/***
 * @pName mi-ocr-web-app
 * @name UserDetailsServiceEx
 * @user HongWei
 * @date 2018/7/22
 * @desc
 */
package com.management.admin.security;


import com.management.admin.biz.IAdminUserService;
import com.management.admin.entity.db.AdminUser;
import com.management.admin.entity.db.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
public class SecurityDetailsService implements UserDetailsService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IAdminUserService adminUserService;

    @Override
    public UserDetails loadUserByUsername(String username) {
        AdminUser detail = adminUserService.getByAccount(username);
        return new SecurityUserDetails(detail, detail.getAccount(), detail.getAccount(), detail.getPassword(), AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_ADMIN"));
    }


}
