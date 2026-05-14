package com.fank.f1k2.business.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fank.f1k2.business.entity.AnswerRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fank.f1k2.common.exception.F1k2Exception;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK fan1ke2ke@gmail.com（悲伤的橘子树）
 */
public interface IAnswerRecordService extends IService<AnswerRecord> {

    /**
     * 分页获取答题记录
     *
     * @param page       分页对象
     * @param queryFrom 答题记录
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> queryPage(Page<AnswerRecord> page, AnswerRecord queryFrom);

    /**
     * 新增答题记录
     *
     * @param answerRecord 答题记录对象
     * @return 添加结果
     */
    boolean addAnswerRecord(AnswerRecord answerRecord) throws F1k2Exception;
}
