package com.fank.f1k2.business.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fank.f1k2.business.entity.ContentVotes;
import com.fank.f1k2.business.dao.ContentVotesMapper;
import com.fank.f1k2.business.service.IContentVotesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * @author FanK fan1ke2ke@gmail.com（悲伤的橘子树）
 */
@Service
public class ContentVotesServiceImpl extends ServiceImpl<ContentVotesMapper, ContentVotes> implements IContentVotesService {

    /**
     * 分页获取顶踩投票记录表
     *
     * @param page      分页对象
     * @param queryFrom 顶踩投票记录表
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> queryPage(Page<ContentVotes> page, ContentVotes queryFrom) {
        return baseMapper.queryPage(page, queryFrom);
    }
}
