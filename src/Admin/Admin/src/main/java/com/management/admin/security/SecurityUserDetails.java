/***
 * @pName mi-ocr-web-app
 * @name UserDetailsEx
 * @user HongWei
 * @date 2018/7/28
 * @desc
 */
package com.management.admin.security;

import com.management.admin.entity.db.AdminUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class SecurityUserDetails extends User {
    private String salt;
    private AdminUser detail;

    public AdminUser getDetail() {
        return detail;
    }

    public void setDetail(AdminUser detail) {
        this.detail = detail;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public SecurityUserDetails(AdminUser detail, String username, String salt, String password,
                               Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.detail = detail;
        this.salt = salt;
    }
}
