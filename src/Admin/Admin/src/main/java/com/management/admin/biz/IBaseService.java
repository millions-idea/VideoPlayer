/***
 * @pName management
 * @name BaseService
 * @user DF
 * @date 2018/8/13
 * @desc
 */
package com.management.admin.biz;

import java.util.List;

public interface IBaseService<T> {
    /**
     * 获取数据  2018年8月13日13:26:57
     * @return
     */
    T get(T param);

    /**
     * 获取全部数据  2018年8月13日13:26:57
     * @return
     */
    List<T> getList();

    /**
     * 分页获取全部数据  2018年8月13日13:27:17
     * @param page
     * @param limit
     * @param condition
     * @return
     */
    List<T> getLimit(Integer page, Integer limit, String condition);


    /**
     * 插入数据  2018年8月13日13:27:48
     * @param param
     * @return
     */
    int insert(T param);

    /**
     * 更新数据  2018年8月13日13:28:01
     * @param param
     * @return
     */
    int update(T param);

    /**
     * 删除数据  2018年8月13日13:28:16
     * @param param
     * @return
     */
    int delete(T param);
}
