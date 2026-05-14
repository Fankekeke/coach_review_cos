package com.fank.f1k2.business.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fank.f1k2.business.entity.AssessmentTask;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.LinkedHashMap;

/**
 * @author FanK fan1ke2ke@gmail.com（悲伤的橘子树）
 */
public interface IAssessmentTaskService extends IService<AssessmentTask> {

    /**
     * 分页获取自我评估任务表
     *
     * @param page      分页对象
     * @param queryFrom 自我评估任务表
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> queryPage(Page<AssessmentTask> page, AssessmentTask queryFrom);
}
