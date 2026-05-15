package com.fank.f1k2.business.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fank.f1k2.business.entity.PartyQuestions;
import com.fank.f1k2.business.dao.PartyQuestionsMapper;
import com.fank.f1k2.business.service.IPartyQuestionsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK fan1ke2ke@gmail.com（悲伤的橘子树）
 */
@Service
public class PartyQuestionsServiceImpl extends ServiceImpl<PartyQuestionsMapper, PartyQuestions> implements IPartyQuestionsService {

    /**
     * 分页获取教练问题表
     *
     * @param page      分页对象
     * @param queryFrom 教练问题表
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> queryPage(Page<PartyQuestions> page, PartyQuestions queryFrom) {
        return baseMapper.queryPage(page, queryFrom);
    }

    /**
     * 查询所有问题
     *
     * @return 列表
     */
    @Override
    public List<LinkedHashMap<String, Object>> queryAllQuestions() {
        return baseMapper.queryAllQuestions();
    }
}
