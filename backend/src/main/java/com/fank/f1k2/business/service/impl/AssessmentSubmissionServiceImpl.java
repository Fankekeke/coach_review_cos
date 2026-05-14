package com.fank.f1k2.business.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fank.f1k2.business.entity.AssessmentSubmission;
import com.fank.f1k2.business.dao.AssessmentSubmissionMapper;
import com.fank.f1k2.business.service.IAssessmentSubmissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * @author FanK fan1ke2ke@gmail.com（悲伤的橘子树）
 */
@Service
public class AssessmentSubmissionServiceImpl extends ServiceImpl<AssessmentSubmissionMapper, AssessmentSubmission> implements IAssessmentSubmissionService {

    /**
     * 分页获取评估任务提交表
     *
     * @param page      分页对象
     * @param queryFrom 评估任务提交表
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> queryPage(Page<AssessmentSubmission> page, AssessmentSubmission queryFrom) {
        return baseMapper.queryPage(page, queryFrom);
    }
}
