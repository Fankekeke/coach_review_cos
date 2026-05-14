package com.fank.f1k2.business.controller;


import cn.hutool.core.date.DateUtil;
import com.fank.f1k2.common.exception.F1k2Exception;
import com.fank.f1k2.common.utils.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fank.f1k2.business.entity.QuestionBank;
import com.fank.f1k2.business.service.IQuestionBankService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

import org.springframework.web.bind.annotation.RestController;

/**
 * 题库信息 控制层
 *
 * @author FanK fan1ke2ke@gmail.com（悲伤的橘子树）
 */
@RestController
@RequestMapping("/business/question-bank")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class QuestionBankController {

    private final IQuestionBankService bulletinInfoService;

    /**
     * 分页获取题库信息
     *
     * @param page      分页对象
     * @param queryFrom 题库信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<QuestionBank> page, QuestionBank queryFrom) {
        return R.ok(bulletinInfoService.queryPage(page, queryFrom));
    }

    /**
     * 查询题库信息详情
     *
     * @param id 主键ID
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(bulletinInfoService.getById(id));
    }

    /**
     * 查询题库信息列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(bulletinInfoService.list());
    }

    /**
     * 新增题库信息
     *
     * @param addFrom 题库信息对象
     * @return 结果
     */
    @PostMapping
    public R save(QuestionBank addFrom) throws F1k2Exception {
        addFrom.setCode("OB-" + System.currentTimeMillis());
        addFrom.setCreateDate(DateUtil.formatDateTime(new Date()));
        return R.ok(bulletinInfoService.addQuestionBank(addFrom));
    }

    /**
     * 查询题库信息列表（统计不同题目类型数量）
     *
     * @return 列表
     */
    @GetMapping("/queryBankList")
    public R queryBankList() {
        return R.ok(bulletinInfoService.queryQuestionBankList());
    }

    /**
     * 查询题库信息详情
     *
     * @param bandId 主键ID
     * @return 列表
     */
    @GetMapping("/queryBankDetail")
    public R queryBankDetail(Integer bandId) {
        return R.ok(bulletinInfoService.queryBankDetail(bandId));
    }

    /**
     * 修改题库信息
     *
     * @param editFrom 题库信息对象
     * @return 结果
     */
    @PutMapping
    public R edit(QuestionBank editFrom) throws F1k2Exception {
        return R.ok(bulletinInfoService.editQuestionBank(editFrom));
    }

    /**
     * 删除题库信息
     *
     * @param ids 删除的主键ID
     * @return 结果
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(bulletinInfoService.removeByIds(ids));
    }

}
