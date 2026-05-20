package com.fank.f1k2.business.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fank.f1k2.business.entity.DiscussionSpeechRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK fan1ke2ke@gmail.com（悲伤的橘子树）
 */
public interface DiscussionSpeechRecordMapper extends BaseMapper<DiscussionSpeechRecord> {

    /**
     * 分页获取讨论发言记录表
     *
     * @param page      分页对象
     * @param queryFrom 讨论发言记录表
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> queryPage(Page<DiscussionSpeechRecord> page, @Param("queryFrom") DiscussionSpeechRecord queryFrom);

    /**
     * 根据房间ID查询讨论记录
     *
     * @param roomId 房间ID
     * @return 讨论记录
     */
    List<LinkedHashMap<String, Object>> queryRecordByRoomId(@Param("roomId") Integer roomId);
}
