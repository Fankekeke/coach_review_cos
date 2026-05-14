package com.fank.f1k2.business.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fank.f1k2.business.entity.QuestionBank;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fank.f1k2.common.exception.F1k2Exception;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK fan1ke2ke@gmail.com（悲伤的橘子树）
 */
public interface IQuestionBankService extends IService<QuestionBank> {

    /**
     * 分页获取题库信息
     *
     * @param page      分页对象
     * @param queryFrom 题库信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> queryPage(Page<QuestionBank> page, QuestionBank queryFrom);

    /**
     * 新增题库信息
     *
     * @param questionBank 题库信息对象
     * @return 添加结果
     */
    boolean addQuestionBank(QuestionBank questionBank) throws F1k2Exception;

    /**
     * 修改题库信息
     *
     * @param questionBank 题库信息对象
     * @return 修改结果
     */
    boolean editQuestionBank(QuestionBank questionBank) throws F1k2Exception;

    /**
     * 查询题库信息列表
     *
     * @return 列表
     */
    List<LinkedHashMap<String, Object>> queryQuestionBankList();

    /**
     * 查询题库信息详情
     *
     * @param bandId 主键ID
     * @return 详情
     */
    LinkedHashMap<String, Object> queryBankDetail(Integer bandId);
}
