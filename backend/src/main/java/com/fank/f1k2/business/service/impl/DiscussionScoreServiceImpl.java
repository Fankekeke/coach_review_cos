package com.fank.f1k2.business.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fank.f1k2.business.entity.DiscussionScore;
import com.fank.f1k2.business.dao.DiscussionScoreMapper;
import com.fank.f1k2.business.service.IDiscussionScoreService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * @author FanK fan1ke2ke@gmail.com（悲伤的橘子树）
 */
@Service
public class DiscussionScoreServiceImpl extends ServiceImpl<DiscussionScoreMapper, DiscussionScore> implements IDiscussionScoreService {

    /**
     * 分页获取讨论评分表
     *
     * @param page      分页对象
     * @param queryFrom 讨论评分表
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> queryPage(Page<DiscussionScore> page, DiscussionScore queryFrom) {
        return baseMapper.queryPage(page, queryFrom);
    }
}
