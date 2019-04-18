/***
 * @pName Admin
 * @name PushMessageMapper
 * @user HongWei
 * @date 2019/3/11
 * @desc
 */
package com.management.admin.repository;

import com.management.admin.entity.db.PushMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface PushMessageMapper extends MyMapper<PushMessage> {
    @Select("SELECT * FROM tb_push_messages WHERE user_id = #{userId} AND is_read = 0 ORDER BY add_date DESC LIMIT 10")
    List<PushMessage> selectByUser(@Param("userId") Long userId);

    @Update("UPDATE tb_push_messages SET is_read=1,edit_date=NOW() WHERE message_id =#{messageId} AND user_id=#{userId}")
    int signal(@Param("messageId") Long messageId, @Param("userId") Long userId);

    @Update("UPDATE tb_push_messages SET is_read=1,edit_date=NOW() WHERE message_id IN(${messageIdList}) AND user_id=#{userId}")
    int signalList(@Param("messageIdList") String messageIdList, @Param("userId") Long userId);
}
