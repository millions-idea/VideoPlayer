/***
 * @pName Admin
 * @name PushMessageServiceImpl
 * @user HongWei
 * @date 2019/3/11
 * @desc
 */
package com.management.admin.biz.impl;

import com.management.admin.biz.IPushMessageService;
import com.management.admin.entity.db.PushMessage;
import com.management.admin.repository.PushMessageMapper;
import com.management.admin.utils.IdWorker;
import com.management.admin.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class PushMessageServiceImpl extends BaseServiceImpl<PushMessage> implements IPushMessageService {
    private final PushMessageMapper pushMessageMapper;

    @Autowired
    public PushMessageServiceImpl(PushMessageMapper pushMessageMapper) {
        this.pushMessageMapper = pushMessageMapper;
    }

    @Override
    public List<PushMessage> getMessages(Long userId) {
        return pushMessageMapper.selectByUser(userId);
    }

    @Override
    public boolean signal(Long messageId, Long userId) {
        return pushMessageMapper.signal(messageId, userId) > 0;
    }

    @Override
    public boolean sendGroupMessage(List<PushMessage> pushMessages) {
        pushMessages.forEach(item -> {
            try {
                item.setMessageId(IdWorker.getFlowIdWorkerInstance().nextId());
                item.setAddDate(new Date());
                item.setIsRead(0);
            } catch (Exception e) {
            }
        });
        return pushMessageMapper.inserUpdatetList(pushMessages) > 0;
    }

    @Override
    public boolean sendMessage(PushMessage pushMessage) {
        try {
            pushMessage.setMessageId(IdWorker.getFlowIdWorkerInstance().nextId());
            pushMessage.setAddDate(new Date());
            pushMessage.setIsRead(0);
        } catch (Exception e) {
        }
        return pushMessageMapper.insert(pushMessage) > 0;
    }

    @Override
    public void signalList(List<PushMessage> messages, Long userId) {
        List<String> collect = messages.stream().map(item -> item.getMessageId() + "").collect(Collectors.toList());
        String join = String.join(",", collect);
        pushMessageMapper.signalList(join, userId);
        System.out.println("signal");
    }
}
