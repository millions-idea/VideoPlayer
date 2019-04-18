/***
 * @pName Admin
 * @name PushMessage
 * @user HongWei
 * @date 2019/3/11
 * @desc
 */
package com.management.admin.entity.db;

import lombok.*;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "tb_push_messages")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PushMessage {
    @Id
    private Long messageId ;

    private String message ;

    private String toUrl ;

    /**
     * 是否已阅
     */
    private Integer isRead ;

    private Date addDate ;

    private Date editDate ;

    private Long userId ;


}
