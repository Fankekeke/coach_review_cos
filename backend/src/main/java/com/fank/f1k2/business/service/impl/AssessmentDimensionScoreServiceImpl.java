package com.fank.f1k2.business.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fank.f1k2.business.entity.AssessmentDimensionScore;
import com.fank.f1k2.business.dao.AssessmentDimensionScoreMapper;
import com.fank.f1k2.business.service.IAssessmentDimensionScoreService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * @author FanK fan1ke2ke@gmail.com（悲伤的橘子树）
 */
@Service
public class AssessmentDimensionScoreServiceImpl extends ServiceImpl<AssessmentDimensionScoreMapper, AssessmentDimensionScore> implements IAssessmentDimensionScoreService {

    /**
     * 分页获取评估维度得分表
     *
     * @param page      分页对象
     * @param queryFrom 评估维度得分表
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> queryPage(Page<AssessmentDimensionScore> page, AssessmentDimensionScore queryFrom) {
        return baseMapper.queryPage(page, queryFrom);
    }
}
