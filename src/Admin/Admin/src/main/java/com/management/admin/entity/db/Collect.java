/***
 * @pName Admin
 * @name Collect
 * @user HongWei
 * @date 2019/3/22
 * @desc
 */
package com.management.admin.entity.db;

import lombok.*;
import org.joda.time.DateTime;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "tb_collects")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Collect {
    @Id
    private Integer collectId;
    private Long productId;
    private Long userId;
    private Date addDate;
}
