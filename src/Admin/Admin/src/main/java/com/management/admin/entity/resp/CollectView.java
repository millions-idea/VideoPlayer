/***
 * @pName Admin
 * @name CollectView
 * @user HongWei
 * @date 2019/3/22
 * @desc
 */
package com.management.admin.entity.resp;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.joda.time.DateTime;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CollectView {
    private Integer collectId;
    private String productId;
    private String productName;
    private String image;
    private Integer showCount;
    private String userId;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private DateTime collectDate;

    private Double amount ;
    private Integer categoryId ;

}
