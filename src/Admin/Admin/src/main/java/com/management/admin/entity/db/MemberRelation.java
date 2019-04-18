/***
 * @pName Admin
 * @name MemberRelation
 * @user HongWei
 * @date 2019/3/6
 * @desc
 */
package com.management.admin.entity.db;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Table;

@Table(name = "tb_member_relations")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemberRelation {
    private Integer relationId ;

    /**
     * 用户id
     */
    private Integer userId ;

    /**
     * 上级用户id
     */
    private Integer parentId ;


}
