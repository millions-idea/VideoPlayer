/***
 * @pName Admin
 * @name AdminUserMapper
 * @user HongWei
 * @date 2019/3/7
 * @desc
 */
package com.management.admin.repository;

import com.management.admin.entity.db.AdminUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AdminUserMapper extends MyMapper<AdminUser> {
    @Select("SELECT * FROM tb_admin_users WHERE account = #{account} AND password = #{encodePassword}")
    AdminUser selectByPassword(@Param("account") String account, @Param("encodePassword") String encodePassword);

    @Select("SELECT * FROM tb_admin_users WHERE account = #{account}")
    AdminUser selectByAccount(@Param("account") String account);
}
