/***
 * @pName management
 * @name DictionaryService
 * @user DF
 * @date 2018/8/16
 * @desc
 */
package com.management.admin.biz;

import com.management.admin.entity.db.Dictionary;

import java.util.Map;

public interface IDictionaryService extends IBaseService<Dictionary> {
    boolean updateMap(Map<String, String[]> parameterMap);
}
