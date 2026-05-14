package com.fank.f1k2.business.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fank.f1k2.business.entity.DiscussionRoom;
import com.fank.f1k2.business.dao.DiscussionRoomMapper;
import com.fank.f1k2.business.service.IDiscussionRoomService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * @author FanK fan1ke2ke@gmail.com（悲伤的橘子树）
 */
@Service
public class DiscussionRoomServiceImpl extends ServiceImpl<DiscussionRoomMapper, DiscussionRoom> implements IDiscussionRoomService {

    /**
     * 分页获取讨论房间表
     *
     * @param page      分页对象
     * @param queryFrom 讨论房间表
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> queryPage(Page<DiscussionRoom> page, DiscussionRoom queryFrom) {
        return baseMapper.queryPage(page, queryFrom);
    }
}
