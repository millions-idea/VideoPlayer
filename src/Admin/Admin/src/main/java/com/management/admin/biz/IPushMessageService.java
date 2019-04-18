/***
 * @pName Admin
 * @name IPushMessageService
 * @user HongWei
 * @date 2019/3/11
 * @desc
 */
package com.management.admin.biz;

import com.management.admin.entity.db.PushMessage;

import java.util.List;

public interface IPushMessageService extends IBaseService<PushMessage> {
    List<PushMessage> getMessages(Long userId);

    boolean signal(Long messageId, Long userId);

    boolean sendGroupMessage(List<PushMessage> pushMessages);

    boolean sendMessage(PushMessage pushMessage);

    void signalList(List<PushMessage> messages, Long userId);
}
