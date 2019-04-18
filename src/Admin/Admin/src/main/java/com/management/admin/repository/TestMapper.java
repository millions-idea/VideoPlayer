/***
 * @pName Admin
 * @name TestMapper
 * @user HongWei
 * @date 2019/3/6
 * @desc
 */
package com.management.admin.repository;

import com.management.admin.entity.db.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface TestMapper extends MyMapper<User>{
    @Select("SELECT * FROM tb_users WHERE account = #{account}")
    User selectByAccount(@Param("account") String s);
}
