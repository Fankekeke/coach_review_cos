package com.fank.f1k2.business.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fank.f1k2.business.entity.DiscussionSpeechRecord;
import com.fank.f1k2.business.dao.DiscussionSpeechRecordMapper;
import com.fank.f1k2.business.service.IDiscussionSpeechRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK fan1ke2ke@gmail.com（悲伤的橘子树）
 */
@Service
public class DiscussionSpeechRecordServiceImpl extends ServiceImpl<DiscussionSpeechRecordMapper, DiscussionSpeechRecord> implements IDiscussionSpeechRecordService {

    /**
     * 分页获取讨论发言记录表
     *
     * @param page      分页对象
     * @param queryFrom 讨论发言记录表
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> queryPage(Page<DiscussionSpeechRecord> page, DiscussionSpeechRecord queryFrom) {
        return baseMapper.queryPage(page, queryFrom);
    }

    /**
     * 根据房间ID查询讨论记录
     *
     * @param roomId 房间ID
     * @return 讨论记录
     */
    @Override
    public List<LinkedHashMap<String, Object>> queryRecordByRoomId(Integer roomId) {
        return baseMapper.queryRecordByRoomId(roomId);
    }
}
