package com.fank.f1k2.business.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fank.f1k2.business.entity.AssessmentTask;
import com.fank.f1k2.business.dao.AssessmentTaskMapper;
import com.fank.f1k2.business.service.IAssessmentTaskService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * @author FanK fan1ke2ke@gmail.com（悲伤的橘子树）
 */
@Service
public class AssessmentTaskServiceImpl extends ServiceImpl<AssessmentTaskMapper, AssessmentTask> implements IAssessmentTaskService {


    /**
     * 分页获取自我评估任务表
     *
     * @param page      分页对象
     * @param queryFrom 自我评估任务表
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> queryPage(Page<AssessmentTask> page, AssessmentTask queryFrom) {
        return baseMapper.queryPage(page, queryFrom);
    }
}
