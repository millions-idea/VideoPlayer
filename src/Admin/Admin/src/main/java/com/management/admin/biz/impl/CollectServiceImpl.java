/***
 * @pName Admin
 * @name CollectServiceImpl
 * @user HongWei
 * @date 2019/3/22
 * @desc
 */
package com.management.admin.biz.impl;

import com.management.admin.biz.ICollectService;
import com.management.admin.entity.dbExt.CollectDetailInfo;
import com.management.admin.entity.resp.CollectView;
import com.management.admin.repository.CollectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CollectServiceImpl implements ICollectService{
    private final CollectMapper collectMapper;

    @Autowired
    public CollectServiceImpl(CollectMapper collectMapper) {
        this.collectMapper = collectMapper;
    }

    @Override
    public List<CollectView> getLimit(Long userId, Integer page) {
        String size =  "10";
        if(page == null) page = 0;
        List<CollectView> list = collectMapper.selectLimitByUid(userId, page, size);
        return list;
    }

    @Override
    public Integer getTotalPage(Long userId) {
        int count = collectMapper.selectTotalPageByUid(userId);
        Integer size = count / 10;
        if(count < 10) size = 1;
        return size < 0 ? 1000 : size;
    }
}
