/***
 * @pName Admin
 * @name Category
 * @user HongWei
 * @date 2019/3/9
 * @desc
 */
package com.management.admin.entity.db;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "tb_categorys")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    @Id
    private Integer categoryId ;

    private String text;

    /**
     * 父类目
     */
    private Integer parentId ;

    /**
     * 图标
     */
    private String image;

    private String className;
}
